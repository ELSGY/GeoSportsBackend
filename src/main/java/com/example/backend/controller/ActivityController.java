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
		LOGGER.info("activity/getEnrolledActivitiesForUser/{username} endpoint was called with parameter [" + username + "]");
		return activityService.getEnrolledActivitiesForUser(username);
	}

	@GetMapping("/getEnrolledPastActivitiesForUser/{username}")
	public String getEnrolledPastActivitiesForUser(@PathVariable String username) {
		LOGGER.info("activity/getEnrolledPastActivitiesForUser/{username} endpoint was called with parameter [" + username + "]");
		return activityService.getEnrolledPastActivitiesForUser(username);
	}

	@GetMapping("/getTopActivities")
	public String getTopActivities() {
		LOGGER.info("activity/getTopActivities endpoint was called");
		return activityService.getTopActivities();
	}

	@GetMapping("/userEnrolled/{activityName}")
	public String updateActivityParticipants(@PathVariable String activityName) {
		LOGGER.info("activity/userEnrolled/{activityId} endpoint was called with parameter [" + activityName + "]");
		activityService.decreaseActivityParticipants(activityName);
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

	@GetMapping("/updateActivity/{name}/{address}/{date}/{time}/{avbPlaces}/{id}/{lat}/{lng}")
	public String updateActivity(@PathVariable String name, @PathVariable String address, @PathVariable String date, @PathVariable String time, @PathVariable int avbPlaces, @PathVariable int id, @PathVariable double lat, @PathVariable double lng) {
		LOGGER.info("activity/updateActivity/{name}/{address}/{time}/{date}/{avbPlaces}/{id}/{lat}/{lng} endpoint was called with body");
		activityService.updateActivity(name, address, date, time, avbPlaces, id, lat, lng);
		return "Activity: [" + name + "] was updated";
	}

	@GetMapping("/updateActivityRating/{username}/{activityId}/{rating}")
	public String updateActivityRating(@PathVariable String username, @PathVariable int activityId, @PathVariable int rating) {
		LOGGER.info("activity/updateActivityRating endpoint was called with body");
		activityService.updateActivityRating(username, activityId, rating);
		return "Activity rating : [" + activityId + ", " + username + ", " + rating + "] was updated";
	}

	@GetMapping("/deleteActivity/{activityId}")
	public String deleteActivity(@PathVariable int activityId) {
		LOGGER.info("/deleteActivity/{activityId} endpoint was called with parameter [" + activityId + "]");
		activityService.deleteActivity(activityId);
		return "Activity rating : [" + activityId + "] was deleted";
	}
}
