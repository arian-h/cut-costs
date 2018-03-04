package com.boot.cut_costs.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.dto.expense.get.ExpenseGetDtoConverter;
import com.boot.cut_costs.dto.expense.get.ExpenseExtendedGetDto;
import com.boot.cut_costs.dto.expense.get.ExpenseSnippetGetDto;
import com.boot.cut_costs.dto.expense.post.ExpensePostDto;
import com.boot.cut_costs.exception.BadRequestException;
import com.boot.cut_costs.exception.InputValidationException;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.service.ExpenseService;
import com.boot.cut_costs.service.UserService;
import com.boot.cut_costs.validator.ExpenseDtoValidator;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
	
	@Autowired
	private ExpenseGetDtoConverter expenseDtoConverter;
	
	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ExpenseDtoValidator expenseDtoValidator;

	/*
	 * Get information for an expense
	 */
	@RequestMapping(path = "/{expenseId}", method = RequestMethod.GET)
	public ExpenseExtendedGetDto get(@PathVariable long expenseId, Principal principal) {
		Expense expense = expenseService.get(expenseId, principal.getName());
		User currentLoggedInUser = userService.loadByUsername(principal.getName());
		return expenseDtoConverter.convertToExtendedDto(expense, currentLoggedInUser);
	}

	/*
	 * List all user expenses
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<ExpenseSnippetGetDto> list(Principal principal) {
		List<Expense> expenses = expenseService.list(principal.getName());
		List<ExpenseSnippetGetDto> result = new ArrayList<ExpenseSnippetGetDto>();
		User loggedInUser = userService.loadByUsername(principal.getName());
		for (Expense expense: expenses) {
			result.add(expenseDtoConverter.convertToDto(expense, loggedInUser));
		}
		return result;
	}
	
	/*
	 * Delete an expense. Only owner of the expense or admin of the group can delete an expense
	 */
	@RequestMapping(path = "/{expenseId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable long expenseId, Principal principal) {
		expenseService.delete(expenseId, principal.getName());
	}

	/*
	 * Update an existing expense
	 */
	@RequestMapping(path = "/{expenseId}", method = RequestMethod.PUT)
	public ExpenseSnippetGetDto update(@RequestBody ExpensePostDto expenseDto , @PathVariable long expenseId, Principal principal, BindingResult result) throws BadRequestException, IOException {
		expenseDtoValidator.validate(expenseDto, result);
		if (result.hasErrors()) {
			throw new InputValidationException(result.getFieldError().getField());
		}
		Expense expense = expenseService.update(expenseId, expenseDto.getTitle(), expenseDto.getAmount(), expenseDto.getDescription(), expenseDto.getSharers(), expenseDto.getImage(), principal.getName());
		User loggedInUser = userService.loadByUsername(principal.getName());
		return expenseDtoConverter.convertToDto(expense, loggedInUser);
	}

	/*
	 * Get all expenses posted to a group
	 */
	@RequestMapping(path = "/{groupId}/expense", method = RequestMethod.GET)
	public List<ExpenseSnippetGetDto> listExpenses(@PathVariable long groupId, Principal principal) {
		List<Expense> expenses = expenseService.listExpenses(groupId, principal.getName());
		List<ExpenseSnippetGetDto> result = new ArrayList<ExpenseSnippetGetDto>();
		User loggedInUser = userService.loadByUsername(principal.getName());
		for (Expense expense: expenses) {
			result.add(expenseDtoConverter.convertToDto(expense, loggedInUser));
		}
		return result;
	}

	/*
	 * Create expense (i.e. create and add an expense to a group)
	 */
	@RequestMapping(path = "/{groupId}", method = RequestMethod.POST)
	public ExpenseSnippetGetDto create(@RequestBody ExpensePostDto expenseDto, @PathVariable long groupId, Principal principal, BindingResult result) throws IOException {
		expenseDtoValidator.validate(expenseDto, result);
		if (result.hasErrors()) {
			throw new InputValidationException(result.getFieldError().getField());
		}
		Expense expense = expenseService.create(expenseDto.getTitle(), expenseDto.getAmount(),
				expenseDto.getDescription(), expenseDto.getSharers(),
				expenseDto.getImage(), groupId, principal.getName());
		User loggedInUser = userService.loadByUsername(principal.getName());
		return expenseDtoConverter.convertToDto(expense, loggedInUser);
	}

	/*
	 * Remove a sharer from an expense
	 */
	@RequestMapping(path = "/{expenseId}/sharer/{sharerId}", method = RequestMethod.DELETE)
	public long removeSharer(@PathVariable long expenseId, @PathVariable long sharerId, Principal principal) throws IOException {
		return expenseService.removeSharer(expenseId, sharerId, principal.getName());
	}

	/*
	 * Remove a sharer from an expense
	 */
	@RequestMapping(path = "/{expenseId}/sharer/{sharerId}", method = RequestMethod.PATCH)
	public long addSharer(@PathVariable long expenseId, @PathVariable long newSharerId, Principal principal) throws IOException {
		return expenseService.addSharer(expenseId, newSharerId, principal.getName());
	}
}