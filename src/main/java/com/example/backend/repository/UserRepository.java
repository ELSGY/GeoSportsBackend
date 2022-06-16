package com.example.backend.repository;

import com.example.backend.model.Activity;
import com.example.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Repository
public class UserRepository {

	private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Set<User> getUsers() {

		try {
			Set<User> usersList = new HashSet<>(jdbcTemplate.query("SELECT id," +
																   "       username,\n" +
																   "       email\n" +
																   "  FROM users;", BeanPropertyRowMapper.newInstance(User.class)));
			if (usersList.isEmpty()) {
				LOGGER.info("Cannot retrieve users from DB");
				return null;
			}
			LOGGER.info("Successfully retrieved " + usersList.size() + " users from DB");

			return usersList;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public User getUserById(int id) {

		try {
			List<User> user = jdbcTemplate.query("SELECT id,\n" +
												 "       full_name,\n" +
												 "       username,\n" +
												 "       email,\n" +
												 "       photo\n" +
												 "  FROM users\n" +
												 " WHERE id = " + id + ";\n", BeanPropertyRowMapper.newInstance(User.class));
			if (user.isEmpty()) {
				LOGGER.info("Cannot retrieve user from DB");
				return null;
			}
			LOGGER.info("Successfully retrieved user from DB");

			return user.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public User getUserByUsername(String username) {

		// start method
		long startTime = System.nanoTime() / 1000000;

		try {
			List<User> user = jdbcTemplate.query("SELECT *\n" +
												 "  FROM users\n" +
												 " WHERE username = " + "\"" + username + "\"" + ";\n", BeanPropertyRowMapper.newInstance(User.class));
			if (user.isEmpty()) {
				LOGGER.info("Cannot retrieve user from DB");
				getElapsedTime(startTime);
				return null;
			}
			LOGGER.info("Successfully retrieved user from DB");
			getElapsedTime(startTime);
			return user.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		getElapsedTime(startTime);
		return null;
	}

	private void getElapsedTime(long startTime) {
		// end method
		long endTime = System.nanoTime() / 1000000;

		// elapsed time
		double diff = endTime - startTime;
		LOGGER.info("Function getUserByUsername took: " + diff);
	}

	public User getUserByEmail(String email) {

		try {
			List<User> user = jdbcTemplate.query("SELECT *\n" +
												 "  FROM users\n" +
												 " WHERE email = " + "\"" + email + "\"" + ";\n", BeanPropertyRowMapper.newInstance(User.class));
			if (user.isEmpty()) {
				LOGGER.info("Cannot retrieve user from DB");
				return null;
			}
			LOGGER.info("Successfully retrieved user bt email from DB");
			return user.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public User getUserByPVKey(String pvKey) {

		try {
			List<User> user = jdbcTemplate.query("SELECT u.id,\n" +
												 "       u.full_name,\n" +
												 "       u.username,\n" +
												 "       u.email,\n" +
												 "       u.photo\n" +
												 "  FROM users u,\n" +
												 "       activityTickets at\n" +
												 " WHERE u.id = at.user_id AND \n" +
												 "       at.pv_key = " + "\"" + pvKey + "\"" + ";\n", BeanPropertyRowMapper.newInstance(User.class));

			if (user.isEmpty()) {
				LOGGER.info("Cannot retrieve user from DB");
				return null;
			} else {
				LOGGER.info("Successfully retrieved user from DB");

				return user.get(0);
			}

		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public Activity getActivityByPVKey(String pvKey) {

		try {
			List<Activity> activity = jdbcTemplate.query("SELECT a.name\n" +
														 "  FROM activity a,\n" +
														 "       activityTickets at\n" +
														 " WHERE a.id = at.activity_id AND \n" +
														 "       at.pv_key = " + "\"" + pvKey + "\"" + ";\n", BeanPropertyRowMapper.newInstance(Activity.class));

			if (activity.isEmpty()) {
				LOGGER.info("Cannot retrieve user from DB");
				return null;
			} else {
				LOGGER.info("Successfully retrieved user from DB");

				return activity.get(0);
			}

		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
		return null;
	}

	public void insertUser(User user) {
		String sql = "INSERT INTO users(full_name, username, email, password, photo, isAdmin) " +
					 "VALUES(:full_name, :username, :email, :password, :photo, :isAdmin);";
		try {
			jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(user));

		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}
	}
}
