package com.example.backend.controller;

import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/getAllUsers")
	public String getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/getUserById/{id}")
	public String getUserById(@PathVariable int id) {
		return userService.getUserById(id);
	}

	@GetMapping("/getUserByName/{name}")
	public String getUserByName(@PathVariable String name) {
		return userService.getUserByName(name);
	}

	@GetMapping("/generateEventCode")
	public String generateEventCode() {
		return userService.generateEventCode(1);
	}

}
