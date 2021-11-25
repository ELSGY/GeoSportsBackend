package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String getAllUsers() {

		Set<User> usersList = userRepository.getUsers();

		LOGGER.info(String.valueOf(usersList));

		return "";
	}
}
