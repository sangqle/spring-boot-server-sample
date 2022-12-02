package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * RolesAllowed is base role for the rest controller
 */

@RestController
@RequestMapping("/app")
public class AppControllers {
    @GetMapping("/hello")
    public String getHell() {
        return "Hello";
    }
   
}
