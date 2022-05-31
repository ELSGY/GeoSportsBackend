package com.example.backend.controller;

import com.example.backend.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoriesController {

	private static final Logger LOGGER = Logger.getLogger(CategoriesController.class.getName());

	private final CategoriesService categoriesService;

	@Autowired
	public CategoriesController(CategoriesService categoriesService) {
		this.categoriesService = categoriesService;
	}

	@GetMapping("/categoriesMap")
	public String getCategoriesMap() {
		LOGGER.info("categories/categoriesMap endpoint was called");
		return categoriesService.getCategoriesMap();
	}

	@GetMapping("/addSubcategory/{categoryId}/{subcategoryName}")
	public String addSubcategory(@PathVariable int categoryId, @PathVariable String subcategoryName) {
		LOGGER.info("/addSubcategory endpoint was called");
		categoriesService.addSubcategory(categoryId, subcategoryName);
		return "Subcategory added";
	}
}
