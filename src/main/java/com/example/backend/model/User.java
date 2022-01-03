package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

public class User {

	@Getter
	@Setter
	private int id;
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
	private byte[] photo;
	@Getter
	@Setter
	private boolean isAdmin;

	public User() {

	}

	public User(int id, String fullName, String username, String email, byte[] photo, boolean isAdmin) {
		this.id = id;
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.photo = photo;
		this.isAdmin = isAdmin;
	}
}