package com.chicmic.eNaukri.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeConrtoller {

    @GetMapping
    public void homePage(){

    }
    @GetMapping("login")
    public void loginPage(){

    }
    @PostMapping("login")
    public boolean userLogin(@RequestBody Map<Object,Object> map){
        return true;
    }
    @GetMapping("signup")
    public void signupPage(){

    }
    @PostMapping("signup")
    public void userSignup(@RequestBody Map<Object,Object> map){

    }
    @GetMapping("logout")
    public void logout(){

    }
    @GetMapping("forgot-password")
    public void forgotPassword(){

    }
    @PostMapping("forgot-password")
    public boolean sendForgotPaswdLink(@RequestParam String email){
        return true;
    }

}
