package com.example.backend.service;

import com.example.backend.model.Activity;
import com.example.backend.model.User;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.FileService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

	private final UserRepository userRepository;
	private final ActivityRepository activityRepository;

	@Autowired
	public UserService(UserRepository userRepository, ActivityRepository activityRepository) {
		this.userRepository = userRepository;
		this.activityRepository = activityRepository;
	}

	private void createJSONUser(User user, JsonObject userJSON) {
		userJSON.addProperty("id", user.getId());
		userJSON.addProperty("full_name", user.getFull_name());
		userJSON.addProperty("username", user.getUsername());
		userJSON.addProperty("email", user.getEmail());
		userJSON.addProperty("password", user.getPassword());
		userJSON.addProperty("photo", user.getPhoto());
		userJSON.addProperty("isAdmin", user.getIsAdmin());
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
		Activity activity = userRepository.getActivityByPVKey(pvKey);
		if (user == null) {
			return "Cannot retrieve user from DB";
		} else {
			JsonObject userJSON = new JsonObject();
			createJSONUser(user, userJSON);
			userJSON.addProperty("activityName", activity.getName());
			return FileService.objectToJson(userJSON);
		}
	}

	public String getUserByUsername(String username) {

		User user = userRepository.getUserByUsername(username);
		JsonObject userJSON = new JsonObject();
		if (user != null) {
			createJSONUser(user, userJSON);
		} else {
			userJSON.addProperty("username", "N");
		}
		return FileService.objectToJson(userJSON);
	}

	public String getUserByEmail(String email) {

		User user = userRepository.getUserByEmail(email);
		JsonObject userJSON = new JsonObject();
		if (user != null) {
			createJSONUser(user, userJSON);
		} else {
			userJSON.addProperty("email", "N");
		}
		return FileService.objectToJson(userJSON);
	}

	public String generateEventCode(int userID, int activityID) {
		LocalDate date = LocalDate.now();
		int day = date.getDayOfMonth();
		int month = date.getMonthValue();
		int year = date.getYear();

		return String.format("GST%d%d%d%d%d", day, month, year, userID, activityID);
	}

	public String insertUser(User user) {

		userRepository.insertUser(user);
		return "User: [" + user.getFull_name() + "] with username: [" + user.getUsername() + "] inserted into DB!";
	}
}
