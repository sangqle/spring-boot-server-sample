package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RolesAllowed is base role for the rest controller
 */

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "All content here";
    }

    @PostMapping("/user")
    public String createUser() {
        return "This is post method to create user";
    }
    @GetMapping("/mod")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	public String adminaccess() {
		return "admin board.";
	}

}
