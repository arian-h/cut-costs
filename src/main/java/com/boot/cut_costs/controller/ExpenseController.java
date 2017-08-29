package com.boot.cut_costs.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.dto.expense.ExpenseDtoConverter;
import com.boot.cut_costs.dto.expense.ExtendedGetExpenseDto;
import com.boot.cut_costs.dto.expense.GetExpenseDto;
import com.boot.cut_costs.dto.expense.PostExpenseDto;
import com.boot.cut_costs.exception.BadRequestException;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.service.ExpenseService;
import com.boot.cut_costs.validator.ExpenseDtoValidator;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
	
	@Autowired
	private ExpenseDtoConverter expenseDtoConverter;
	
	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	private ExpenseDtoValidator expenseDtoValidator;
	
	@RequestMapping(path = "/{expenseId}", method = RequestMethod.GET)
	public ExtendedGetExpenseDto get(@PathVariable long expenseId, Principal principal) {
		Expense expense = expenseService.get(expenseId, principal.getName());
		return expenseDtoConverter.convertToExtendedDto(expense);
	}
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<GetExpenseDto> list(Principal principal) {
		List<Expense> expenses = expenseService.list(principal.getName());
		List<GetExpenseDto> result = new ArrayList<GetExpenseDto>();
		for (Expense expense: expenses) {
			result.add(expenseDtoConverter.convertToDto(expense));
		}
		return result;
	}
	
	@RequestMapping(path = "/{expenseId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable long expenseId, Principal principal) {
		expenseService.delete(expenseId, principal.getName());
	}

	@RequestMapping(path = "/{expenseId}", method = RequestMethod.PUT)
	public void update(@RequestBody PostExpenseDto expenseDto , @PathVariable long expenseId, Principal principal, BindingResult result) throws BadRequestException, IOException {
		expenseDtoValidator.validate(expenseDto, result);
		if (result.hasErrors()) {
			throw new ValidationException();
		}
		expenseService.update(expenseId, expenseDto.getTitle(), expenseDto.getAmount(), expenseDto.getDescription(), expenseDto.getSharers(), expenseDto.getImage(), principal.getName());
	}

	/*
	 * Get all expenses posted to a group
	 */
	@RequestMapping(path = "/{groupId}/expense", method = RequestMethod.GET)
	public List<GetExpenseDto> getAllExpenses(@PathVariable long groupId, Principal principal) {
		List<Expense> expenses = expenseService.listExpenses(groupId, principal.getName());
		List<GetExpenseDto> result = new ArrayList<GetExpenseDto>();
		for (Expense expense: expenses) {
			result.add(expenseDtoConverter.convertToDto(expense));
		}
		return result;
	}

	/*
	 * Add an expense to a group
	 */
	@RequestMapping(path = "/{groupId}/expense", method = RequestMethod.POST)
	public void addExpense(@RequestBody PostExpenseDto expenseDto, @PathVariable long groupId, Principal principal, BindingResult result) throws IOException {
		expenseDtoValidator.validate(expenseDto, result);
		if (result.hasErrors()) {
			throw new ValidationException(result.getFieldError().getCode());
		}
		expenseService.addExpense(expenseDto.getTitle(), expenseDto.getAmount(),
				expenseDto.getDescription(), expenseDto.getSharers(),
				expenseDto.getImage(), principal.getName(), groupId);
	}
}