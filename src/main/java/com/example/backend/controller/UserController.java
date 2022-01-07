package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
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

	@PostMapping("/insertUser")
	public String insertUser(@RequestBody User user) {
		return userService.insertUser(user);
	}

	@GetMapping("/getUserByPVKey/{pvKey}")
	public String getUserByPVKey(@PathVariable String pvKey) {
		return userService.getUserByPVKey(pvKey);
	}

}
