package com.chicmic.eNaukri.filter;

//import com.example.JobPortal.Services.UsersService;
import com.chicmic.eNaukri.Services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final UsersService userService;


    public CustomAuthorizationFilter(UsersService userService) {
        this.userService=userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //System.out.println("Authorization Filter gets Called!!!!");
//        System.out.println("\u001B[3532m"  + request.getServletPath()+  "\u001B[0m");
//        if(request.getServletPath().equals("/login")||request.getServletPath().equals("/hello")||request.getServletPath().equals("/")
//                ||request.getServletPath().equals("/signup")||request.getServletPath().equals("/go/auth"))
//        {
//            System.out.println("llll");
//            filterChain.doFilter(request,response);
//        }
//        else {
//            System.out.println("oooooo");
//            String AuthorizationHeader = request.getHeader("Authorization");
//            LoginToken uuid= userService.getToken(AuthorizationHeader.substring(7));
//            boolean flag = true;
////            if(request.getServletPath().equals("/add/product")){
////                if(!uuid.getEmail().equals("Admin@chicmic")){
////                    uuid=null;
////                    flag=false;
////                }
////            }
//            //if auth head is null or invalid
//            if(AuthorizationHeader==null||uuid==null){
//                Map<String,String> error=new HashMap<>();
//                if(flag) error.put("error_message","Please provide valid token");
//                else error.put("error_message", "You are not authorized to add products");
////             token.put("refresh_token",refresh_token);
//
//                response.setContentType(APPLICATION_JSON_VALUE);
//
//                new ObjectMapper().writeValue(response.getOutputStream(),error);
//            }
//            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//            Users user = userService.getUser(uuid.getEmail());
//            System.out.println(user);
//            System.out.println(authorities);
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                    new UsernamePasswordAuthenticationToken(uuid.getEmail(), null, authorities);
//
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);
        }
    }


