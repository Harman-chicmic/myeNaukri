package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.JobDto;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.SocialLink;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.service.CompanyService;
import com.chicmic.eNaukri.service.JobService;
import com.chicmic.eNaukri.service.SocialLinkService;
import com.chicmic.eNaukri.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company/")
@RequiredArgsConstructor
public class CompanyController {
    private final UserServiceImpl userService;
    private final JobRepo jobRepo;
    private final CompanyRepo companyRepo;
    private final CompanyService companyService;
    @Autowired JobService jobService;
    SocialLinkService linkService;

    @GetMapping("{id}")
    public Company companyPage(@PathVariable Long id){
        return companyRepo.findById(id).get();
    }
    @GetMapping("{id}/jobs/{jobId}")
    public ResponseEntity<?> getjobFromCompany(@PathVariable("id")Long id, @PathVariable("jobId") Long jobId){
        if(companyService.jobExistsForCompany(id,jobId)){
            Job job=jobRepo.findById(jobId).get();
            return ResponseEntity.ok(job);
        }
        return (ResponseEntity<?>) ResponseEntity.badRequest().body("Job that you are looking for doesn't exists for this company !");
    }
    @PostMapping("{id}/postJob")
    public ResponseEntity<String> postJob(@RequestBody JobDto job, @PathVariable Long id){
        jobService.saveJob(job,id);
        return ResponseEntity.ok("Job successfully posted");
    }
    @PutMapping("{id}/setStatus")
    public ResponseEntity<String> setStatus(@PathVariable Long id,boolean active){
        jobService.setStatus(id,active);
        return ResponseEntity.ok("Status changed");
    }
    @PostMapping("{id}/addSocialLinks")
    public ResponseEntity<String> addSocialLinks(@PathVariable Long id, SocialLink dto){
        linkService.addSocialLinks(null, dto,id);
        return ResponseEntity.ok("Added social links");
    }
}
