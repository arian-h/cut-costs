package com.boot.cut_costs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.boot.cut_costs.repository.UserDetailsRepository;
import com.boot.cut_costs.security.model.CustomUserDetails;

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
	CommandLineRunner init(UserDetailsRepository accountRepository) {
		return (arg) -> {
			accountRepository.deleteAll();
//			accountRepository
//					.save(new CustomUserDetails("administrator", passwordEncoder.encode("Aryan12345"), true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")));
		};
	}
}