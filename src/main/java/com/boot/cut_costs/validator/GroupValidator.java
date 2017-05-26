package com.boot.cut_costs.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.boot.cut_costs.model.CCGroup;

public class GroupValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CCGroup.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CCGroup group = (CCGroup)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Group name is empty");
		if (!validateName(group.getName())){
			errors.rejectValue("name", "Group name does not meet expectations");
		}
		if (!validateDescription(group.getDescription())){
			errors.rejectValue("description", "Description does not meet expectations");
		}
	}
	
	private boolean validateName(String name) {
		name = name.trim();
		return name.length() >= 5 && name.length() <= 25;
	}

	private boolean validateDescription(String description) {
		if (description == null) {
			return true;
		}
		description = description.trim();
		return description.length() <= 200;
	}

}
