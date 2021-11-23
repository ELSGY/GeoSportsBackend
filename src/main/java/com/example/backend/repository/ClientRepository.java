package com.example.backend.repository;

import com.example.backend.model.Client;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class ClientRepository {

	private static final Logger LOGGER = Logger.getLogger(ClientRepository.class.getName());

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public ClientRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Set<Client> getUsers() {

		try {
			Set<Client> usersList = new HashSet<>(jdbcTemplate.query("select * from clients;", BeanPropertyRowMapper.newInstance(Client.class)));

			LOGGER.info("Successfully retrieved " + usersList.size() + " events from DB");

			return usersList;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;
	}
}
