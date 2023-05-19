package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.service.JobService;
import com.chicmic.eNaukri.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final UsersRepo usersRepo;
    private final UserServiceImpl userService;
    private final JobService jobService;

    @GetMapping
    public String homePage(){
        System.out.println("1");
        return "In Home Page";
    }
    @GetMapping("login-page")
    public String loginPage(){
        return "in Login Page";
    }
    @PostMapping("login")
    public String userLogin(@RequestBody Map<Object,Object> map){
        return "login successful";
    }
    @GetMapping("signup")
    public String signupPage(){
        return "In signup Page";
    }
    @PostMapping("signup")
    public void userSignup(@RequestBody Map<Object,Object> map){
    }
    @GetMapping("logout-user")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        userService.logout(request,response);
        return "Logout Successful";
    }
    @GetMapping("forgot-password")
    public String forgotPassword(){
        return "forgot password?";
    }
    @PostMapping("forgot-password")
    public boolean sendForgotPaswdLink(@RequestParam String email){
        return true;
    }
    @GetMapping("jobs")
    public List<Job> displayJobs(@RequestParam(required = false,name = "q") String query,
                                 @RequestParam(required = false,name = "location") String location,
                                 @RequestParam(required = false,name = "type") String jobType,
                                 @RequestParam(required = false,name = "postedOn") String postedOn,
                                 @RequestParam(required = false,name = "remoteHybridOnsite") String remoteHybridOnsite){
        return jobService.displayFilteredPaginatedJobs(query,location,jobType,postedOn,remoteHybridOnsite);
    }
}
