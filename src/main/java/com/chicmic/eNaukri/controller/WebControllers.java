package com.chicmic.eNaukri.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebControllers {
    @GetMapping("/signup")
    public String signup(){return "signup";}
}
