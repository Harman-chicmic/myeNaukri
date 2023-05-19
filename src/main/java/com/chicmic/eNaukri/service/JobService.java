package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.Skills;
import com.chicmic.eNaukri.model.UserSkills;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class JobService {
    @Autowired
    JobRepo jobRepo;
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    UsersRepo usersRepo;
    public void saveJob(Job job, String postedFor) {
        Job newJob=new Job();
        newJob.setJobTitle(job.getJobTitle());
        newJob.setJobDesc(job.getJobDesc());
        newJob.setActive(true);
        newJob.setPostedOn(LocalDate.now());
        newJob.setExpiresAt(job.getExpiresAt());
        newJob.setPostFor(companyRepo.findByCompanyName(postedFor.trim()));
        jobRepo.save(newJob);
        List<Users> users=usersRepo.findAll();
        for(Users user:users.toArray(new Users[0])){
            List<UserSkills> skills=user.getUserSkillsList();
        }
    }
    public void setStatus(Long jobId, boolean active){
        Optional<Job> job=jobRepo.findById(jobId);
        if(job.isPresent()){
            Job job1=job.get();
            job1.setActive(active);
            jobRepo.save(job1);
        }
    }
}
