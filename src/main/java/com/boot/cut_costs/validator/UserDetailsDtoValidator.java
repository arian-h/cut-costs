package com.boot.cut_costs.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.dto.UserDetailsDto;
import com.boot.cut_costs.utils.CustomValidationUtils;

@Component
public class UserDetailsDtoValidator implements Validator {
		
	@Override
	public boolean supports(Class<?> clazz) {
		return UserDetailsDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDetailsDto userDetailsDTO = (UserDetailsDto)target;
		if (!CustomValidationUtils.validateUserName(userDetailsDTO.getName())){
			errors.rejectValue("name", "name does not meet expectations");
		}
		if (!CustomValidationUtils.validatePassword(userDetailsDTO.getPassword())){
			errors.rejectValue("password", "password does not meet expectations");
		}
		if (!CustomValidationUtils.validateEmail(userDetailsDTO.getUsername())){
			errors.rejectValue("username", "username must be valid a valid email address");
		}
	}
}
