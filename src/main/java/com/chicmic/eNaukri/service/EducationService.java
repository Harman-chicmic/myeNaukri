package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UserEducationDto;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.UserEducation;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.EducationRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class EducationService {
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    EducationRepo educationRepo;
    public void addEducation(UserEducationDto dto,Long userId){
        Users user= usersRepo.findByUserId(userId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Education education = mapper.convertValue(dto, Education.class);
        UserEducation userEducation=new UserEducation();
        userEducation.setEducation(education);
        userEducation.setUser(user);
        educationRepo.save(education);
    }
}
