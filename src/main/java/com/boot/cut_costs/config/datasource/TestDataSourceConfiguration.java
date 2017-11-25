package com.boot.cut_costs.config.datasource;

import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestDataSourceConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	@Profile("test")
	public DataSource testDataSource() throws URISyntaxException {
		return DataSourceBuilder.create().build();
	}

}