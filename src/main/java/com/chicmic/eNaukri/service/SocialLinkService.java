package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.SocialLink;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.SocialLinkRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLinkService {
    SocialLinkRepo socialLinkRepo;
    UsersRepo usersRepo;
    CompanyRepo companyRepo;
    public void addSocialLinks(Long userId, SocialLink dto,Long companyId){
        if(userId!=null&&companyId==null){
            Users user= usersRepo.findById(userId).get();
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            SocialLink socialLink = mapper.convertValue(dto, SocialLink.class);
            socialLink.setUserLinks(user);
            socialLinkRepo.save(socialLink);
        }
        if (userId==null&&companyId!=null){
            Company company=companyRepo.findById(companyId).get();
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            SocialLink socialLink = mapper.convertValue(dto, SocialLink.class);
            socialLink.setCompanyLinks(company);
            socialLinkRepo.save(socialLink);
        }
    }
}
