package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

public class SportsCategories implements Comparable<SportsCategories> {

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String catName;

	public SportsCategories() {

	}

	public SportsCategories(int id, String catName) {
		this.id = id;
		this.catName = catName;
	}

	@Override
	public int compareTo(SportsCategories o) {
		return Comparator.comparing(SportsCategories::getId)
						 .compare(this, o);
	}
}
