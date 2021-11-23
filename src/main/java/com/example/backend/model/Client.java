package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

public class Client {

	@Getter
	@Setter
	private String fullName;
	@Getter
	@Setter
	private String username;
	@Getter
	@Setter
	private String email;
	@Getter
	@Setter
	private String password;
	@Getter
	@Setter
	private byte[] photo;

	public Client() {

	}

	public Client(String fullName, String username, String email, String password, byte[] photo) {
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.photo = photo;
	}

}