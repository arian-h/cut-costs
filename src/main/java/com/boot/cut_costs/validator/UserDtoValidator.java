package com.boot.cut_costs.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.dto.user.PostUserDto;
import com.boot.cut_costs.utils.CustomValidationUtils;

@Component
public class UserDtoValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return PostUserDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PostUserDto userDTO = (PostUserDto)target;
		if (!CustomValidationUtils.validateUserName(userDTO.getName())) {
			errors.rejectValue("name", "name does not meet expectations");
		}
		if (!CustomValidationUtils.validateUserDescription(userDTO.getDescription())){
			errors.rejectValue("description", "description does not meet expectations");
		}
		if (!CustomValidationUtils.validateImage(userDTO.getImage(), 2)){
			errors.rejectValue("image", "image does not meet expectations");
		}
	}
}
