package com.example.backend.controller;

import com.example.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sendEnrollMail")
public class SendEnrollMail {

	private final ClientService clientService;

	@Autowired
	public SendEnrollMail(ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public String sendEnrollMail() {
		return clientService.getAllUsers();
	}
}
