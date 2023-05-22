package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.controller.UserController;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UsersRepo usersRepo;
    private final ApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final ExperienceRepo experienceRepo;
    private final SkillsRepo skillsRepo;
    private final CompanyRepo companyRepo;
    private final UserTokenRepo tokenRepo;
    private final JobSkillsRepo jobSkillsRepo;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    JavaMailSender javaMailSender;

    private Logger logger= LoggerFactory.getLogger(UserController.class);
    public static final String imagePath= "/home/chicmic/Downloads/JobPortal/src/main/resources/static/assets/img/";
    public static final String resumePath = "/home/chicmic/Downloads/JobPortal/src/main/resources/static/assets/files/";



    public void saveUUID(UserToken userToken) {
        tokenRepo.save(userToken);
    }

    public Users getUserByEmail(String username) {
        return usersRepo.findByEmail(username);
    }

    public Users findUserFromUUID(String token) {
        UserToken userToken= tokenRepo.findByToken(token);
        return usersRepo.findById(userToken.getUserr().getUserId()).get();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("AuthHeader".equals(cookie.getName())){
                    tokenRepo.deleteByToken(cookie.getValue());
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);break;
                }
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user= usersRepo.findByEmail(username);
//        if(user==null)throw new CustomApiExceptionHandler(HttpStatus.NOT_FOUND,username + "doesn't exists, please REGISTER.");

        Collection<Authority> authorites=new ArrayList<>();
        authorites.add(new Authority("USER"));

        return new User(user.getEmail(),user.getPassword(),authorites);
    }

    public String findCurrentCompany(Long id) {
        Users users=usersRepo.findById(id).get();
       return experienceRepo.findByExpUserAndCurrentlyWorking(users,true).getExCompany().getCompanyName();
    }

    public void changeAlerts(Long id, boolean b) {
        Users temp=usersRepo.findById(id).get();
        temp.setEnableNotification(b);
        usersRepo.save(temp);
    }

    public boolean checkJobForuser(Long id, Long jobId) {
        Users temp=usersRepo.findById(id).get();
        Job job=jobRepo.findById(jobId).get();
        return applicationRepo.existsByApplicantIdAndJobId(temp,job);
    }

    public void withdrawApxn(Long id, Long jobId) {
        Users temp=usersRepo.findById(id).get();
        Job job=jobRepo.findById(jobId).get();

        Application application= applicationRepo.findByApplicantIdAndJobId(temp,job);
        application.setWithdraw(true);
        applicationRepo.save(application);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }

    @Async
    public void register(Users user) {
        // Generate OTP
        String otp =Integer.toString(new Random().nextInt(999999));
        System.out.println(otp);

        // Send OTP to user's email
        String subject = "OTP for user registration";
        String message = "Your OTP is: " + otp;
        user.setOtp(otp);
        usersRepo.save(user);
        sendEmail(user.getEmail(), subject, message);

    }
    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("harmanjyot.singh@chicmic.co.in");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
    public boolean verify(String email, String otp) {
        // Get user by email
        Users user = usersRepo.findByEmail(email);
        if (user == null) {
            return false;
        }

        // Check if OTP is correct
        if (user.getOtp().equals(otp)) {
            // Update user's OTP status to verified
            user.setVerified(true);
            //userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public void updateUser(UsersDto user, MultipartFile imgFile, MultipartFile resumeFile) throws IOException {
        Users existingUser=usersRepo.findByEmail(user.getEmail());
        if(user.getFullName()!=null){
            existingUser.setFullName(user.getFullName());
        }
        if(user.getPhoneNumber()!=null){
            existingUser.setPhoneNumber(user.getPhoneNumber());
        }
        if (user.getCurrentCompany()!=null){
            existingUser.setCurrentCompany(user.getCurrentCompany());
        }
        if(user.getBio()!=null){
            existingUser.setBio(user.getBio());
        }
        if(!imgFile.isEmpty()){
            String imgFolder = imagePath;
            System.out.println(imagePath);
            byte[] imgFileBytes = imgFile.getBytes();
            Path imgPath = Paths.get(imgFolder +  imgFile.getOriginalFilename());
            Files.write(imgPath, imgFileBytes);
            String ppPath="/static/assets/img" +imgFile.getOriginalFilename();
            existingUser.setPpPath(ppPath);
        }
        if(!resumeFile.isEmpty()){
            String resumeFolder=resumePath;
            byte[] resumeFileBytes= resumeFile.getBytes();
            Path resumePath=Paths.get(resumeFolder+resumeFile.getOriginalFilename());
            Files.write(resumePath,resumeFileBytes);
            String cvPath="/static/assets/files" +resumeFile.getOriginalFilename();
            existingUser.setCvPath(cvPath);
        }
        usersRepo.save(existingUser);
    }

    public void saveUser(UsersDto dto, MultipartFile imgFile, MultipartFile resumeFile) throws IOException {
        String userToken=UUID.randomUUID().toString();
        String fullName=dto.getFullName();
        String email= dto.getEmail();
        String phone=dto.getPhoneNumber();
        String password=dto.getPassword();
        String bio=dto.getBio();
        String imgFolder = imagePath;
        String resumeFolder=resumePath;
        System.out.println(imagePath);
        Users user=Users.builder()
                .fullName(fullName)
                .email(email)
                .phoneNumber(phone)
                .password(passwordEncoder().encode(password))
                .uuid(userToken)
                .bio(bio)
                .enableNotification(true)
                .build();
        if(!imgFile.isEmpty()&&!resumeFile.isEmpty()){
            byte[] imgFileBytes = imgFile.getBytes();
            byte[] resumeFileBytes= resumeFile.getBytes();
            Path imgPath = Paths.get(imgFolder +  imgFile.getOriginalFilename());
            Path resumePath=Paths.get(resumeFolder+resumeFile.getOriginalFilename());
            logger.info(imgPath.toString()+resumePath.toString());
            Files.write(imgPath, imgFileBytes);
            Files.write(resumePath,resumeFileBytes);
            user=Users.builder()
                    .ppPath("/static/assets/img" +imgFile.getOriginalFilename())
                    .cvPath("/static/assets/files" +resumeFile.getOriginalFilename()).build();
        }
       register(user);
    }
}
