package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

public class ActivityTickets {

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private int user_id;
	@Getter
	@Setter
	private int activity_id;
	@Getter
	@Setter
	private int pv_key;

}
