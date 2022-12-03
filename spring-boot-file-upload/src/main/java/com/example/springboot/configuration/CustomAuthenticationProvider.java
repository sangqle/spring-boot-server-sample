package com.example.springboot.configuration;

import com.example.springboot.service.UserDetailsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    public static Logger _Logger = LogManager.getLogger(CustomAuthenticationProvider.class);

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() == null) ? "" : authentication.getName();
        final String password = (authentication.getCredentials() == null) ? "" : (String) authentication.getCredentials();

        if (username.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("invalid login details");
        }

        // get user details using Spring security user details service
        UserDetails user = null;
        try {
            user = userDetailsService.loadUserByUsername(username);
            if (user == null || !user.getPassword().equals(password)) {
                _Logger.error(String.format("Invalid credentials username: %s", username));
                return null;
            }
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return createSuccessfulAuthentication(authentication, user);
    }

    private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                authentication.getCredentials(),
                user.getAuthorities()
        );

        token.setDetails(user);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
