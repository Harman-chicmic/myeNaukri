package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.UserEducationDto;
import com.chicmic.eNaukri.Dto.UserExperienceDto;
import com.chicmic.eNaukri.Dto.UserSkillDto;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
    UsersService usersService;
    SkillsService skillsService;
    EducationService educationService;
    ExperienceService experienceService;
    ApplicationService applicationService;
    PasswordResetService passwordResetService;
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("{id}")
    public void getUser(){

    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(UsersDto dto, @RequestParam("imgFile") MultipartFile imgFile,
                                           @RequestParam("resumeFile") MultipartFile resumeFile) throws IOException {
        usersService.register(dto,imgFile,resumeFile);
        return ResponseEntity.ok("User Registered");
    }
    @PutMapping("/updateProfile")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProfile(UsersDto user, @RequestParam("resumeFile")MultipartFile resumeFile, @RequestParam("imgFile")MultipartFile imgFile) throws IOException {
        usersService.updateUser(user,imgFile,resumeFile);
    }
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
    @GetMapping("{userId}/myapplications")
    public ResponseEntity<String> myApplications(@PathVariable Long userId){
        applicationService.viewApplications(userId);
        return ResponseEntity.ok("list of your applications");
    }
    @PostMapping("{id}/myapplications/withdraw")
    public void withdrawApxn(@RequestParam String job){
    }
    @PutMapping("/{userId}/skills")
    public ResponseEntity<String> selectUserSkills(
            @PathVariable Long userId,
            @RequestBody UserSkillDto dto) {
        dto.setUserId(userId);
        skillsService.addSkills(dto);
        return ResponseEntity.ok("Skills selected successfully.");
    }
    @PostMapping("/{userId}/education")
    public ResponseEntity<String> selectUserEducation(
            @PathVariable Long userId,
            @RequestBody UserEducationDto dto) {
        educationService.addEducation(dto,userId);
        return ResponseEntity.ok("Educational qualification added to the user");
    }
    @PostMapping("/{userId}/experience")
    public ResponseEntity<String> selectExperience(
            @PathVariable Long userId,
            @RequestBody UserExperienceDto dto) {
        experienceService.addExperience(userId,dto);
        return ResponseEntity.ok("Experience added to the user");
    }
    @PostMapping("/{userId}/{jobId}/apply")
    public ResponseEntity<String> apply(
            @PathVariable Long userId, @PathVariable Long jobId,
            MultipartFile resumeFile, @RequestBody Application application)throws IOException {
        applicationService.applyForJob(application,resumeFile,userId,jobId);
        return ResponseEntity.ok("Successfully applied to the user");
    }
}
