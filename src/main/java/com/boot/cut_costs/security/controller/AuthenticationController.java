package com.boot.cut_costs.security.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.security.model.CustomUserDetails;
import com.boot.cut_costs.security.service.CustomUserDetailsService;
import com.boot.cut_costs.security.service.AuthenticationService;
import com.boot.cut_costs.security.validator.CustomUserDetailsValidator;

@RestController
public class AuthenticationController {
	
	@Autowired
	protected AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsServices;

	@Autowired
	private CustomUserDetailsValidator userDetailsValidator;
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(@RequestBody CustomUserDetails user, HttpServletResponse response) throws IOException {
		logger.debug("Authenticating login attemp from user " + user.getUsername());
		authenticateUserAndSetSession(user, response);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void create(@RequestBody CustomUserDetails user, HttpServletResponse response, BindingResult result) {
		userDetailsValidator.validate(user, result);
		logger.debug("User signup attempt with username: " + user.getUsername());
		if (result.hasErrors()) {
			throw new ValidationException();
		}
		userDetailsServices.saveIfNotExists(user);
		
		authenticateUserAndSetSession(user, response);
	}

	private void authenticateUserAndSetSession(CustomUserDetails user, HttpServletResponse response) throws BadCredentialsException {
		String username = user.getUsername();
        String password = user.getPassword();
        logger.debug("Authentication user " + username);
        Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		if (authenticatedUser != null) {
	        AuthenticationService.addAuthentication(response, authenticatedUser.getName());
	        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);			
		} else {
			logger.debug("Authentication failed for user " + username);
			throw new BadCredentialsException("Bad credentials provided");
		}
    }

}