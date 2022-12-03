package com.example.springboot.configuration;

import com.example.springboot.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    public static final Logger _Logger = LogManager.getLogger(JwtTokenUtil.class);
    private final String JWT_SECRET = "abcdef";
    private final long JWT_EXPIRATION = 604800000L;

    /*
     * @param userDetail
     * Generate jwt from user detail
     *
     */
    public String generateToken(UserDetails userDetail) {
        String jwt = "";
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
            jwt = Jwts.builder()
                    .setSubject(userDetail.getUsername())
                    .setIssuedAt(now).setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return jwt;
    }

    public boolean validate(String authToken) {
        boolean isValid = false;
        try {
            if (authToken == null || authToken.isEmpty()) return false;
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            isValid = true;
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return isValid;
    }

    /**
     * @param token get userName from jwt
     */
    public String getUsername(String token) {
        String userName = null;
        try {
            Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
            userName = claims.getSubject();
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return userName;
    }

    public static void main(String[] args) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        UserDetailsImpl user = new UserDetailsImpl();
        user.setUsername("admin");
        String token = jwtTokenUtil.generateToken(user);
        String sampleToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY2OTU2Nzc5OCwiZXhwIjoxNjcwMTcyNTk4fQ.Vhd49wbjnqQscPkQhzkP4yC7CqpbTAvlZyaiwz8S4AcQ2tbeCBOm4LZDJkzbxCNZLbqoqxjoZS6oXArM5t0c2Q";
        System.err.println(token);
    }
}
