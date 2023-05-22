package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebControllers {
    @Autowired
    JobService jobService;
    @GetMapping("/signup")
    public String signup(){return "signup";}
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<String> nwe(@Param("jobId") Long jobId){
        jobService.getUsersWithMatchingSkills(jobId);
        return ResponseEntity.ok("k");
    }
    @GetMapping("{id}/update-profile")
    public void updatePage(){

    }
}
