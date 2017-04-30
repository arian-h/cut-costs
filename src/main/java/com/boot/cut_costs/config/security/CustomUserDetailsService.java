package com.boot.cut_costs.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.boot.cut_costs.repository.AccountRepository;

public class CustomUserDetailsService implements UserDetailsService {
	
	private AccountRepository accountRepository;

	public CustomUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomUserDetails account = accountRepository.findByUsername(username);
		if (account != null) {
			return account;
		} else {
			throw new UsernameNotFoundException("could not find the user '" + username + "'");
		}
	}
	
}
