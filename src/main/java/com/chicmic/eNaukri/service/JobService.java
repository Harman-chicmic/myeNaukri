package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class JobService {
    @Autowired
    JobRepo jobRepo;
    public void setStatus(Long jobId, boolean active){

    }
}
