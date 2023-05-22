package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.repo.SkillsRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class RecommendService {

    UsersRepo usersRepo;

    SkillsRepo skillsRepo;

}
