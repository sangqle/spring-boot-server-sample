package com.example.springboot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

/**
 * RolesAllowed is base role for the rest controller
 */

@RestController
@RequestMapping("/api/test")
@RolesAllowed("ADMIN")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @RolesAllowed("USER")
    public String userAccess(Authentication authentication, Principal principal) {
        return "User Content.";
    }

    @GetMapping("/mod")
	@RolesAllowed("MOD")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@RolesAllowed("ADMIN")
	public String adminaccess() {
		return "admin board.";
	}
}
