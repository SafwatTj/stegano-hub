package com.steganohub.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return new User("admin", "$2a$10$CZ8A7T7g5gqjvlQjQT8KH.OiyYtlr3T18/YFj7zLumJuk8r1cinKC", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Benutzer nicht gefunden: " + username);
        }
    }
}