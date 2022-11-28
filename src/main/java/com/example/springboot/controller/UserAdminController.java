package com.example.springboot.controller;

import com.example.springboot.configuration.CustomAuthenticationProvider;
import com.example.springboot.configuration.JwtTokenUtil;
import com.example.springboot.configuration.filter.JwtTokenFilter;
import com.example.springboot.dto.LoginRequest;
import com.example.springboot.enums.ApiMessageCode;
import com.example.springboot.model.ApiMessageResponse;
import com.example.springboot.service.UserDetailsImpl;
import com.example.springboot.service.UserDetailsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserAdminController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public static final Logger _Logger = LogManager.getLogger(UserAdminController.class);

    @PostMapping("/login")
    public ResponseEntity<ApiMessageResponse> login(@RequestBody LoginRequest loginRequest) {
        ApiMessageResponse messageResponse = new ApiMessageResponse(ApiMessageCode.INVALID_DATA);
       try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

           Authentication authenticate = customAuthenticationProvider
                   .authenticate(new UsernamePasswordAuthenticationToken(username, password));

           if(authenticate == null) {
               return ResponseEntity.status(403).body(messageResponse);
           }
           UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getDetails();
           String jwt = jwtTokenUtil.generateToken(userDetails);
           messageResponse = new ApiMessageResponse(ApiMessageCode.SUCCESS);
           messageResponse.setData(jwt);
       } catch (Exception ex) {
           _Logger.error(ex.getMessage(), ex);
       }
       return ResponseEntity.ok().body(messageResponse);
   }
}
