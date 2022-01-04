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

import java.text.ParseException;
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
		return activityService.getAllActivities();
	}

	@GetMapping("/getDefaultActivities/{latitude}/{longitude}/{distance}")
	public String getDefaultActivitiesForClient(@PathVariable double latitude, @PathVariable double longitude, @PathVariable int distance) {
		return activityService.getDefaultActivitiesForClient(latitude, longitude, distance);
	}

	@GetMapping("/userEnrolled/{activityId}")
	public void updateActivityParticipants(@PathVariable int activityId) {
		activityService.updateActivityParticipants(activityId);
	}

	@PostMapping("/insertActivity")
	public Activity insertActivityIntoDB(@RequestBody Activity activity) {
		LOGGER.info(activity.toString());
		return activityService.insertActivityIntoDB(activity);
	}
}
