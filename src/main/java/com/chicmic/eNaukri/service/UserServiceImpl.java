package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.UserToken;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UsersRepo usersRepo;
    private final ApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final SkillsRepo skillsRepo;
    private final UserCompanyRepo userCompanyRepo;
    private final UserTokenRepo tokenRepo;

    public void saveUUID(UserToken userToken) {
        tokenRepo.save(userToken);
    }

    public Users getUserByEmail(String username) {
        return usersRepo.findByEmail(username);
    }

    public Users findUserFromUUID(String token) {
        UserToken userToken= tokenRepo.findByUuid(token);
        return usersRepo.findById(userToken.getUserr().getUserId()).get();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("AuthHeader".equals(cookie.getName())){
                    tokenRepo.deleteByUuid(cookie.getValue());
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);break;
                }
            }
        }
    }
}
