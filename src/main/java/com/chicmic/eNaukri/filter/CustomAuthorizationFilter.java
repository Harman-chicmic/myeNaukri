package com.chicmic.eNaukri.filter;

import com.chicmic.eNaukri.model.Authority;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private UserServiceImpl userService;
    
    public  CustomAuthorizationFilter(UserServiceImpl userService){
        this.userService=userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getServletPath());

        if(request.getServletPath().contains("/user/")){

            String token= null;
            if (request.getCookies() != null) {
                token= Arrays.stream(request.getCookies())
                        .filter(c->c.getName().equals("AuthHeader")).findFirst()
                        .map(Cookie::getValue).orElse(null);
            }

            if(token==null || userService.findUserFromUUID(token)==null){

                Cookie prevUrl=new Cookie("prevURL", request.getServletPath());
                prevUrl.setHttpOnly(true);
                prevUrl.setMaxAge(60*10);
                response.addCookie(prevUrl);

                String redirectUrl="/login/user";
                new DefaultRedirectStrategy().sendRedirect(request,response,redirectUrl);
//                throw new CustomApiExceptionHandler(HttpStatus.BAD_REQUEST,"Please give Valid Token id");

            }
            else {
                if(request.getCookies()!=null){
                    for(Cookie cookie:request.getCookies()){
                        if("prevURL".equals(cookie.getName())){
                            cookie.setValue(null);
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);break;
                        }
                    }
                }

                Users temp= userService.findUserFromUUID(token.toLowerCase());
                Collection<Authority> authorities=new ArrayList<>();
                 authorities.add(new Authority("USER"));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                        new UsernamePasswordAuthenticationToken(temp,null,authorities);

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request,response);
            }
        }
        else filterChain.doFilter(request,response);
    }
}
