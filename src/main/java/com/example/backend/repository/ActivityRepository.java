package com.example.backend.repository;

import com.example.backend.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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

			LOGGER.info("Successfully retrieved user from DB");

			return activity.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;
	}

	public Activity insertActivityIntoDB(Activity ac) {

		String sql = "INSERT INTO activity(name, latitude, longitude, avb_places, id_cat, id_subcat, address, date, time) " +
					 "VALUES(:name, :latitude, :longitude, :avbPlaces, :idCat, :idSubcat, :address, :date, :time);";
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

	public void updateActivityParticipants(int activityId) {
		String sql = "UPDATE activity\n" +
					 "   SET avb_places = avb_places - 1\n" +
					 " WHERE id='?';";
		try {
			jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(activityId));
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}

}
