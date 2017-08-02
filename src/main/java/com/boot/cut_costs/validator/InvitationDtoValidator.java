package com.boot.cut_costs.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.dto.invitation.PostInvitationDto;
import com.boot.cut_costs.utils.CustomValidationUtils;

public class InvitationDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PostInvitationDto.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		PostInvitationDto invitationDTO = (PostInvitationDto)target;
		if (!CustomValidationUtils.validateInvitationDescription(invitationDTO.getDescription())){
			errors.rejectValue("description", "invitation description does not meet expectations");
		}
	}
	
}