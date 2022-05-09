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

import java.util.logging.Logger;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/getAllUsers")
	public String getAllUsers() {
		LOGGER.info("user/getAllUsers endpoint was called");
		return userService.getAllUsers();
	}

	@GetMapping("/getUserByUsername/{username}")
	public String getUserByUsername(@PathVariable String username) {
		LOGGER.info("user/getUserByUsername/{username} endpoint was called with parameters [" + username + "]");
		return userService.getUserByUsername(username);
	}

	@GetMapping("/getUserByEmail/{email}")
	public String getUserByEmail(@PathVariable String email) {
		LOGGER.info("user/getUserByEmail/{email} endpoint was called with parameters [" + email + "]");
		return userService.getUserByEmail(email);
	}

	@GetMapping("/getUserById/{id}")
	public String getUserById(@PathVariable int id) {
		LOGGER.info("user/getUserById/{id} endpoint was called with parameters [" + id + "]");
		return userService.getUserById(id);
	}

	@PostMapping("/insertUser")
	public String insertUser(@RequestBody User user) {
		LOGGER.info("user/insertUser endpoint was called");
		return userService.insertUser(user);
	}

	@GetMapping("/getUserByPVKey/{pvKey}")
	public String getUserByPVKey(@PathVariable String pvKey) {
		LOGGER.info("user/getUserByPVKey/{pvKey} endpoint was called with parameters [" + pvKey + "]");
		return userService.getUserByPVKey(pvKey);
	}

}
