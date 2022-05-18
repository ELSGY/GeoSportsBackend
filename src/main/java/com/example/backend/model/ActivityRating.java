package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

public class ActivityRating {

	@Getter
	@Setter
	private int activityId;
	@Getter
	@Setter
	private int userId;
	@Getter
	@Setter
	private int rating;

	public ActivityRating() {

	}

	public ActivityRating(int activity_id, int user_id, int rating) {
		this.activityId = activity_id;
		this.userId = user_id;
		this.rating = rating;
	}
}
