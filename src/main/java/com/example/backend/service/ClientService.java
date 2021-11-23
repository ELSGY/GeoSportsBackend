package com.example.backend.service;

import com.example.backend.model.Client;
import com.example.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.logging.Logger;

@Service
public class ClientService {

	private static final Logger LOGGER = Logger.getLogger(ClientService.class.getName());

	private final ClientRepository clientRepository;

	@Autowired
	public ClientService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public String getAllUsers() {

		Set<Client> usersList = clientRepository.getUsers();

		LOGGER.info(String.valueOf(usersList));

		return "";
	}
}
