package com.example.backend.service;

import com.example.backend.model.SportsCategories;
import com.example.backend.model.SportsSubCategories;
import com.example.backend.repository.CategoriesRepository;
import com.example.backend.utils.FileService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

@Service
public class CategoriesService {

	private static final Logger LOGGER = Logger.getLogger(CategoriesService.class.getName());

	private final CategoriesRepository categoriesRepository;

	@Autowired
	public CategoriesService(CategoriesRepository categoriesRepository) {
		this.categoriesRepository = categoriesRepository;
	}

	public String getCategoriesMap() {

		// get categories and sort them
		Set<SportsCategories> sportsCategoriesSet = categoriesRepository.getSportsCategories();
		TreeSet<SportsCategories> sortedCategoriesTreeSet = new TreeSet<>(sportsCategoriesSet);

		// get subcategories
		Set<SportsSubCategories> sportsSubCategoriesSet = categoriesRepository.getSportsSubCategories();

		JsonArray categoriesMap = new JsonArray();

		// create categories map
		sortedCategoriesTreeSet.forEach(cat -> {

			JsonObject category = new JsonObject();
			JsonArray subCategories = new JsonArray();

			sportsSubCategoriesSet.forEach(subCat -> {

//				LOGGER.info("" + subCat.getCat());

				if (subCat.getCat() == cat.getId()) {
					subCategories.add(subCat.getSubcatName());
				}
			});

			category.addProperty("category", cat.getCatName());
			category.add("subcategories", subCategories);

			categoriesMap.add(category);

		});

		return FileService.objectToJson(categoriesMap);
	}

	public String getCategoryById(int id) {
		return categoriesRepository.getCategoryById(id).getCatName();
	}

	public String getSubcategoryById(int id) {
		return categoriesRepository.getSubcategoryById(id).getSubcatName();
	}

	public int getCategoryByName(String name) {
		return categoriesRepository.getCategoryByName(name).getId();
	}

	public int getSubcategoryByName(String name) {
		return categoriesRepository.getSubcategoryByName(name).getId();
	}
}
