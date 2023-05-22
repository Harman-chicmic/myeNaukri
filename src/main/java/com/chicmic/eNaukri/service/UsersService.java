package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service public class UsersService {
    public static final String imagePath= "/home/chicmic/Downloads/JobPortal/src/main/resources/static/assets/img/";
    public static final String resumePath = "/home/chicmic/Downloads/JobPortal/src/main/resources/static/assets/files/";
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    JavaMailSender javaMailSender;
    public Users getUserByEmail(String email) {
        return usersRepo.findByEmail(email);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }

    public void saveUser(Users user) {
        usersRepo.save(user);
    }

    public Users getUser(String email) {
        return usersRepo.findByEmail(email);
    }
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
        sendEmailForOtp(user.getEmail(), subject, message);

        // Save user to database
        // ...

//        return user;
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
