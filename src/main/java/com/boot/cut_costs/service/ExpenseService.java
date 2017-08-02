package com.boot.cut_costs.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.exception.BadRequestException;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.ExpenseRepository;
import com.boot.cut_costs.utils.CommonUtils;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	private static Logger logger = LoggerFactory.getLogger(ExpenseService.class);

	public Expense get(long expenseId, String username) {
		Expense expense = expenseRepository.findById(expenseId);
		Group group = expense.getGroup();
		groupService.validateMemberAccessToGroup(group, userService.loadByUsername(username));
		return expense;
	}

	public Set<Expense> list(String username) {
		User user = userService.loadByUsername(username);
		Set<Expense> expenses = new HashSet<Expense>();
		expenses.addAll(user.getReceivedExpenses());
		expenses.addAll(user.getOwnedExpenses());
		return expenses;
	}

	//only owner or admin of the group can delete an expense
	public void delete(long expenseId, String username) {
		User user = userService.loadByUsername(username);
		Expense expense = expenseRepository.findById(expenseId);
		Group group = expense.getGroup();
		if (!expense.getOwner().equals(user) && !group.isAdmin(user)) {
			throw new BadRequestException("User is not admin of the group or owner of the expense");
		}
		expenseRepository.delete(expense);
		logger.debug("Expense with id " + expenseId + "was deleted");
	}

	//only owner of an expense can update it
	public void update(long expenseId, String title, long amount, String description, Set<Long> sharersIds, String image, String username) throws BadRequestException, IOException {
		Expense expense = loadById(expenseId);
		User owner = userService.loadByUsername(username);
		if (!expense.getOwner().equals(owner)) {
			throw new BadRequestException("User " + owner.getId() + " doesn't have edit access to expense with id " + expense.getId());
		}
		Set<User> sharers = new HashSet<User>();
		for (long sharerId: sharersIds) {
			User sharer = userService.loadById(sharerId);
			groupService.validateMemberAccessToGroup(expense.getGroup(), sharer);
			sharers.add(sharer);
		}
		expense.setAmount(amount);
		expense.setDescription(description);
		expense.setTitle(title);
		expense.setShareres(sharers);
		String imageId = CommonUtils.decodeBase64AndSaveImage(image);
		if (imageId != null) {
			expense.setImageId(imageId);			
		}
		expenseRepository.save(expense);
		logger.debug("Expense with title " + title + " was updated by user " + username);
	}
	
	public Expense loadById(long expenseId) {
		Expense expense = expenseRepository.findOne(expenseId);
		if (expense == null) {
			throw new ResourceNotFoundException("Expense with id " + expenseId + " was not found");
		}
		return expense;
	}
	
	
}
