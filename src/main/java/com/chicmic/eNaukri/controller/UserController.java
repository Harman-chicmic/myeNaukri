package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

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

}
