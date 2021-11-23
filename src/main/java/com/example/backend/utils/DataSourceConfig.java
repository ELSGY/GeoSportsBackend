package com.example.backend.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.url}")
	private String dataSourceURL;

	@Bean
	@Primary
	public DataSource dataSource() {
		LOGGER.debug("Connecting to DB:");
		final HikariConfig hikariConfig = new HikariConfig();

		LOGGER.debug("Set data source url : {}", dataSourceURL);
		hikariConfig.setJdbcUrl(dataSourceURL);
		LOGGER.debug("Set driver class name : {}", driverClassName);
		hikariConfig.setDriverClassName(driverClassName);

		return new HikariDataSource(hikariConfig);
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		LOGGER.debug("Create new NamedParameterJdbcTemplate");
		return new NamedParameterJdbcTemplate(dataSource());
	}

}