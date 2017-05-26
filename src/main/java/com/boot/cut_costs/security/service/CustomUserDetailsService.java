package com.boot.cut_costs.security.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.repository.UserDetailsRepository;
import com.boot.cut_costs.security.exception.DuplicateUsernameException;
import com.boot.cut_costs.security.model.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Override
	public CustomUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		CustomUserDetails account = userDetailsRepository.findByUsername(userName);
		if (account != null) {
			return account;
		} else {
			throw new UsernameNotFoundException("could not find the user '" + userName + "'");
		}
	}

	@Transactional
	public void saveIfNotExists(CustomUserDetails user) {
		String username = user.getUsername();
		String password = user.getPassword();
		if (userDetailsRepository.existsByUsername(username)) {
			logger.debug("Duplicate username " + username);
        	throw new DuplicateUsernameException();
		}
		CustomUserDetails userDetails = new CustomUserDetails(username, passwordEncoder.encode(password), true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		userDetailsRepository.save(userDetails);
		logger.debug("New user with username " + username + " was created");
	}
}