package com.boot.cut_costs.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.exception.DuplicateUsernameException;
import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserDetailsRepository;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	/*
	 *  it's a naming convention, it returns CustomUserDetails instance
	 */
	@Override
	public CustomUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		CustomUserDetails account = userDetailsRepository.findByUsername(userName);
		if (account != null) {
			return account;
		} else {
			throw new UsernameNotFoundException("Could not find user with user name: " + userName );
		}
	}

	public void saveIfNotExists(String username, String password, String name) {
		if (userDetailsRepository.existsByUsername(username)) {
			logger.debug("Duplicate username " + username);
        	throw new DuplicateUsernameException("Username " + username + " is already taken");
		}
		CustomUserDetails userDetails = new CustomUserDetails(username, passwordEncoder.encode(password), true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		User user = new User();
		user.setName(name);
		userDetails.setUser(user);
		userDetailsRepository.save(userDetails);
	}
	
	public void delete(CustomUserDetails userDetails) {
		userDetailsRepository.delete(userDetails);
	}
}