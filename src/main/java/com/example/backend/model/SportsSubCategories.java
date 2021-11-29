package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

public class SportsSubCategories implements Comparable<SportsSubCategories> {

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String subcatName;
	@Getter
	@Setter
	private int cat;

	public SportsSubCategories() {

	}

	public SportsSubCategories(int id, String subCatName, int catId) {
		this.id = id;
		this.subcatName = subCatName;
		this.cat = catId;
	}

	@Override
	public int compareTo(SportsSubCategories o) {
		return Comparator.comparing(SportsSubCategories::getCat)
						 .compare(this, o);
	}
}
