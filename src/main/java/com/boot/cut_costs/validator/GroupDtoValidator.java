package com.boot.cut_costs.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.dto.group.PostGroupDto;
import com.boot.cut_costs.utils.CustomValidationUtils;

@Component
public class GroupDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PostGroupDto.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		PostGroupDto groupDTO = (PostGroupDto)target;
		if (!CustomValidationUtils.validateGroupName(groupDTO.getName())){
			errors.rejectValue("name", "group name does not meet expectations");
		}
		if (!CustomValidationUtils.validateGroupDescription(groupDTO.getDescription())){
			errors.rejectValue("description", "group description does not meet expectations");
		}
	}
	
}
