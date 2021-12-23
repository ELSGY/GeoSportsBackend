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
			addToJSONArray(activityList, activity);
		});

		return FileService.objectToJson(activityList);
	}

	public String getActivityByName(String name) {

		Activity activity = activityRepository.getActivityByName(name);

		JsonObject activityJSON = getActivityJSON(activity);

		return FileService.objectToJson(activityJSON);
	}

	private JsonObject getActivityJSON(Activity activity) {
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
		return activityJSON;
	}

	public Activity insertActivityIntoDB(Activity activity) {
		return activityRepository.insertActivityIntoDB(activity);
	}

	public double getDistanceBetweenTwoActivities(double lat1, double lon1, double lat2, double lon2) {
		int R = 6371; // Radius of the earth in km
		double dLat = Math.toRadians(lat2 - lat1);  // radians
		double dLon = Math.toRadians(lon2 - lon1);  // radians
		double a =
				Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	public String getDefaultActivitiesForClient(double lat, double lng) {

		Set<Activity> activities = activityRepository.getAllActivities();

		JsonArray activityList = new JsonArray();

		activities.forEach(activity -> {

			if (getDistanceBetweenTwoActivities(lat, lng, activity.getLatitude(), activity.getLongitude()) >= 100) {
				addToJSONArray(activityList, activity);
			}
		});
		return FileService.objectToJson(activityList);
	}

	private void addToJSONArray(JsonArray activityList, Activity activity) {
		JsonObject activityJSON = getActivityJSON(activity);

		activityList.add(activityJSON);
	}
}
