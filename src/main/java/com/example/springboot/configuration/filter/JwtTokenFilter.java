package com.example.springboot.configuration.filter;

import com.example.springboot.configuration.JwtTokenUtil;
import com.example.springboot.service.UserDetailsImpl;
import com.example.springboot.service.UserDetailsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.aspectj.util.LangUtil.isEmpty;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final Logger _Logger = LogManager.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            _Logger.error("Missing 'Authorization' field");
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            _Logger.error("Invalid token: " + token);
            chain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        // Hard code for testing without fetch from database
        String username = jwtTokenUtil.getUsername(token);
        if(!username.equals("admin")) {
            _Logger.error("Do not has permission");
           chain.doFilter(request, response);
           return;
        }

        // Set sample authentication object and hardcode for authorities
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        System.err.println(userDetails.getAuthorities());

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                null , userDetails, userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<GrantedAuthority> authorities1 = (List<GrantedAuthority>) authentication.getAuthorities();
        System.err.println(authorities1);
        chain.doFilter(request, response);
    }
}
