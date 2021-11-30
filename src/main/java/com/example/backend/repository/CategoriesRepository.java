package com.example.backend.repository;

import com.example.backend.model.SportsCategories;
import com.example.backend.model.SportsSubCategories;
import com.example.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Repository
public class CategoriesRepository {

	private static final Logger LOGGER = Logger.getLogger(CategoriesRepository.class.getName());

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public CategoriesRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Set<SportsCategories> getSportsCategories() {

		try {
			Set<SportsCategories> sportsCategoriesSet = new HashSet<>(jdbcTemplate.query("SELECT *\n" +
																						 "  FROM activityCategories;\n", BeanPropertyRowMapper.newInstance(SportsCategories.class)));

			LOGGER.info("Successfully retrieved " + sportsCategoriesSet.size() + " sports categories from DB");

			return sportsCategoriesSet;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;

	}

	public Set<SportsSubCategories> getSportsSubCategories() {

		try {
			Set<SportsSubCategories> sportsSubCategoriesSet = new HashSet<>(jdbcTemplate.query("SELECT *\n" +
																							   "  FROM activitySubCategories;\n", BeanPropertyRowMapper.newInstance(SportsSubCategories.class)));

			LOGGER.info("Successfully retrieved " + sportsSubCategoriesSet.size() + " sports subcategories from DB");

			return sportsSubCategoriesSet;
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;

	}

	public SportsCategories getCategoryById(int id) {

		try {
			List<SportsCategories> category = jdbcTemplate.query("SELECT *\n" +
																 "  FROM activityCategories\n" +
																 "  where id =" + id + ";\n", BeanPropertyRowMapper.newInstance(SportsCategories.class));

			LOGGER.info("Successfully retrieved category from DB");

			return category.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;

	}

	public SportsCategories getCategoryByName(String name) {

		try {
			List<SportsCategories> category = jdbcTemplate.query("SELECT *\n" +
																 "  FROM activityCategories\n" +
																 "  where cat_name =" + "\"" + name + "\"" + ";\n", BeanPropertyRowMapper.newInstance(SportsCategories.class));

			LOGGER.info("Successfully retrieved category from DB");

			return category.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;

	}

	public SportsSubCategories getSubcategoryById(int id) {

		try {
			List<SportsSubCategories> subcategory = jdbcTemplate.query("SELECT *\n" +
																	   "  FROM activitySubCategories\n" +
																	   "  where id =" + id + ";\n", BeanPropertyRowMapper.newInstance(SportsSubCategories.class));

			LOGGER.info("Successfully retrieved subcategory from DB");

			return subcategory.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;

	}

	public SportsSubCategories getSubcategoryByName(String name) {

		try {
			List<SportsSubCategories> subcategory = jdbcTemplate.query("SELECT *\n" +
																	   "  FROM activitySubCategories\n" +
																	   "  where cat_name =" + "\"" + name + "\"" + ";\n", BeanPropertyRowMapper.newInstance(SportsSubCategories.class));

			LOGGER.info("Successfully retrieved subcategory from DB");

			return subcategory.get(0);
		} catch (DataAccessException e) {
			LOGGER.info(String.valueOf(e));
		}

		return null;

	}
}
