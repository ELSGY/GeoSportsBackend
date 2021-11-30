package com.example.backend.service;

import com.example.backend.model.Activity;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.utils.FileService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.logging.Logger;

@Service
public class ActivityService {

	private static final Logger LOGGER = Logger.getLogger(ActivityService.class.getName());

	private final ActivityRepository activityRepository;
	private final CategoriesService categoriesService;

	@Autowired
	public ActivityService(ActivityRepository activityRepository, CategoriesService categoriesService) {
		this.activityRepository = activityRepository;
		this.categoriesService = categoriesService;
	}

	public String getAllActivities() {

		Set<Activity> activitySet = activityRepository.getAllActivities();

		JsonArray activityList = new JsonArray();

		activitySet.forEach(activity -> {

			JsonObject activityJSON = new JsonObject();
			activityJSON.addProperty("id", activity.getId());
			activityJSON.addProperty("name", activity.getName());
			activityJSON.addProperty("latitude", activity.getLatitude());
			activityJSON.addProperty("longitude", activity.getLongitude());
			activityJSON.addProperty("avbPlaces", activity.getAvbPlaces());
			activityJSON.addProperty("category", categoriesService.getCategoryById(activity.getIdCat()));
			activityJSON.addProperty("subcategory", categoriesService.getSubcategoryById(activity.getIdSubcat()));
			activityJSON.addProperty("address", activity.getAddress());
			activityJSON.addProperty("date", activity.getDate());
			activityJSON.addProperty("time", activity.getTime());

			activityList.add(activityJSON);

		});

		return FileService.objectToJson(activityList);
	}

}
