package com.example.springboot.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * This service to integrate with your data source
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get user from your database and check login here
        if(username.equals("admin")) {
            return new UserDetailsImpl(1, "admin","admin@gmail.com", "adminpass", null);
        }
        return null;
    }
}
