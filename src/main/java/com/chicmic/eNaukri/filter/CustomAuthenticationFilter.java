package com.chicmic.eNaukri.filter;

import com.chicmic.eNaukri.Services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UsersService userService;
    private final AuthenticationManager authenticationManager; //manager provider

    public CustomAuthenticationFilter(UsersService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager= authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        System.out.println("attempt auth");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.getParameter("email"),request.getParameter("password"));
        System.out.println(usernamePasswordAuthenticationToken+"///");
        // return usernamePasswordAuthenticationToken;
        //authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //.authenticate(new UsernamePasswordAuthenticationToken(username, password))`
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println(authResult+"hey");

        System.out.println("User Authenticated Successfully!!!");
//        UUID uuid=UUID.randomUUID();
//        LoginToken uuidEntity = new LoginToken();
//        uuidEntity.setLoginToken(uuid.toString());
//
//        String email = userService.getUser(request.getParameter("email")).getEmail();
//        uuidEntity.setEmail(email);
//
//        int id = userService.getUser(request.getParameter("email")).getId();
//        uuidEntity.setId(id);
//        userService.CreateToken(uuidEntity);
//        new ObjectMapper().writeValue(response.getOutputStream(),uuid.toString());
        // userService.saveUser(user1);
    }
}

