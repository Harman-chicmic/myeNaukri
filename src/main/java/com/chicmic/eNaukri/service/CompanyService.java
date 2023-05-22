package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.ExperienceRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepo;
    private final JobService jobService;
    private final ExperienceRepo experienceRepo;
    private final UserServiceImpl userService;


    public boolean jobExistsForCompany(Long id, Long jobId) {
        Company company=companyRepo.findById(id).get();
        final boolean[] flag = {false};
        company.getJobList().forEach(job -> {
            if(jobId.equals(job.getJobId())) flag[0] =true;
        });
        return flag[0];
    }
}
