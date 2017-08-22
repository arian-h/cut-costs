package com.boot.cut_costs.utils;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserDetailsRepository;

@Component
@Transactional
public class UserTestingUtil {

	private final static String DUMMY_PASSWORD = "Test1234";

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	public UserTestingUtil() {
	}
	
	/**
	 * Create a user details object and a user. Map user details to the user and save the user details to the db
	 */
	public User createUser(String username, String name, String description, String imageId) throws IOException {
		User user = new User();
		user.setName(name);
		user.setDescription(description);
		user.setImageId(imageId);
		CustomUserDetails userDetails = new CustomUserDetails(username, DUMMY_PASSWORD, true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		userDetails.setUser(user);
		userDetailsRepository.save(userDetails);
		return user;
	}
}