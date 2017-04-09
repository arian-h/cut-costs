package com.boot.cut_costs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserRepository;

@RestController
public class UserRestAPI {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home() {
		User user = new User();
		user.setAge(12);
		user.setName("ARIAN");
		userRepository.save(user);
		return "hellosss";
	}
}
