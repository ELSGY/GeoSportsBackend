package com.example.backend.controller;

import com.example.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sendEnrollMail")
public class SendEnrollMail {

	private final ClientRepository clientRepository;

	@Autowired
	public SendEnrollMail(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@GetMapping
	public String sendEnrollMail() {
		return clientRepository.getUsers();
	}
}
