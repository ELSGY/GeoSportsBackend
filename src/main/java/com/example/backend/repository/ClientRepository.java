package com.example.backend.repository;

import com.example.backend.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class ClientRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientRepository.class);

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public ClientRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getUsers(){

		Set<Client> usersList = new HashSet<>(jdbcTemplate.query("select * from clients", BeanPropertyRowMapper.newInstance(Client.class)));

		LOGGER.debug("Successfully retrieved {} events from DB", usersList.size());

		return usersList.toString();

	}
}
