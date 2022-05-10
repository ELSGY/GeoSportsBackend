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

	@GetMapping("/getUnenrolledActivitiesForUser/{username}")
	public String getUnenrolledActivitiesForUser(@PathVariable String username) {
		LOGGER.info("activity/getUnenrolledActivitiesForUser/{username} endpoint was called with parameter [" + username + "]");
		return activityService.getUnenrolledActivitiesForUser(username);
	}

	@GetMapping("/getEnrolledActivitiesForUser/{username}")
	public String getEnrolledActivitiesForUser(@PathVariable String username) {
		LOGGER.info("activity/getUnenrolledActivitiesForUser/{username} endpoint was called with parameter [" + username + "]");
		return activityService.getEnrolledActivitiesForUser(username);
	}

	@GetMapping("/userEnrolled/{activityName}")
	public String updateActivityParticipants(@PathVariable String activityName) {
		LOGGER.info("activity/userEnrolled/{activityId} endpoint was called with parameter [" + activityName + "]");
		activityService.updateActivityParticipants(activityName);
		return "Available places for " + activityName + " were updated!";
	}

	@GetMapping("/getActivityByName/{activityName}")
	public String getActivityByName(@PathVariable String activityName) {
		LOGGER.info("getActivityByName/{activityName} endpoint was called with parameter [" + activityName + "]");
		return activityService.getActivityByName(activityName);
	}

	@GetMapping("/getActivityByNameForUser/{activityName}/{username}")
	public String getActivityByNameForUser(@PathVariable String activityName, @PathVariable String username) {
		LOGGER.info("getActivityByNameForUser/{activityName}/{username} endpoint was called with parameters [" + activityName + "]");
		return activityService.getActivityByNameForUser(activityName, username);
	}

	@PostMapping("/insertActivity")
	public Activity insertActivityIntoDB(@RequestBody Activity activity) {
		LOGGER.info("activity/insertActivity endpoint was called with body");
		return activityService.insertActivityIntoDB(activity);
	}

	@GetMapping("/updateActivity/{name}/{date}/{time}/{avbPlaces}/{id}")
	public String updateActivity(@PathVariable String name, @PathVariable String date, @PathVariable String time, @PathVariable int avbPlaces, @PathVariable int id) {
		LOGGER.info("activity/updateActivity/{name}/{time}/{date}/{avbPlaces}/{id} endpoint was called with body");
		activityService.updateActivity(name, date, time, avbPlaces, id);
		return "Activity: [" + name + "] was updated";
	}
}
