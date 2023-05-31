package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.UserEducationDto;
import com.chicmic.eNaukri.Dto.UserExperienceDto;
import com.chicmic.eNaukri.Dto.UserSkillDto;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @Autowired
    SkillsService skillsService;
    EducationService educationService;
    @Autowired ExperienceService experienceService;
    @Autowired ApplicationService applicationService;
    UserServiceImpl userService;
    @Autowired
    UsersService usersService;
    SocialLinkService linkService;
    JobService jobService;

    @GetMapping("{id}")
    public void getUser(){

    }
    @PostMapping("{id}/update-profile")
    public void updateProfile(UsersDto user, @RequestParam("resumeFile")MultipartFile resumeFile, @RequestParam("imgFile")MultipartFile imgFile) throws IOException {
        usersService.updateUser(user,imgFile,resumeFile);
    }
    @GetMapping("{userId}/myapplications")
    public ResponseEntity<String> myApplications(@PathVariable("userId") Long userId){
        applicationService.viewApplications(userId);
        return ResponseEntity.ok("list of your applications");
    }
    @PostMapping("{userId}/skills")
    public ResponseEntity<String> selectUserSkills(
            @PathVariable("userId") Long userId,
            @RequestBody UserSkillDto dto) {
        dto.setUserId(userId);
        skillsService.addSkills(dto);
        return ResponseEntity.ok("Skills selected successfully.");
    }
    @PostMapping("{userId}/education")
    public ResponseEntity<String> selectUserEducation(
            @PathVariable("userId") Long userId,
            @RequestBody UserEducationDto dto) {
        educationService.addEducation(dto,userId);
        return ResponseEntity.ok("Educational qualification added to the user");
    }
    @PostMapping("{userId}/experience")
    public ResponseEntity<String> selectExperience(
            @PathVariable("userId") Long userId,
            @RequestBody UserExperienceDto dto) {
        experienceService.addExperience(userId,dto);
        return ResponseEntity.ok("Experience added to the user");
    }
    @PostMapping("{userId}/{jobId}/apply")
    public ResponseEntity<String> apply(
            @PathVariable("userId") Long userId, @PathVariable() Long jobId,@RequestParam("resumeFile") MultipartFile resumeFile,
            @RequestBody Application application)throws IOException,MessagingException {
        applicationService.applyForJob(application,resumeFile,userId,jobId);
        return ResponseEntity.ok("Successfully applied to the job");
    }
    @GetMapping("{id}/unsubscribe")
    public String unsubscribe(@PathVariable("id") Long id){
        userService.changeAlerts(id,false);
        return "Unsubscribed !";
    }
    @GetMapping("{id}/subscribe")
    public String subscribe(@PathVariable("id") Long id){
        userService.changeAlerts(id,true);
        return "Subscribed !";
    }
    @GetMapping("{id}/currentCompany")
    public String currentCompany(@PathVariable("id") Long id){
        return userService.findCurrentCompany(id);
    }
    @GetMapping("{userId}/{jobId}/checkApplication")
    public String check(@PathVariable("userId") Long userId, @PathVariable("jobId") Long jobId){
        userService.checkJobForUser(userId, jobId);
        return "";
    }
    @GetMapping("{userId}/{jobId}/withdraw")
    public void withdraw(@PathVariable("userId") Long userId, @PathVariable("jobId") Long jobId){
        userService.withdrawApxn(userId, jobId);
    }
    @GetMapping("{jobId}/numApplicants")
    public String numApplicants(@PathVariable Long jobId){
        return String.valueOf(applicationService.getNumApplicantsForJob(jobId));
    }
    @PostMapping("{userid}/addSocialLinks")
    public ResponseEntity<String> addSocialLinks(@PathVariable Long userId, SocialLink dto){
        linkService.addSocialLinks(userId, dto,null);
        return ResponseEntity.ok("Added social links");
    }
    @PostMapping("{userId}/verify")
    public ResponseEntity<String> verify(@PathVariable("userId") Long userId,@RequestParam String otp){
        boolean isVerified = usersService.verify(userId, otp);
        if (isVerified) {
            return ResponseEntity.ok("User verification successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP. User verification failed.");
        }
    }
}
