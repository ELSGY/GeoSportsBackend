package com.example.backend.repository;

import com.example.backend.model.Activity;
import com.example.backend.model.SportsCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
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

}
