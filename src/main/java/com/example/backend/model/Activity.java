package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Activity {

	private int id;
	private String name;
	private double latitude;
	private double longitude;
	private int avbPlaces;
	private int idCat;
	private int idSubcat;
	private String address;
	private String date;
	private String time;
	private String photo;
}
