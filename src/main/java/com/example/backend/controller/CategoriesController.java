package com.example.backend.controller;

import com.example.backend.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

	private final CategoriesService categoriesService;

	@Autowired
	public CategoriesController(CategoriesService categoriesService) {
		this.categoriesService = categoriesService;
	}

	@GetMapping("/categoriesMap")
	public String getCategoriesMap(){
		return categoriesService.getCategoriesMap();
	}
}
