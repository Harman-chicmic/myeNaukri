package com.chicmic.eNaukri.controller;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.service.PasswordResetService;
import com.chicmic.eNaukri.service.UsersService;
import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.Users;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserControllers {

    UsersService usersService;

    PasswordResetService passwordResetService;

    BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/set-new-password")
    public void setPassword(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        Users user = usersService.getUserByEmail(email);
        passwordResetService.createPasswordResetTokenForUser(user);
    }
    @GetMapping("/enterNewPassword/{token}/{uuid}")
    public String Enter(HttpServletRequest request, @PathVariable("token") String token, @PathVariable("uuid") String uuid) {
        PasswordResetToken passwordResetRequest = passwordResetService.findByToken(token);
        if (passwordResetRequest == null || passwordResetRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "redirect:/login?error=InvalidToken";
        }
        return "forgotPasswordForm";
    }
    @PostMapping("/enterNewPassword/{token}/{uuid}")
    public String resetPassword(HttpServletRequest request,@PathVariable("token") String token,@PathVariable("uuid") String uuid){
        PasswordResetToken passwordResetRequest = passwordResetService.findByToken(token);
        Users user = usersService.getUserByUuid(uuid);
        String newPassword=request.getParameter("password");
        if (passwordResetRequest == null || passwordResetRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "redirect:/login?error=InvalidToken";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        usersService.saveUser(user);
        passwordResetService.delete(passwordResetRequest);
        return "user-login";
    }

}
