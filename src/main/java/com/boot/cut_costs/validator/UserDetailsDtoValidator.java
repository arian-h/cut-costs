package com.boot.cut_costs.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.DTO.UserDetailsDto;
import com.boot.cut_costs.util.CustomValidationUtil;

@Component
public class UserDetailsDtoValidator implements Validator {
		
	@Override
	public boolean supports(Class<?> clazz) {
		return UserDetailsDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDetailsDto userDetailsDTO = (UserDetailsDto)target;
		if (!CustomValidationUtil.validateUserName(userDetailsDTO.getName())){
			errors.rejectValue("name", "Name does not meet expectations");
		}
		if (!CustomValidationUtil.validatePassword(userDetailsDTO.getPassword())){
			errors.rejectValue("password", "Password does not meet expectations");
		}
		if (!CustomValidationUtil.validateEmail(userDetailsDTO.getUsername())){
			errors.rejectValue("username", "Invalid email address for username");
		}
	}
}
