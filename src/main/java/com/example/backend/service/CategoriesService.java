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
		if (sportsCategoriesSet.isEmpty()) {
			return "Could not get categories from DB";
		}
		TreeSet<SportsCategories> sortedCategoriesTreeSet = new TreeSet<>(sportsCategoriesSet);

		// get subcategories
		Set<SportsSubCategories> sportsSubCategoriesSet = categoriesRepository.getSportsSubCategories();
		if (sportsSubCategoriesSet.isEmpty()) {
			return "Could not get subcategories from DB";
		}

		JsonArray categoriesMap = getJsonArray(sortedCategoriesTreeSet, sportsSubCategoriesSet);

		return FileService.objectToJson(categoriesMap);
	}

	private JsonArray getJsonArray(TreeSet<SportsCategories> sortedCategoriesTreeSet, Set<SportsSubCategories> sportsSubCategoriesSet) {
		JsonArray categoriesMap = new JsonArray();

		// create categories map
		sortedCategoriesTreeSet.forEach(cat -> {

			JsonObject categories = new JsonObject();
			JsonArray subCategories = new JsonArray();


			sportsSubCategoriesSet.forEach(subCat -> {
				JsonObject subCategory = new JsonObject();

				if (subCat.getCat() == cat.getId()) {
					subCategory.addProperty("subCatId", subCat.getId());
					subCategory.addProperty("subCatName", subCat.getSubcatName());
					subCategories.add(subCategory);
				}
			});
			JsonObject category = new JsonObject();
			category.addProperty("catId", cat.getId());
			category.addProperty("catName", cat.getCatName());

			categories.add("category", category);
			categories.add("subcategories", subCategories);

			categoriesMap.add(categories);

		});
		return categoriesMap;
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

	public void addSubcategory(int categoryId, String subcategoryName) {
		categoriesRepository.addSubcategory(categoryId, subcategoryName);
	}
}
