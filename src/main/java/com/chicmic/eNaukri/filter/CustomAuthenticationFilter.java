package com.chicmic.eNaukri.filter;

import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserServiceImpl userService;
    public CustomAuthenticationFilter(UserServiceImpl userService, AuthenticationManager authenticationManager) {
        this.userService=userService;
        this.authenticationManager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    // sending username, password to authentication manager
        String username=request.getParameter("username").trim().toLowerCase();
        String password=request.getParameter("password").trim().toLowerCase();

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication
            (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

    // after successful login
        Collection<Authority> authorities=new ArrayList<>();
        authorities.add(new Authority("USER"));
        UUID uuid=UUID.randomUUID();

        Users loggedInUser=userService.getUserByEmail(authResult.getName());
        UserToken userToken=UserToken.builder().userr(loggedInUser).token(uuid.toString()).build();

    // saving uuid & updating cookies
        userService.saveUUID(userToken);

        String redirectUrl="";
        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("JSESSIONID".equals(cookie.getName())){
                    cookie.setValue(uuid.toString());
                    response.addCookie(cookie);
                }
                if("prevURL".equals(cookie.getName())){
                    redirectUrl=(!cookie.getValue().isEmpty())?cookie.getValue():"";
                }
            }
        }

        Cookie cookie=new Cookie("AuthHeader",uuid.toString());
        cookie.setMaxAge(60*60*24);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(loggedInUser,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

    //redirecting
        redirectUrl=(redirectUrl.trim().isEmpty())?"/login-page":redirectUrl;
        new DefaultRedirectStrategy().sendRedirect(request,response,redirectUrl);
    }

}
