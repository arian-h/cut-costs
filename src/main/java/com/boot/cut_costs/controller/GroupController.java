package com.boot.cut_costs.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.dto.expense.ExpenseDtoConverter;
import com.boot.cut_costs.dto.expense.GetExpenseDto;
import com.boot.cut_costs.dto.expense.PostExpenseDto;
import com.boot.cut_costs.dto.group.ExtendedGetGroupDto;
import com.boot.cut_costs.dto.group.GetGroupDto;
import com.boot.cut_costs.dto.group.GroupDtoConverter;
import com.boot.cut_costs.dto.group.PostGroupDto;
import com.boot.cut_costs.dto.user.GetUserDto;
import com.boot.cut_costs.dto.user.UserDtoConverter;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.service.GroupService;
import com.boot.cut_costs.service.UserService;
import com.boot.cut_costs.validator.ExpenseDtoValidator;
import com.boot.cut_costs.validator.GroupDtoValidator;

@RestController
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupDtoValidator createGroupDtoValidator;

	@Autowired
	private GroupDtoValidator groupDtoValidator;
	
	@Autowired
	private GroupDtoConverter groupDtoConverter;
	
	@Autowired
	private UserDtoConverter userDtoConverter;
	
	@Autowired
	private ExpenseDtoValidator expenseDtoValidator;
	
	@Autowired
	private ExpenseDtoConverter expenseDtoConverter;
	
	//TODO: return the id for the group created
	@RequestMapping(path = "", method = RequestMethod.POST)
	public void create(@RequestBody PostGroupDto groupDto, Principal principal, BindingResult result) throws IOException {
		createGroupDtoValidator.validate(groupDto, result);
		if (result.hasErrors()) {
			throw new ValidationException(result.getFieldError().getCode());
		}
		groupService.create(groupDto.getName(), groupDto.getDescription(), groupDto.getImage(), principal.getName());
	}

	@RequestMapping(path = "/{groupId}", method = RequestMethod.PUT)
	public void update(@RequestBody PostGroupDto groupDto, @PathVariable long groupId, Principal principal, BindingResult result) throws IOException {
		groupDtoValidator.validate(groupDto, result);
		if (result.hasErrors()) {
			throw new ValidationException(result.getFieldError().getCode());
		}
		groupService.update(groupId, groupDto.getName(), groupDto.getDescription(), groupDto.getImage(), principal.getName());
	}

	/**
	 * get information about a specific group
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(path = "/{groupId}", method = RequestMethod.GET)
	public ExtendedGetGroupDto get(@PathVariable long groupId, Principal principal) {
		Group group = groupService.get(groupId, principal.getName());
		return groupDtoConverter.convertToExtendedDto(group);
	}
	
	@RequestMapping(path = "/{groupId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable long groupId, Principal principal) {
		groupService.delete(groupId, principal.getName());
	}

	/**
	 * get list of all groups user is member of
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public Set<GetGroupDto> getAllGroups(Principal principal) {
		Set<Group> groups = groupService.list(principal.getName());
		Set<GetGroupDto> result = new HashSet<GetGroupDto>();
		for (Group group: groups) {
			result.add(groupDtoConverter.convertToDto(group));
		}
		return result;
	}
	
	@RequestMapping(path = "/{groupId}/user", method = RequestMethod.GET)
	public Set<GetUserDto> getAllMembers(@PathVariable long groupId, Principal principal) {
		Set<User> members = groupService.listMembers(groupId, principal.getName());
		Set<GetUserDto> result = new HashSet<GetUserDto>();
		for (User member: members) {
			result.add(userDtoConverter.convertToDto(member));
		}
		return result;
	}

	@RequestMapping(path = "/{groupId}/user/{userId}", method = RequestMethod.DELETE)
	public void deleteMember(@PathVariable long groupId, @PathVariable long userId, Principal principal) {
		groupService.removeMember(groupId, userId, principal.getName());
	}
	
	@RequestMapping(path = "/{groupId}/expense", method = RequestMethod.GET)
	public Set<GetExpenseDto> getAllExpenses(@PathVariable long groupId, Principal principal) {
		Set<Expense> expenses = groupService.listExpenses(groupId, principal.getName());
		Set<GetExpenseDto> result = new HashSet<GetExpenseDto>();
		for (Expense expense: expenses) {
			result.add(expenseDtoConverter.convertToDto(expense));
		}
		return result;
	}
	
	@RequestMapping(path = "/{groupId}/expense", method = RequestMethod.POST)
	public void addExpense(@RequestBody PostExpenseDto expenseDto, @PathVariable long groupId, Principal principal, BindingResult result) throws IOException {
		expenseDtoValidator.validate(expenseDto, result);
		if (result.hasErrors()) {
			throw new ValidationException(result.getFieldError().getCode());
		}
		groupService.addExpense(expenseDto.getTitle(), expenseDto.getAmount(),
				expenseDto.getDescription(), expenseDto.getSharers(),
				expenseDto.getImage(), principal.getName(), groupId);
	}
}
