package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

public class Activity {

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private double latitude;
	@Getter
	@Setter
	private double longitude;
	@Getter
	@Setter
	private int avbPlaces;
	@Getter
	@Setter
	private int idCat;
	@Getter
	@Setter
	private int idSubcat;
	@Getter
	@Setter
	private String address;
	@Getter
	@Setter
	private String date;
	@Getter
	@Setter
	private String time;

	public Activity() {

	}

	public Activity(int id, String name, double latitude, double longitude, int avbPlaces, int idCat, int idSubcat, String address, String date, String time) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.avbPlaces = avbPlaces;
		this.idCat = idCat;
		this.idSubcat = idSubcat;
		this.address = address;
		this.date = date;
		this.time = time;
	}

	public String toString() {
		return name + " " + latitude + " " + longitude + " " + avbPlaces + " " + idCat + " " + idSubcat + " " + address + " " + date + " " + time;
	}
}
