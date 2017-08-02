package com.boot.cut_costs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point
 *
 */
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

//	@Bean
//	CommandLineRunner init(UserDetailsRepository accountRepository) {
//		return (arg) -> {
//			accountRepository.deleteAll();
//		};
//	}
}