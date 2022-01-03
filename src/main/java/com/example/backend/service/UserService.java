package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.FileService;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

@Service
public class UserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	private void createJSONUser(User user, JsonObject userJSON) {
		userJSON.addProperty("id", user.getId());
		userJSON.addProperty("fullName", user.getFullName());
		userJSON.addProperty("username", user.getUsername());
		userJSON.addProperty("email", user.getEmail());
	}

	public String getAllUsers() {

		Set<User> usersList = userRepository.getUsers();

		JsonArray jsonArray = new JsonArray();

		usersList.forEach(user -> {
			JsonObject userJSON = new JsonObject();
			createJSONUser(user, userJSON);
			jsonArray.add(userJSON);
		});

		return FileService.objectToJson(jsonArray);
	}

	public String getUserById(int id) {

		User user = userRepository.getUserById(id);

		JsonObject userJSON = new JsonObject();
		createJSONUser(user, userJSON);

		return FileService.objectToJson(userJSON);
	}

	public String getUserByPVKey(String pvKey) {

		User user = userRepository.getUserByPVKey(pvKey);

		JsonObject userJSON = new JsonObject();
		createJSONUser(user, userJSON);

		return FileService.objectToJson(userJSON);
	}

	public String getUserByName(String name) {

		User user = userRepository.getUserByName(name);

		JsonObject userJSON = new JsonObject();
		createJSONUser(user, userJSON);

		return FileService.objectToJson(userJSON);
	}

	public String generateEventCode(int userID, int activityID) {

		LocalDate date = LocalDate.now();
		int day = date.getDayOfMonth();
		int month = date.getMonthValue();
		int year = date.getYear();

		return String.format("GST%d%d%d%d%d", day, month, year, userID, activityID);

	}
}
