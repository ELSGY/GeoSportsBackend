package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SportsSubCategories implements Comparable<SportsSubCategories> {

	private int id;
	private String subcatName;
	private int cat;

	@Override
	public int compareTo(SportsSubCategories o) {
		return Comparator.comparing(SportsSubCategories::getCat)
						 .compare(this, o);
	}
}
