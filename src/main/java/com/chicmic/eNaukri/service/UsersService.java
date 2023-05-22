package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

@Service public class UsersService {
    @Value("${my.imagePath.string}")
    String imagePath;
    @Value("${my.cvPath.string}")
    String resumePath;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    public Users getUserByEmail(String email) {
        return usersRepo.findByEmail(email);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }
    public void saveUser(Users user) {
        usersRepo.save(user);
    }

    @Async
    public void register(UsersDto dto, MultipartFile imgFile,MultipartFile resumeFile) throws IOException {
        String uuid= UUID.randomUUID().toString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Users newUser = mapper.convertValue(dto, Users.class);
        if(!imgFile.isEmpty()){
            String imgFolder = imagePath;
            System.out.println(imagePath);
            byte[] imgFileBytes = imgFile.getBytes();
            Path imgPath = Paths.get(imgFolder +  imgFile.getOriginalFilename());
            Files.write(imgPath, imgFileBytes);
            String ppPath="/static/assets/img" +imgFile.getOriginalFilename();
            newUser.setPpPath(ppPath);
        }
        if(!resumeFile.isEmpty()){
            String resumeFolder=resumePath;
            byte[] resumeFileBytes= resumeFile.getBytes();
            Path resumePath=Paths.get(resumeFolder+resumeFile.getOriginalFilename());
            Files.write(resumePath,resumeFileBytes);
            String cvPath="/static/assets/files" +resumeFile.getOriginalFilename();
            newUser.setCvPath(cvPath);
        }
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setUuid(uuid);
        // Generate OTP
        String otp =Integer.toString(new Random().nextInt(999999));
        // Send OTP to user's email
        String subject = "OTP for user registration";
        String message = "Your OTP is: " + otp;
        newUser.setOtp(otp);
        usersRepo.save(newUser);
        sendEmailForOtp(newUser.getEmail(), subject, message);
    }

    private void sendEmailForOtp(String to, String subject, String body) {
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
            user.setOtpVerified(true);
            //userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    public void updateUser(UsersDto user, MultipartFile imgFile,MultipartFile resumeFile) throws IOException {
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
}
