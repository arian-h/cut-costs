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

import com.boot.cut_costs.security.model.UserRepository;
import com.boot.cut_costs.security.model.UserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository accountRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDetails account = accountRepository.findByUsername(userName);
		if (account != null) {
			return account;
		} else {
			throw new UsernameNotFoundException("could not find the user '" + userName + "'");
		}
	}

	@Transactional
	public void save(UserDetails user) {
		String userName = user.getUsername();
		String password = user.getPassword();
		UserDetails userDetails = new UserDetails(userName, passwordEncoder.encode(password), true, true, true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ROLE"));
		accountRepository.save(userDetails);
		logger.debug("New user with username " + userName + " was created");
	}
	
	public boolean exists(String userName) {
		return accountRepository.exists(userName);
	}
	
	@Transactional
	public void update(UserDetails user, String newPassword) {
		String userName = user.getUsername();
		UserDetails userDetails = new UserDetails(userName, passwordEncoder.encode(newPassword), true, true, true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ROLE"));
		accountRepository.save(userDetails);
		logger.debug("Credentials for user with username " + userName + " was updated");
	}
	
}