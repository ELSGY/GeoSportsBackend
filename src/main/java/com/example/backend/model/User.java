package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

public class User {

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String full_name;
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
	private String photo;
	@Getter
	@Setter
	private String isAdmin;

	public User() {

	}

	public User(int id, String full_name, String username, String email, String password, String photo, String isAdmin) {
		this.id = id;
		this.full_name = full_name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.photo = photo;
		this.isAdmin = isAdmin;
	}
}