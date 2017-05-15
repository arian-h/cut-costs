package com.boot.cut_costs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.boot.cut_costs.security.model.UserRepository;
import com.boot.cut_costs.security.model.UserDetails;

/**
 * Application entry point
 *
 */
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Bean
	CommandLineRunner init(UserRepository accountRepository) {
		return (arg) -> {
			accountRepository.deleteAll();
			accountRepository
					.save(new UserDetails("admin", passwordEncoder.encode("password"), true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ROLE")));
		};
	}
}