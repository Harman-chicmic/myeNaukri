package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UserEducationDto;
import com.chicmic.eNaukri.Dto.UserExperienceDto;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.Experience;
import com.chicmic.eNaukri.model.UserExperience;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class ExperienceService {
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    CompanyRepo companyRepo;
    public void addExperience(Long userId, UserExperienceDto dto){
        Users user=usersRepo.findByUserId(userId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Experience experience = mapper.convertValue(dto, Experience.class);
        experience.setExCompany(companyRepo.getById(dto.getCompanyId()));
        UserExperience userExperience=new UserExperience();
        userExperience.setExperience(experience);
        userExperience.setUser(user);
    }
}
