package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class JobService {
    @Autowired
    JobRepo jobRepo;
    public void setStatus(Long jobId, boolean active){
        Optional<Job> job=jobRepo.findById(jobId);
        if(job.isPresent()){
            Job job1=job.get();
            job1.setActive(active);
            jobRepo.save(job1);
        }
    }
}
