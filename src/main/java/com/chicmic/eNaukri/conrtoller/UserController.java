package com.chicmic.eNaukri.conrtoller;

import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("{id}")
    public void getUser(@PathVariable Long id){

    }
    @GetMapping("{id}/update-profile")
    public void updatePage(@PathVariable Long id){

    }
    @PostMapping("{id}/update-profile")
    public void updateUser(@PathVariable Long id,@RequestBody Map<Object,Object> map){

    }
    @GetMapping("{id}/myapplications")
    public void myApplications(@PathVariable Long id){

    }
    @PostMapping("{id}/myapplications/withdraw")
    public void withdrawApxn(@PathVariable Long id,@RequestParam String job){
    }
    @GetMapping("{id}/checkCompany")
    public String returnCurrentCompany(@PathVariable Long id){
        return userService.findCurrentCompany(id);
    }

    @PostMapping("{id}/post")
    public String postJob(@RequestBody Job job,@RequestParam("company")String postedFor){
        userService.saveJob(job,postedFor);
        return "Wooho, Job posted !";
    }
}
