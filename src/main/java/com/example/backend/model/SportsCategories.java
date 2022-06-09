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
public class SportsCategories implements Comparable<SportsCategories> {

	private int id;
	private String catName;

	@Override
	public int compareTo(SportsCategories o) {
		return Comparator.comparing(SportsCategories::getId)
						 .compare(this, o);
	}
}
