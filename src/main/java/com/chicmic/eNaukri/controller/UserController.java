package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.service.JobService;
import com.chicmic.eNaukri.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;



import com.chicmic.eNaukri.Dto.UserEducationDto;
import com.chicmic.eNaukri.Dto.UserExperienceDto;
import com.chicmic.eNaukri.Dto.UserSkillDto;
import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final SkillsService skillsService;
    private final ExperienceService experienceService;
    private final EducationService educationService;

    @GetMapping("{id}")
    public void getUser(@PathVariable Long id){

    }
    @GetMapping("{id}/update-profile")
    public void updatePage(@PathVariable Long id){

    }
    @PostMapping("{id}/update-profile")
    public void updateProfile(UsersDto user, @RequestParam("resumeFile")MultipartFile resumeFile, @RequestParam("imgFile")MultipartFile imgFile) throws IOException {
        userService.updateUser(user,imgFile,resumeFile);
    }
    @GetMapping("{id}/checkCompany")
    public String returnCurrentCompany(@PathVariable Long id){
        return userService.findCurrentCompany(id);
    }

    @PostMapping("{id}/addedu")
    public void addEducation(@RequestBody Education education){

    }
    @PostMapping("{id}/job/{jobId}/withdraw")
    public ResponseEntity<?> withdrawApxn(@PathVariable("id") Long id,@PathVariable("jobId") Long jobId) {
        if (userService.checkJobForuser(id, jobId)) {
            userService.withdrawApxn(id, jobId);
            return ResponseEntity.ok().body("Withdrawn");
        }
        return ResponseEntity.badRequest().body("The job you are withdrawing doesn't exists for user !");
    }
    @PostMapping("{id}/addexp")
    public void addExperience(){

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

    @PostMapping("{id}/post")
    public String postJob(@RequestBody Job job,@RequestParam("company")String postedFor) {
        jobService.saveJob(job, postedFor);
        return "Wooho, Job posted !";
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

}
