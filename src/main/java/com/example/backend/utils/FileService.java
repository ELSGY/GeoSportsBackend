package com.example.backend.utils;

import com.example.backend.utils.annotations.ExcludeFieldFromJson;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileService {

	public static <E> String objectToJson(E inputObject) {
		ExclusionStrategy strategy = new ExclusionStrategy() {
			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}

			@Override
			public boolean shouldSkipField(FieldAttributes field) {
				return field.getAnnotation(ExcludeFieldFromJson.class) != null;
			}
		};

		Gson gson = new GsonBuilder()
				.addSerializationExclusionStrategy(strategy)
				.setPrettyPrinting()
				.create();

		return gson.toJson(inputObject);
	}
}
