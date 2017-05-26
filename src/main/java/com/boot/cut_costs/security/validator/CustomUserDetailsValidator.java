package com.boot.cut_costs.security.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import com.boot.cut_costs.security.model.CustomUserDetails;

@Component
public class CustomUserDetailsValidator implements Validator {
	
	/* 
	 * at least one uppercase, one lowercase, one digit and no special characters
	 * and size between 6 and 20 
	 */
	private final static String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9]{6,20}$"; 
	/* 
	 * all alphanumeric character (starts with alphabet), with at most one period (dot) allowed in the middle
	 * and size between 8 and 15
	*/
	private final static String USERNAME_PATTERN = "^(?!.*\\..*\\..*)[A-Za-z]([A-Za-z0-9.]*[A-Za-z0-9]){8,15}$";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CustomUserDetails.class.equals(clazz);
	}

	/*
	 * use message code instead of message
	 */
	@Override
	public void validate(Object target, Errors errors) {
		CustomUserDetails userDetails = (CustomUserDetails)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Username is empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Password is empty");
		if (!validateUsername(userDetails.getUsername())){
			errors.rejectValue("username", "Username does not meet expectations");
		}
		if (!validatePassword(userDetails.getPassword())){
			errors.rejectValue("password", "Password does not meet expectations");
		}
	}
	
	private boolean validateUsername(String username){
		return username.matches(USERNAME_PATTERN);
	}
	
	private boolean validatePassword(String password) {
		return password.matches(PASSWORD_PATTERN);
	}

}
