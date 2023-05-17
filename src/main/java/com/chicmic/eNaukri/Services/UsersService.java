package com.chicmic.eNaukri.Services;

import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service public class UsersService {
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
        int otp = new Random().nextInt(999999);
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
    public boolean verify(String email, int otp) {
        // Get user by email
        Users user = usersRepo.findByEmail(email);
        if (user == null) {
            return false;
        }

        // Check if OTP is correct
        if (user.getOtp() == otp) {
            // Update user's OTP status to verified
            user.setOtpVerified(true);
            //userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    public void updateUser(String fullName, String cvPath, String email, String phoneNumber, String currentCompany, String ppPath){
        Users existingUser=usersRepo.findByEmail(email);
        if(fullName!=null){
            existingUser.setFullName(fullName);
        }
        if(phoneNumber!=null){
            existingUser.setPhoneNumber(phoneNumber);
        }
        if (currentCompany!=null){
            existingUser.setCurrentCompany(currentCompany);
        }
        if (cvPath!=null){
            existingUser.setCvPath(cvPath);
        }
        if(ppPath!=null){
            existingUser.setPpPath(ppPath);
        }
        usersRepo.save(existingUser);
    }
}
