package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Activity activity = (Activity) o;
		return getId() == activity.getId() && Double.compare(activity.getLatitude(), getLatitude()) == 0 && Double.compare(activity.getLongitude(), getLongitude()) == 0 && getAvbPlaces() == activity.getAvbPlaces() && getIdCat() == activity.getIdCat() && getIdSubcat() == activity.getIdSubcat() && getName().equals(activity.getName()) && getAddress().equals(activity.getAddress()) && getDate().equals(activity.getDate()) && getTime().equals(activity.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getLatitude(), getLongitude(), getAvbPlaces(), getIdCat(), getIdSubcat(), getAddress(), getDate(), getTime());
	}
}
