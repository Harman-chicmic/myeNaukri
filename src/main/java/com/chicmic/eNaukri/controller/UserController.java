package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.UserEducationDto;
import com.chicmic.eNaukri.Dto.UserExperienceDto;
import com.chicmic.eNaukri.Dto.UserSkillDto;
import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UsersService usersService;
    @Autowired
    SkillsService skillsService;
    @Autowired
    EducationService educationService;
    @Autowired
    ExperienceService experienceService;
    @Autowired
    ApplicationService applicationService;

    @GetMapping("{id}")
    public void getUser(){

    }
    @GetMapping("{id}/update-profile")
    public void updatePage(){

    }
    @PostMapping("{id}/update-profile")
    public void updateUser(@RequestBody Users users){

    }
    @PostMapping("{id}/addedu")
    public void addEducation(@RequestBody Education education){

    }
    @PostMapping("{id}/addexp")
    public void addExperience(){

    }
    @GetMapping("{id}/myapplications")
    public void myApplications(){

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
