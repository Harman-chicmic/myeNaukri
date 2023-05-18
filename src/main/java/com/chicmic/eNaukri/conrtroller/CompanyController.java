package com.chicmic.eNaukri.conrtroller;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company/")
@RequiredArgsConstructor
public class CompanyController {
    private final UserServiceImpl userService;
    private final JobRepo jobRepo;
    private final CompanyRepo companyRepo;

    @GetMapping("{id}")
    public Company companyPage(@PathVariable Long id){
        Company company=companyRepo.findById(id).get();
        List<Job> jobList=new ArrayList<>();
        jobList.add(jobRepo.findById(1l).get());
        jobList.add(jobRepo.findById(2l).get());
        company.setJobList(jobList);
        return companyRepo.save(company);
    }
}
