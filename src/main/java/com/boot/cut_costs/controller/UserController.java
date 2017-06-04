package com.boot.cut_costs.controller;

import java.io.IOException;
import java.security.Principal;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.DTO.UserDto;
import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.service.CustomUserDetailsService;
import com.boot.cut_costs.service.UserService;
import com.boot.cut_costs.validator.SingupUserDtoValidator;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private SingupUserDtoValidator userDTOValidator;
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(path="/{userId}",method=RequestMethod.GET)
	public User getUserById(@PathVariable long userId) {
		return userService.load(userId);
	}
	
	@RequestMapping(path="",method=RequestMethod.GET)
	public User getCurrentUser(Principal principal) {
		CustomUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		return userService.load(userDetails.getUser().getId());
	}

	@RequestMapping(path="",method=RequestMethod.PUT)
	public void update(@RequestBody UserDto userDto, Principal principal, BindingResult result) throws IOException {
		userDTOValidator.validate(userDto, result);
		logger.debug("User information update attempt with name: " + principal.getName());
		if (result.hasErrors()) {
			throw new ValidationException();
		}
		CustomUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		userService.update(userDetails, userDto);
	}
}
