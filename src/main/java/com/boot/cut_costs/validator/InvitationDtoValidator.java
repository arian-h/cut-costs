package com.boot.cut_costs.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.dto.invitation.post.InvitationPostDto;

public class InvitationDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return InvitationPostDto.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		return;
	}
}