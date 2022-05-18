package com.example.backend.service;

import com.example.backend.model.Activity;
import com.example.backend.model.ActivityRating;
import com.example.backend.model.User;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.FileService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ActivityService {

	private static final Logger LOGGER = Logger.getLogger(ActivityService.class.getName());

	private final ActivityRepository activityRepository;
	private final CategoriesService categoriesService;
	private final UserRepository userRepository;

	@Autowired
	public ActivityService(ActivityRepository activityRepository, CategoriesService categoriesService, UserRepository userRepository) {
		this.activityRepository = activityRepository;
		this.categoriesService = categoriesService;
		this.userRepository = userRepository;
	}

	public String getAllActivities() {

		// get activities from db
		Set<Activity> activitySet = activityRepository.getAllActivities();
		if (activitySet == null) {
			return "Could not get activities from DB";
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate todayDate = LocalDate.now();
		String localDateString = dtf.format(todayDate);
		Date today = null;

		try {
			today = sdf.parse(localDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JsonArray activityList = new JsonArray();

		Date finalToday = today;
		activitySet.forEach(activity -> {

			Date activityDate = null;
			try {
				activityDate = sdf.parse(activity.getDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (finalToday.before(activityDate))
				addToJSONArray(activityList, activity, 0);
		});

		return FileService.objectToJson(activityList);
	}

	public String getActivityByName(String name) {

		Activity activity = activityRepository.getActivityByName(name);
		if (activity == null) {
			return "Could not get activity from DB";
		}

		JsonObject activityJSON = getActivityJSON(activity);

		return FileService.objectToJson(activityJSON);
	}

	public String getActivityByNameForUser(String name, String username) {

		Activity activity = activityRepository.getActivityByName(name);
		if (activity == null) {
			return "Could not get activity from DB";
		}

		int userId = userRepository.getUserByUsername(username).getId();
		int check = activityRepository.checkUserToActivity(userId, activity.getId());

		JsonObject activityJSON = getActivityJSON(activity);
		activityJSON.addProperty("enrolledCheck", check);

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
		activityJSON.addProperty("photo", activity.getPhoto());
		return activityJSON;
	}

	public Activity insertActivityIntoDB(Activity activity) {
		return activityRepository.insertActivityIntoDB(activity);
	}

	public void updateActivityRating(String username, int activityId, int rating) {
		ActivityRating ar = new ActivityRating(activityId, userRepository.getUserByUsername(username).getId(), rating);
		if (activityRepository.ratingGiven(ar)) {
			activityRepository.updateActivityRatingForUser(ar);
		} else {
			activityRepository.insertActivityRatingIntoDB(ar);
		}
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

	public String getDistanceActivitiesForClient(double lat, double lng, int distance) {

		Set<Activity> activities = activityRepository.getAllActivities();
		if (activities.isEmpty()) {
			return "Could not get activities from DB";
		}

		JsonArray activityList = new JsonArray();

		activities.forEach(activity -> {
			if (getDistanceBetweenTwoActivities(lat, lng, activity.getLatitude(), activity.getLongitude()) <= distance) {
				addToJSONArray(activityList, activity, 0);
			}
		});
		return FileService.objectToJson(activityList);
	}

	public String getUnenrolledActivitiesForUser(String username) {

		User user = userRepository.getUserByUsername(username);
		if (user == null) {
			return "Could not get user from DB";
		}
		int userId = user.getId();

		LOGGER.info("User retrieved from DB: [" + user.getFull_name() + "]");

		Set<Activity> allActivities = activityRepository.getUnenrolledActivitiesForUser(userId);
		if (allActivities.isEmpty()) {
			return "Could not get activities from DB";
		}
		LOGGER.info(".getUnenrolledActivitiesForUser");


		//		enrolledActivities.forEach(activity -> {
		//			LOGGER.info("Distanta fata de " + activity.getName() + ": " + getDistanceBetweenTwoActivities(activity.getLatitude(), activity.getLongitude(), latitude, longitude));
		//			if (getDistanceBetweenTwoActivities(activity.getLatitude(), activity.getLongitude(), latitude, longitude) <= 100) {
		//				addToJSONArray(activityList, activity);
		//			}
		//		});

		JsonArray activityList = filterActiveEvents(allActivities);

		return FileService.objectToJson(activityList);
	}

	public String getEnrolledActivitiesForUser(String username) {

		User user = userRepository.getUserByUsername(username);
		if (user == null) {
			return "Could not get user from DB";
		}
		int userId = user.getId();

		LOGGER.info("User retrieved from DB: [" + user.getFull_name() + "]");

		Set<Activity> allActivities = activityRepository.getEnrolledActivitiesForUser(userId);
		if (allActivities.isEmpty()) {
			return "Could not get activities from DB";
		}
		LOGGER.info(".getEnrolledActivitiesForUser");


		//		enrolledActivities.forEach(activity -> {
		//			LOGGER.info("Distanta fata de " + activity.getName() + ": " + getDistanceBetweenTwoActivities(activity.getLatitude(), activity.getLongitude(), latitude, longitude));
		//			if (getDistanceBetweenTwoActivities(activity.getLatitude(), activity.getLongitude(), latitude, longitude) <= 100) {
		//				addToJSONArray(activityList, activity);
		//			}
		//		});

		JsonArray activityList = filterActiveEvents(allActivities);

		return FileService.objectToJson(activityList);
	}

	public String getEnrolledPastActivitiesForUser(String username) {

		User user = userRepository.getUserByUsername(username);
		if (user == null) {
			return "Could not get user from DB";
		}
		int userId = user.getId();

		LOGGER.info("User retrieved from DB: [" + user.getFull_name() + "]");

		Set<Activity> allActivities = activityRepository.getEnrolledActivitiesForUser(userId);
		if (allActivities.isEmpty()) {
			return "Could not get activities from DB";
		}
		LOGGER.info(".getEnrolledPastActivitiesForUser");

		JsonArray activityList = filterPastEvents(allActivities, userId);

		return FileService.objectToJson(activityList);
	}

	public String getTopActivities() {
		Set<Activity> allActivities = activityRepository.getAllActivities();
		if (allActivities == null) {
			return "Could not get activities from DB";
		}
		LOGGER.info(".getTopActivities");

		JsonArray activityList = filterCurrentMonth(allActivities);

		return FileService.objectToJson(activityList);
	}

	private JsonArray filterCurrentMonth(Set<Activity> allActivities) {
		// get past activities in current month
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String today = formatter.format(date);

		Date firstDayOfTheMonthDate = new Date(date.getYear(), date.getMonth(), 1);
		String firstDayOfTheMonth = formatter.format(firstDayOfTheMonthDate);

		JsonArray activityList = new JsonArray();
		allActivities.forEach(activity -> {
			try {
				if ((formatter.parse(today).after(formatter.parse(activity.getDate()))) &&
					(formatter.parse(activity.getDate()).after(formatter.parse(firstDayOfTheMonth)))) {
					ActivityRating actRating = activityRepository.getActivityTopRating(activity.getId());
					int rating;
					if (actRating != null) {
						rating = actRating.getRating() / actRating.getUserId();
						addToJSONArray(activityList, activity, rating);
					}
					addToJSONArray(activityList, activity, 0);

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return activityList;
	}

	private JsonArray filterActiveEvents(Set<Activity> allActivities) {
		// get just activities that are still active
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String today = formatter.format(date);

		JsonArray activityList = new JsonArray();
		allActivities.forEach(activity -> {
			try {
				if (formatter.parse(today).before(formatter.parse(activity.getDate()))) {
					addToJSONArray(activityList, activity, 0);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return activityList;
	}

	private JsonArray filterPastEvents(Set<Activity> allActivities, int userId) {
		// get just activities that are still active
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String today = formatter.format(date);

		JsonArray activityList = new JsonArray();
		allActivities.forEach(activity -> {
			try {
				if (formatter.parse(today).after(formatter.parse(activity.getDate()))) {
					ActivityRating ar = new ActivityRating(activity.getId(), userId, 0);
					if (activityRepository.ratingGiven(ar)) {
						int rating = activityRepository.getActivityRatingForUser(activity.getId(), userId);
						addToJSONArray(activityList, activity, rating);
					} else {
						addToJSONArray(activityList, activity, 0);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return activityList;
	}

	private void addToJSONArray(JsonArray activityList, Activity activity, int rating) {
		JsonObject activityJSON = getActivityJSON(activity);
		activityJSON.addProperty("rating", rating);
		activityList.add(activityJSON);
	}

	public void decreaseActivityParticipants(String activityName) {
		activityRepository.decreaseActivityParticipants(activityName);
	}

	public void increaseActivityParticipants(String activityName) {
		activityRepository.increaseActivityParticipants(activityName);
	}

	public void insertActivityTicket(int userId, int activityId, String code) {
		activityRepository.insertActivityTicket(userId, activityId, code);
	}

	public void deleteUserTicketForActivity(int userId, int activityId) {
		activityRepository.deleteUserTicketForActivity(userId, activityId);
	}

	public void updateActivity(String name, String date, String time, int avbPlaces, int id) {
		activityRepository.updateActivity(name, date, time, avbPlaces, id);
	}
}
