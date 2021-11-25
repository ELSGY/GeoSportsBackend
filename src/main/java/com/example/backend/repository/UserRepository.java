package com.example.backend.repository;

import com.example.backend.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

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
			Set<User> usersList = new HashSet<>(jdbcTemplate.query("SELECT id,\n" +
																   "       full_name,\n" +
																   "       username,\n" +
																   "       email,\n" +
																   "       photo\n" +
																   "  FROM users;", BeanPropertyRowMapper.newInstance(User.class)));

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

			LOGGER.info("Successfully retrieved user from DB");

			return user.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;
	}

	public User getUserByName(String name) {

		try {
			List<User> user = jdbcTemplate.query("SELECT id,\n" +
												 "       full_name,\n" +
												 "       username,\n" +
												 "       email,\n" +
												 "       photo\n" +
												 "  FROM users\n" +
												 " WHERE full_name = " + "\"" + name + "\"" + ";\n", BeanPropertyRowMapper.newInstance(User.class));

			LOGGER.info("Successfully retrieved user from DB");

			return user.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;
	}

}
