//package com.boot.cut_costs.config;
//
//import java.util.Arrays;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//import com.boot.cut_costs.model.CustomUserDetails;
//import com.boot.cut_costs.model.User;
//import com.boot.cut_costs.service.CustomUserDetailsService;
//
//@TestConfiguration
//public class SpringSecurityWebAuxTestConfig {
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	@Autowired
//	private CustomUserDetailsService userDetailsServices;
//	
//	@Primary
//	@Bean
//	public UserDetailsService userDetailsService() {
//		String username = "basicuser_username";
//		String password = "basicuser_password";
//		String name = "basicuser_name";
//
//		User user = new User();
//		user.setName(name);
//
//		CustomUserDetails userDetails = new CustomUserDetails(username,
//				passwordEncoder.encode(password), true, true, true, true,
//				AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
//		userDetails.setUser(user);
//		
//		//userDetailsServices.saveIfNotExists(username, password, name);
//		
//		return new InMemoryUserDetailsManager(Arrays.asList(userDetails));
//	}
//}