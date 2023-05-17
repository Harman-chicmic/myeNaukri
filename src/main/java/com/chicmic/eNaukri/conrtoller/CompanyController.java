package com.chicmic.eNaukri.conrtoller;

import com.chicmic.eNaukri.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company/")
@RequiredArgsConstructor
public class CompanyController {
    private final UserServiceImpl userService;
}
