package com.boot.cut_costs.security.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.security.exception.DuplicateUsernameException;
import com.boot.cut_costs.security.model.UserDetails;
import com.boot.cut_costs.security.service.CustomUserDetailsService;
import com.boot.cut_costs.security.service.TokenAuthenticationService;

@RestController
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	protected AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(@RequestBody UserDetails user, HttpServletResponse response) throws IOException {
		
		logger.debug("Authenticating login attemp from user " + user.getUsername());

		authenticateUserAndSetSession(user, response);
	}


	@RequestMapping(value = "/singup", method = RequestMethod.PUT)
	public void update(@RequestBody UserDetails user, HttpServletResponse response) throws IOException {
		
		logger.debug("Attempting credentials update " + user.getUsername());        
    	
		authenticateUserAndSetSession(user, response);
        customUserDetailsService.update(user, newPassword);
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void create(@RequestBody UserDetails user, HttpServletResponse response) {
		
		String userName = user.getUsername();
        
		logger.debug("User signup attempt with username: " + userName);
        
        if(customUserDetailsService.exists(userName)) {
			logger.debug("Duplicate username " + userName);
        	throw new DuplicateUsernameException();
        } else {
        	customUserDetailsService.save(user);
            authenticateUserAndSetSession(user, response);
        }
	}
	
	private void authenticateUserAndSetSession(UserDetails user, HttpServletResponse response) throws BadCredentialsException {

		String userName = user.getUsername();
        String password = user.getPassword();
        
        logger.debug("Authentication session" + userName);
        
        Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
		if (authenticatedUser != null) {
	        TokenAuthenticationService.addAuthentication(response, authenticatedUser.getName());
	        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);			
		} else {
			throw new BadCredentialsException("Authentication failed for user " + userName);
		}
    }
}