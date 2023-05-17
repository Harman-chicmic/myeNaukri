package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Authority;
import com.chicmic.eNaukri.model.UserToken;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UsersRepo usersRepo;
    private final ApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final SkillsRepo skillsRepo;
    private final UserTokenRepo tokenRepo;

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
}
