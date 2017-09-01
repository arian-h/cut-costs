package com.boot.cut_costs.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.boot.cut_costs.dto.expense.PostExpenseDto;
import com.boot.cut_costs.utils.CustomValidationUtils;

@Component
public class ExpenseDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PostExpenseDto.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		PostExpenseDto expenseDTO = (PostExpenseDto)target;
		if (!CustomValidationUtils.validateExpenseTitle(expenseDTO.getTitle())){
			errors.rejectValue("title", "expense title does not meet expectations");
		}
		if (!CustomValidationUtils.validateExpenseAmount(expenseDTO.getAmount())){
			errors.rejectValue("amount", "expense amount does not meet expectations");
		}
		if (!CustomValidationUtils.validateImage(expenseDTO.getImage(), 2)){
			errors.rejectValue("image", "image does not meet expectations");
		}
//		if (!CustomValidationUtils.validateSharers(expenseDTO.getSharers())) {
//			errors.rejectValue("sharers", "sharers do not meet expectations");
//		}
	}
	
}