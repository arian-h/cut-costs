package com.boot.cut_costs.config.datasource;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProductionDataSourceConfiguration {
	@Bean
	@Primary
	@Profile("!test")
	public BasicDataSource productionDataSource() throws URISyntaxException {
		URI dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));
		System.out.println("---------------------------------------");
		System.out.println(System.getenv("CLEARDB_DATABASE_URL"));
		String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = String.format("jdbc:mysql://%s:%d%s?useSSL=false", dbUri.getHost(), dbUri.getPort(),  dbUri.getPath());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        System.out.println("PORT " + dbUri.getPort());
        //basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        System.out.println(username);
        System.out.println(password);
        System.out.println(dbUrl);
        System.out.println("+++++++++++++++++++++");
        return basicDataSource;
	}
	
}
