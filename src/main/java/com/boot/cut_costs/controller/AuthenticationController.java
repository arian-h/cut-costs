package com.boot.cut_costs.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.dto.UserDetailsDto;
import com.boot.cut_costs.exception.InputValidationException;
import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.service.AuthenticationService;
import com.boot.cut_costs.service.CustomUserDetailsService;
import com.boot.cut_costs.validator.UserDetailsDtoValidator;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	protected AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private UserDetailsDtoValidator createUserDetailsDtoValidator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public long login(@RequestBody UserDetailsDto userDetailsDTO, HttpServletResponse response) throws IOException {
		logger.debug("Authenticating login attemp from user " + userDetailsDTO.getUsername());
		return authenticateUserAndSetSession(userDetailsDTO.getUsername(), userDetailsDTO.getPassword(), response);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void create(@RequestBody UserDetailsDto userDetailsDTO, HttpServletResponse response, BindingResult result) {
		String username = userDetailsDTO.getUsername();
		String password = userDetailsDTO.getPassword();
		String name = userDetailsDTO.getName();
		logger.debug("User signup attempt with username: " + username);
		createUserDetailsDtoValidator.validate(userDetailsDTO, result);
		if (result.hasErrors()) {
			throw new InputValidationException(result.getFieldError().getField());
		}
		userDetailsService.saveIfNotExists(username, passwordEncoder.encode(password), name);
		authenticateUserAndSetSession(username, password, response);
		logger.debug("New UserDetails with username " + username + " was created");
	}

	private long authenticateUserAndSetSession(String username, String password, HttpServletResponse response) throws BadCredentialsException {
        logger.debug("Authentication user " + username);
        Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		if (authenticatedUser != null) {
			AuthenticationService.addAuthentication(response, authenticatedUser.getName());
	        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
	        return ((CustomUserDetails)(authenticatedUser.getPrincipal())).getId();
		} else {
			logger.debug("Authentication failed for user " + username);
			throw new BadCredentialsException("Bad credentials provided");
		}
    }

}