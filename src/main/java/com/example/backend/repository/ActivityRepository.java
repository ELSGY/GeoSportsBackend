package com.example.backend.repository;

import com.example.backend.model.Activity;
import com.example.backend.model.ActivityRating;
import com.example.backend.model.ActivityTickets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Repository
public class ActivityRepository {

	private static final Logger LOGGER = Logger.getLogger(ActivityRepository.class.getName());

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public ActivityRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Set<Activity> getAllActivities() {

		try {
			Set<Activity> activitySet = new HashSet<>(jdbcTemplate.query("SELECT *\n" +
																		 "  FROM activity;\n", BeanPropertyRowMapper.newInstance(Activity.class)));

			if (activitySet.isEmpty()) {
				return null;
			}

			LOGGER.info("Successfully retrieved " + activitySet.size() + " activities from DB");

			return activitySet;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public Activity getActivityByName(String name) {

		try {
			List<Activity> activity = jdbcTemplate.query("SELECT *\n" +
														 "  FROM activity\n" +
														 " WHERE name =" + "\"" + name + "\"" + ";\n", BeanPropertyRowMapper.newInstance(Activity.class));
			if (activity.isEmpty()) {
				return null;
			}

			LOGGER.info("Successfully retrieved activity from DB");

			return activity.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public int checkUserToActivity(int userId, int activityId) {
		List<ActivityTickets> check = new ArrayList<>();
		try {
			check = jdbcTemplate.query("SELECT *\n" +
									   "  FROM activityTickets\n" +
									   " WHERE user_id =" + "\"" + userId + "\"" + " and activity_id=" + activityId + ";\n", BeanPropertyRowMapper.newInstance(ActivityTickets.class));
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		if (check.size() <= 0) {
			LOGGER.info("User isn't enrolled to activity");
			return 0;
		} else {
			LOGGER.info("User is enrolled to activity");
			return 1;
		}
	}

	public Set<Activity> getEnrolledActivitiesForUser(int userID) {

		LOGGER.info("Getting activities from user with ID: [" + userID + "]");

		try {
			Set<Activity> enrolledActivities = new HashSet<>(jdbcTemplate.query("SELECT *\n" +
																				"  FROM activity a\n" +
																				" WHERE a.id IN (\n" +
																				"           SELECT at.activity_id\n" +
																				"             FROM activityTickets at\n" +
																				"            WHERE at.user_id = " + userID + "\n" +
																				"       );\n", BeanPropertyRowMapper.newInstance(Activity.class)));

			LOGGER.info("Successfully retrieved " + enrolledActivities.size() + " enrolled activities for user from DB");

			return enrolledActivities;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;
	}

	public Set<Activity> getUnenrolledActivitiesForUser(int userID) {

		LOGGER.info("Getting activities for user with ID: [" + userID + "]");

		try {
			Set<Activity> unenrolledActivities = new HashSet<>(jdbcTemplate.query("SELECT *\n" +
																				  "  FROM activity a\n" +
																				  " WHERE a.id NOT IN (\n" +
																				  "           SELECT at.activity_id\n" +
																				  "             FROM activityTickets at\n" +
																				  "            WHERE at.user_id = " + userID + "\n" +
																				  "       );\n", BeanPropertyRowMapper.newInstance(Activity.class)));

			LOGGER.info("Successfully retrieved " + unenrolledActivities.size() + " enrolled activities for user from DB");

			return unenrolledActivities;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;
	}

	public boolean ratingGiven(ActivityRating ar) {

		LOGGER.info("Getting activity rating for user with ID: [" + ar.getUserId() + "]");

		try {
			Set<ActivityRating> activityRatingSet = new HashSet<>(jdbcTemplate.query("SELECT *\n" +
																					 "  FROM activityRating\n" +
																					 " WHERE activity_id = " + ar.getActivityId() + " AND \n" +
																					 "       user_id =" + ar.getUserId() + ";", BeanPropertyRowMapper.newInstance(ActivityRating.class)));

			LOGGER.info("Successfully retrieved existing " + activityRatingSet.size() + " rating for activity for user from DB");

			if (activityRatingSet.size() > 0) {
				return true;
			}
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return false;
	}

	public int getActivityRatingForUser(int activityId, int userId) {

		LOGGER.info("Getting activity rating for user with ID: [" + userId + "]");

		try {
			Set<ActivityRating> activityRatingSet = new HashSet<>(jdbcTemplate.query("SELECT *\n" +
																					 "  FROM activityRating\n" +
																					 " WHERE activity_id = " + activityId + " AND \n" +
																					 "       user_id =" + userId + ";", BeanPropertyRowMapper.newInstance(ActivityRating.class)));

			LOGGER.info("Successfully retrieved existing " + activityRatingSet.size() + " rating for activity for user from DB");

			if (activityRatingSet.size() > 0) {
				return activityRatingSet.stream().findFirst().get().getRating();
			}
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return 0;
	}

	public ActivityRating getActivityTopRating(int activityId) {

		LOGGER.info("Getting activity rating with ID: [" + activityId + "]");

		try {
			Set<ActivityRating> activityRatingSet = new HashSet<>(jdbcTemplate.query("SELECT activity_id,\n" +
																					 "       count(activity_id) user_id,\n" +
																					 "       sum(rating) rating\n" +
																					 "  FROM activityRating\n" +
																					 " WHERE activity_id = " + activityId + "\n" +
																					 " GROUP BY activity_id\n" +
																					 " ORDER BY Rating DESC;\n", BeanPropertyRowMapper.newInstance(ActivityRating.class)));

			LOGGER.info("Successfully retrieved existing " + activityRatingSet.size() + " rating for activity for user from DB");

			if (activityRatingSet.size() > 0) {
				return activityRatingSet.stream().findFirst().get();
			}
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public void insertActivityRatingIntoDB(ActivityRating ar) {

		String sql = "INSERT INTO activityRating(activity_id, user_id, rating) " +
					 "VALUES(:activityId, :userId, :rating);";
		try {
			jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(ar));
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}

	public void deleteActivity(int activityId) {

		HashMap<String, Object> map = new HashMap<>();
		map.put("id", activityId);

		String sqlActivity = "DELETE FROM activity\n" +
							 "      WHERE id = :id;\n";

		try {
			jdbcTemplate.update(sqlActivity, map);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		String sqlTickets = "DELETE FROM activityTickets\n" +
							"      WHERE activity_id = :id;\n";

		try {
			jdbcTemplate.update(sqlTickets, map);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		String sqlRating = "DELETE FROM activityRating\n" +
						   "      WHERE activity_id = :id;\n";

		try {
			jdbcTemplate.update(sqlRating, map);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}

	public void updateActivityRatingForUser(ActivityRating ar) {
		String sql = "UPDATE activityRating\n" +
					 "   SET activity_id = :activityId,\n" +
					 "       user_id = :userId,\n" +
					 "       rating = :rating\n" +
					 " WHERE activity_id = :activityId AND \n" +
					 "       user_id = :userId;";

		HashMap<String, Object> map = new HashMap<>();
		map.put("activityId", ar.getActivityId());
		map.put("userId", ar.getUserId());
		map.put("rating", ar.getRating());

		try {
			jdbcTemplate.update(sql, map);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}

	public Activity insertActivityIntoDB(Activity ac) {

		String sql = "INSERT INTO activity(name, latitude, longitude, avb_places, id_cat, id_subcat, address, date, time, photo) " +
					 "VALUES(:name, :latitude, :longitude, :avbPlaces, :idCat, :idSubcat, :address, :date, :time, :photo);";
		try {
			jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(ac));
			return ac;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return ac;
	}

	public void insertActivityTicket(int userId, int activityId, String code) {

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("userId", userId);
		hashMap.put("activityId", activityId);
		hashMap.put("code", code);

		String sql = "INSERT INTO activityTickets(user_id, activity_id, pv_key) VALUES (:userId,:activityId,:code);";
		try {
			jdbcTemplate.update(sql, hashMap);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}

	public void deleteUserTicketForActivity(int userId, int activityId) {

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("userId", userId);
		hashMap.put("activityId", activityId);

		String sql = "DELETE FROM activityTickets WHERE user_id = :userId and activity_id =  :activityId;";

		try {
			int check = jdbcTemplate.update(sql, hashMap);
			if (check == 1) {
				LOGGER.info("Ticket deleted");
			} else {
				LOGGER.info("Couldn't delete ticket!");
			}
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}

	public void decreaseActivityParticipants(String activityName) {

		int activityParticipants = getActivityByName(activityName).getAvbPlaces();
		if (activityParticipants >= 1) {
			int activityId = getActivityByName(activityName).getId();
			HashMap<String, Integer> map = new HashMap<>();
			map.put("id", activityId);
			String sql = "UPDATE activity\n" +
						 "   SET avb_places = avb_places - 1\n" +
						 " WHERE id = :id;";
			try {
				jdbcTemplate.update(sql, map);
			} catch (DataAccessException e) {
				LOGGER.info(String.valueOf(e));
			}
		}
	}

	public void increaseActivityParticipants(String activityName) {

		int activityId = getActivityByName(activityName).getId();
		HashMap<String, Integer> map = new HashMap<>();
		map.put("id", activityId);
		String sql = "UPDATE activity\n" +
					 "   SET avb_places = avb_places + 1\n" +
					 " WHERE id = :id;";
		try {
			jdbcTemplate.update(sql, map);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}

	public void updateActivity(String name, String address, String date, String time, int avbPlaces, int id, double lat, double lng) {
		String sql = "UPDATE activity\n" +
					 "   SET name = :name,\n" +
					 "       date = :date,\n" +
					 "       address = :address,\n" +
					 "       time = :time,\n" +
					 "       latitude = :lat,\n" +
					 "       longitude = :lng,\n" +
					 "       avb_places = :avbPlaces\n" +
					 " WHERE id = :id;\n";

		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("address", address);
		map.put("date", date);
		map.put("time", time);
		map.put("lat", lat);
		map.put("lng", lng);
		map.put("avbPlaces", avbPlaces);
		map.put("id", id);

		try {
			jdbcTemplate.update(sql, map);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}
}
