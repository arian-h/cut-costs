package com.boot.cut_costs.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.security.auth.jwt.JWTLoginFilter;
import com.boot.cut_costs.security.model.AccountRepository;
import com.boot.cut_costs.security.model.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		CustomUserDetails account = accountRepository.findByUsername(userName);
		if (account != null) {
			return account;
		} else {
			throw new UsernameNotFoundException("could not find the user '" + userName + "'");
		}
	}

	public void save(String userName, String password) {
		CustomUserDetails userDetails = new CustomUserDetails(userName, passwordEncoder.encode(password), true, true, true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ROLE"));
		accountRepository.save(userDetails);
		logger.debug("New user with username " + userName + " was created");
	}
	
	public boolean exists(String userName) {
		return accountRepository.exists(userName);
	}
	
}
