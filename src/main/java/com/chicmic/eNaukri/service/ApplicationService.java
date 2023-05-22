package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Experience;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.ApplicationRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service public class ApplicationService {
    @Value("${my.cvPath.String}")
    String resumePath;
    @Autowired
    ApplicationRepo applicationRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    JobRepo jobRepo;
    public void applyForJob(Application application,MultipartFile resumeFile,Long userId,Long jobId)throws IOException {
       Users user = usersRepo.findByUserId(userId);
       Job job=jobRepo.findJobByJobId(jobId);
       ObjectMapper mapper = new ObjectMapper();
       mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
       Application jobApplication = mapper.convertValue(application, Application.class);
        if(!resumeFile.isEmpty()){
            String resumeFolder=resumePath;
            byte[] resumeFileBytes= resumeFile.getBytes();
            Path resumePath= Paths.get(resumeFolder+resumeFile.getOriginalFilename());
            Files.write(resumePath,resumeFileBytes);
            String cvPath="/static/assets/files" +resumeFile.getOriginalFilename();
            jobApplication.setCvPath(cvPath);
        }
        applicationRepo.save(jobApplication);
    }
    public List<Application> viewApplications(Long userId){
        Users user = usersRepo.findById(userId).get();
        List<Application> applications=user.getApplicationList();
        return applications;
    }
}
