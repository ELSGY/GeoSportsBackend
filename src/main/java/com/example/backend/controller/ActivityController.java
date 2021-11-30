package com.example.backend.controller;

import com.example.backend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/activity")
public class ActivityController {

	private static final Logger LOGGER = Logger.getLogger(ActivityController.class.getName());

	private final ActivityService activityService;

	@Autowired
	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}

	@GetMapping
	public String getAllActivities(){
		return activityService.getAllActivities();
	}
}
