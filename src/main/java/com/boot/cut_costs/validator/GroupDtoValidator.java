package com.boot.cut_costs.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.DTO.GroupDto;
import com.boot.cut_costs.util.CustomValidationUtil;

@Component
public class GroupDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return GroupDto.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		GroupDto groupDTO = (GroupDto)target;
		if (!CustomValidationUtil.validateGroupName(groupDTO.getName())){
			errors.rejectValue("name", "Group name does not meet expectations");
		}
		if (!CustomValidationUtil.validateGroupDescription(groupDTO.getDescription())){
			errors.rejectValue("description", "Group description does not meet expectations");
		}
		if (!CustomValidationUtil.validateImage(groupDTO.getImage(), 2)) {
			errors.rejectValue("image", "Image size does not meet expectations");			
		}
	}
	
}
