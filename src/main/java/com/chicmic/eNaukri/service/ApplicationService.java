package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Application;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.ApplicationRepo;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    @Value("${my.cvPath.string}")
    String resumePath;
    ApplicationRepo applicationRepo;
    UsersRepo usersRepo;
    @Autowired JobRepo jobRepo;
    JavaMailSender javaMailSender;
    CompanyRepo companyRepo;
    @Async
    public void applyForJob(Application application, MultipartFile resumeFile, Long userId, Long jobId)
            throws IOException,MessagingException{
       Users user = usersRepo.findByUserId(userId);
       Job job=jobRepo.findJobByJobId(jobId);
       if(job.isActive()==true){
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
        job.setNumApplicants(job.getNumApplicants()+1);
        jobRepo.save(job);
        String jobTitle= job.getJobTitle();
        String company=job.getPostFor().getCompanyName();
        Company company1=companyRepo.findByCompanyName(company);
        sendEmailOnApplication(user.getEmail(),jobTitle,company);
//        sendEmailOnApplication(company1.);
       }
    }
    public void sendEmailOnApplication(String recipientEmail, String jobTitle,String company)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("bluflame.business@gmail.com", "eNaukri Site");
        helper.setTo(recipientEmail);
        String subject = "Your application has been submitted";
        String content = "<p>Hello,</p>"
                + "<p>Your application to the"+ company +"for" +jobTitle+ "has been submitted.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
        System.out.println("email sent");
    }
    public List<Application> viewApplications(Long userId){
        Users user = usersRepo.findById(userId).get();
        List<Application> applications=user.getApplicationList();
        return applications;
    }
    public int getNumApplicantsForJob(Long jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        return job.getNumApplicants();
    }
}
