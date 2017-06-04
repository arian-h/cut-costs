package com.boot.cut_costs.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.DTO.UserDto;
import com.boot.cut_costs.util.CustomValidationUtil;

@Component
public class SingupUserDtoValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return UserDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDto userDTO = (UserDto)target;
		if (!CustomValidationUtil.validateUserName(userDTO.getName())) {
			errors.rejectValue("name", "Name does not meet expectations");
		}
		if (!CustomValidationUtil.validateImage(userDTO.getImage(), 2)) {
			errors.rejectValue("image", "Image does not meet expectations");
		}
		if (!CustomValidationUtil.validateUserDescription(userDTO.getDescription())){
			errors.rejectValue("description", "Description does not meet expectations");
		}
	}
}
