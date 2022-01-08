package com.example.backend.controller;

import com.example.backend.model.Activity;
import com.example.backend.service.ActivityService;
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
@RequestMapping("/activity")
public class ActivityController {

	private static final Logger LOGGER = Logger.getLogger(ActivityController.class.getName());
	private final ActivityService activityService;

	@Autowired
	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}

	@GetMapping("/allActivities")
	public String getAllActivities() {
		LOGGER.info("activity/allActivities endpoint was called");
		return activityService.getAllActivities();
	}

	@GetMapping("/getDistanceActivities/{latitude}/{longitude}/{distance}")
	public String getDistanceActivitiesForClient(@PathVariable double latitude, @PathVariable double longitude, @PathVariable int distance) {
		LOGGER.info("activity/getDistanceActivities/{latitude}/{longitude}/{distance} endpoint was called with parameters [" + latitude + ", " + longitude + ", " + distance + "]");
		return activityService.getDistanceActivitiesForClient(latitude, longitude, distance);
	}

	@GetMapping("/getDefaultActivitiesForUser/{username}")
	public String getDefaultActivitiesForUser(@PathVariable String username) {
		LOGGER.info("activity/getDefaultActivitiesForUser/{username} endpoint was called with parameter [" + username + "]");
		return activityService.getDefaultActivitiesForUser(username);
	}

	@GetMapping("/userEnrolled/{activityId}")
	public void updateActivityParticipants(@PathVariable int activityId) {
		LOGGER.info("activity/userEnrolled/{activityId} endpoint was called with parameter [" + activityId + "]");
		activityService.updateActivityParticipants(activityId);
	}

	@PostMapping("/insertActivity")
	public Activity insertActivityIntoDB(@RequestBody Activity activity) {
		LOGGER.info("activity/insertActivity endpoint was called with body");
		return activityService.insertActivityIntoDB(activity);
	}
}
