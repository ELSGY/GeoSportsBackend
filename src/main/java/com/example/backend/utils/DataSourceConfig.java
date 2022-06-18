package com.example.backend.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	private static final Logger LOGGER = Logger.getLogger(DataSourceConfig.class.getName());

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.url}")
	private String dataSourceURL;

	@Bean
	@Primary
	public DataSource dataSource() {
		LOGGER.info("Connecting to DB:");
		final HikariConfig hikariConfig = new HikariConfig();

		LOGGER.info("Set data source url : " + dataSourceURL);
		hikariConfig.setJdbcUrl(dataSourceURL);
		LOGGER.info("Set driver class name : " + driverClassName);
		hikariConfig.setDriverClassName(driverClassName);

		return new HikariDataSource(hikariConfig);
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		LOGGER.info("Create new NamedParameterJdbcTemplate");
		return new NamedParameterJdbcTemplate(dataSource());
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("geosports.srl@gmail.com");
		mailSender.setPassword("sphobgxmctpwciuf");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

}