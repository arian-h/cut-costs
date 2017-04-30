package com.boot.cut_costs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.config.security.CustomUserDetails;
import com.boot.cut_costs.repository.AccountRepository;

@RestController
public class UserAcccountController {
	@Autowired
	AccountRepository accountRepository;

	@RequestMapping(path = "/signup", method = RequestMethod.POST)
	public void addUser(@RequestBody CustomUserDetails account) {
		String username = account.getUsername();
		String password = account.getPassword();
		CustomUserDetails userDetails = new CustomUserDetails(username,
				password, true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ROLE"));
		accountRepository.save(userDetails);
	}

}
