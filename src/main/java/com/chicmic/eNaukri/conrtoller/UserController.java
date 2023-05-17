package com.chicmic.eNaukri.conrtoller;

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
    public void updateUser(@RequestBody Map<Object,Object> map){

    }
    @GetMapping("{id}/myapplications")
    public void myApplications(){

    }
    @PostMapping("{id}/myapplications/withdraw")
    public void withdrawApxn(@RequestParam String job){

    }

}
