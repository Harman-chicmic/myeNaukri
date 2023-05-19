package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service public class JobService {
    @Autowired
    JobRepo jobRepo;
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    JobSkillsRepo jobSkillsRepo;
    @Autowired
    UserSkillsRepo userSkillsRepo;
    @Autowired
    SkillsRepo skillsRepo;
    @PersistenceContext
    EntityManager entityManager;
    public void saveJob(Job job, String postedFor) {
        Job newJob=new Job();
        newJob.setJobTitle(job.getJobTitle());
        newJob.setJobDesc(job.getJobDesc());
        newJob.setActive(true);
        newJob.setPostedOn(LocalDate.now());
        newJob.setExpiresAt(job.getExpiresAt());
        newJob.setPostFor(companyRepo.findByCompanyName(postedFor.trim()));
        jobRepo.save(newJob);
    }
    public void setStatus(Long jobId, boolean active) {
        Optional<Job> job = jobRepo.findById(jobId);
        if (job.isPresent()) {
            Job job1 = job.get();
            job1.setActive(active);
            jobRepo.save(job1);
        }
    }
    public void getUsersWithMatchingSkills(Long jobId) {
//        Job temp=new Job();
//        List<JobSkills> jobSkillsList=new ArrayList<>();
//        Skills skills=skillsRepo.findById(1l).get();
//        temp.setJobSkillsList(jobSkillsList);
//        Job job=new Job();
//        JobSkills jobSkills=new JobSkills();
//        jobSkills.setJob(job);
//        jobSkills.setJobSkill(skills);
//        jobSkillsList.add(jobSkills);
//        job.setJobSkillsList(jobSkillsList);
//        jobRepo.save(job);
//
//
//        jobSkillsRepo.save(jobSkills);

        Job job=jobRepo.findById(1l).get();
        List<JobSkills> requiredSkills=job.getJobSkillsList();
        Set<UserSkills> userSet=new HashSet<>();
        requiredSkills.forEach(jobSkillss ->{
                  userSet.addAll(userSkillsRepo.findBySkills(jobSkillss.getJobSkill()));
        });
        Set<Users> usersSet=new HashSet<>();
        userSet.forEach(userSkills -> usersSet.add(userSkills.getUser()));
        System.out.println(usersSet);
    }

    private void sendEmailNotifications(List<Users> users, Job job) {
        for (Users users1 : users) {
            String emailContent = "Dear " + users1.getFullName() + ",\n"
                    + "A new job matching your skills has been posted.\n"
                    + "Job Title: " + job.getJobTitle() + "\n"
                    + "Job Description: " + job.getJobDesc() + "\n"
                    + "Please consider applying if you are interested.\n"
                    + "Best regards,\n"
                    + "Your Job Portal Team";
            // Send email to the user
            // ...
        }
    }



}
