package com.example.backend.repository;

import com.example.backend.model.Activity;
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

	public void updateActivity(String name, String date, String time, int avbPlaces, int id) {
		String sql = "UPDATE activity\n" +
					 "   SET name = :name,\n" +
					 "       date = :date,\n" +
					 "       time = :time,\n" +
					 "       avb_places = :avbPlaces\n" +
					 " WHERE id = :id;\n";

		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("date", date);
		map.put("time", time);
		map.put("avbPlaces", avbPlaces);
		map.put("id", id);

		try {
			jdbcTemplate.update(sql, map);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}
}
