package com.chicmic.eNaukri.Services;

import com.chicmic.eNaukri.model.Users;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsersService userService;

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UsersService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userService.getUser(email);
        if (email==null || user==null) {
            throw new UsernameNotFoundException("Username not found");
        }
        System.out.println(user+"///");
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        return new User(user.getEmail(),user.getPassword(), authorities);
    }
}
