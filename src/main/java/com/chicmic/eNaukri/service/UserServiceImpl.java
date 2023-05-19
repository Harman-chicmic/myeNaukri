package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UsersRepo usersRepo;
    private final ApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final ExperienceRepo experienceRepo;
    private final SkillsRepo skillsRepo;
    private final CompanyRepo companyRepo;
    private final UserTokenRepo tokenRepo;
    @PersistenceContext
    private EntityManager entityManager;

    public void saveUUID(UserToken userToken) {
        tokenRepo.save(userToken);
    }

    public Users getUserByEmail(String username) {
        return usersRepo.findByEmail(username);
    }

    public Users findUserFromUUID(String token) {
        UserToken userToken= tokenRepo.findByToken(token);
        return usersRepo.findById(userToken.getUserr().getUserId()).get();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("AuthHeader".equals(cookie.getName())){
                    tokenRepo.deleteByToken(cookie.getValue());
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);break;
                }
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user= usersRepo.findByEmail(username);
//        if(user==null)throw new CustomApiExceptionHandler(HttpStatus.NOT_FOUND,username + "doesn't exists, please REGISTER.");

        Collection<Authority> authorites=new ArrayList<>();
        authorites.add(new Authority("USER"));

        return new User(user.getEmail(),user.getPassword(),authorites);
    }

    public String findCurrentCompany(Long id) {
        Users users=usersRepo.findById(id).get();
       return experienceRepo.findByExpUserAndCurrentlyWorking(users,true).getExCompany().getCompanyName();
    }





    public List<Job> displayFilteredPaginatedJobs(String query, String location, String jobType, String postedOn, String remoteHybridOnsite) {
        boolean flag=true;
        CriteriaBuilder cb=entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> criteriaQuery= cb.createQuery(Job.class);

        Root<Job> root=criteriaQuery.from(Job.class);

            if(!StringUtils.isEmpty(query)){
                criteriaQuery.where(cb.and( cb.like(root.get("jobTitle"),"%" +query+ "%"),cb.isTrue(root.get("active"))));
                criteriaQuery.where(cb.and( cb.like(root.get("jobDesc"),"%" +query+ "%"),cb.isTrue(root.get("active"))));
            }
            if(!StringUtils.isEmpty(location))criteriaQuery.where(cb.and( cb.like(root.get("location"),location),cb.isTrue(root.get("active"))));
            if(!StringUtils.isEmpty(postedOn))criteriaQuery.where(cb.and( cb.like(root.get("postedOn"),postedOn),cb.isTrue(root.get("active"))));
            if(!StringUtils.isEmpty(jobType))criteriaQuery.where(cb.and( cb.like(root.get("jobType"),jobType),cb.isTrue(root.get("active"))));
            if(!StringUtils.isEmpty(remoteHybridOnsite))criteriaQuery.where(cb.and( cb.like(root.get("remoteHybridOnsite"),remoteHybridOnsite),cb.isTrue(root.get("active"))));

    //pagination
        TypedQuery<Job> typedQuery=entityManager.createQuery(criteriaQuery.where(cb.isTrue(root.get("active"))));
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(5);

        return typedQuery.getResultList();

    }
    //    public List<Job> displayFilteredPaginatedJobs(String query, String location, String jobType, String postedOn, String remoteHybridOnsite) {
//
//
//        if(StringUtils.isEmpty(query)&&StringUtils.isEmpty(location)&&StringUtils.isEmpty(jobType)&&StringUtils.isEmpty(postedOn)&&StringUtils.isEmpty(remoteHybridOnsite)){
//            return jobRepo.findAll();
//        }
//        if(StringUtils.isEmpty(location))location="";
//        if(StringUtils.isEmpty(jobType))jobType="";
//        if(StringUtils.isEmpty(remoteHybridOnsite))remoteHybridOnsite="";
//
//        LocalDate currentDate=LocalDate.now();
//        LocalDate startDate=null;
//        if(!StringUtils.isEmpty(postedOn)){
//            switch (postedOn){
//                case "24hours":
//                    startDate=currentDate.minusDays(1);break;
//                case "thisWeek":
//                    startDate=currentDate.minusWeeks(1);break;
//                case "thisMonth":
//                    startDate=currentDate.minusMonths(1);break;
//                default:startDate=null;break;
//            }
//        }
//
//        if(StringUtils.isEmpty(query)){
//            if(startDate!=null){
//                return jobRepo.findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndPostedOnAfterAndActive(location,jobType,remoteHybridOnsite,startDate,true);
//            }
//            else return jobRepo.findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndActive(location,jobType,remoteHybridOnsite,true);
//        }
//        else{
//            if(startDate!=null){
//                return jobRepo.findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndPostedOnAfterAndJobTitleContainingIgnoreCaseAndActive(location,jobType,remoteHybridOnsite,startDate,query,true);
//            }
//            else return jobRepo.findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndJobTitleContainingIgnoreCaseAndActive(location,jobType,remoteHybridOnsite,query,true);
//
//        }
//    }
}
