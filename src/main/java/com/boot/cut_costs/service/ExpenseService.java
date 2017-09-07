package com.boot.cut_costs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.utils.CommonUtils;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private UserRepository userRepository;

	private static Logger logger = LoggerFactory.getLogger(ExpenseService.class);

	public Expense get(long expenseId, String username) {
		Expense expense = expenseRepository.findById(expenseId);
		Group group = expense.getGroup();
		groupService.validateMemberAccessToGroup(group, userService.loadByUsername(username));
		return expense;
	}

	/*
	 * Return all expenses for a user
	 */
	public List<Expense> list(String username) {
		User user = userService.loadByUsername(username);
		return user.getAllExpenses();
	}

	/*
	 * Owner of the expense or admin of the group can delete an expense
	 */
	public void delete(long expenseId, String username) {
		User owner = userService.loadByUsername(username);
		Expense expense = expenseRepository.findById(expenseId);
		Group group = expense.getGroup();
		if (!expense.getOwner().equals(owner) && !group.isAdmin(owner)) {
			throw new BadRequestException("User is not allowed to delete the expense as it is not admin of the group or owner of the expense");
		}
		User admin = expense.getOwner();
		admin.removeOwnedExpense(expense);
		for (User sharer: expense.getSharers()) {
			sharer.removeReceivedExpense(expense);
		}
		group.removeExpense(expense);
		groupRepository.save(group);
		group.removeExpense(expense);
		owner.removeOwnedExpense(expense);
		expenseRepository.delete(expense);
		logger.debug("Expense with id " + expenseId + "was deleted");
	}

	/*
	 * Owner of the expense and admin of the group can change it
	 */
	public void update(long expenseId, String title, long amount, String description, List<Long> sharersIds, String image, String username) throws BadRequestException, IOException {
		Expense expense = loadById(expenseId);
		User user = userService.loadByUsername(username);
		if (expense.getOwner().getId() != user.getId() && !expense.getGroup().isAdmin(user)) {
			throw new BadRequestException("User " + user.getId() + " doesn't have edit access to expense with id " + expense.getId());
		}
		List<User> sharers = new ArrayList<User>();
		if (sharersIds != null && sharersIds.size() > 0) {
			for (long sharerId: sharersIds) {
				User sharer = userService.loadById(sharerId);
				groupService.validateMemberAccessToGroup(expense.getGroup(), sharer);
				if (!sharer.getReceivedExpenses().contains(expense)) {
					sharer.addReceivedExpense(expense);					
				}
				sharers.add(sharer);
			}
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

	public void create(String title, long amount, String description, List<Long> sharersIds, String image, long groupId, String username) throws IOException {
		Group group = groupService.loadById(groupId);
		User owner = userService.loadByUsername(username);
		groupService.validateMemberAccessToGroup(group, owner);
		List<User> sharers = new ArrayList<User>();
		Expense expense = new Expense();
		if (sharersIds != null && sharersIds.size() > 0) {
			for (long sharerId: sharersIds) {
				if (sharerId != owner.getId()) {
					User sharer = userService.loadById(sharerId);
					groupService.validateMemberAccessToGroup(group, sharer);
					sharers.add(sharer);
					sharer.addReceivedExpense(expense);
				}
			}
		}
		expense.setTitle(title);
		expense.setAmount(amount);
		expense.setDescription(description);
		expense.setGroup(group);
		expense.setOwner(owner);
		expense.setShareres(sharers);
		String imageId = CommonUtils.decodeBase64AndSaveImage(image);
		if (imageId != null) {
			expense.setImageId(imageId);			
		}
		group.addExpense(expense);
		owner.addOwnedExpense(expense);
		expenseRepository.save(expense);
		logger.debug("Expense with title " + title + " was added to group with id " + groupId + "by user " + username);
	}

	public List<Expense> listExpenses(long groupId, String username) {
		Group group = groupService.loadById(groupId);
		User owner = userService.loadByUsername(username);
		groupService.validateMemberAccessToGroup(group, owner);
		return group.getExpenses();
	}

	public Expense loadById(long expenseId) {
		Expense expense = expenseRepository.findOne(expenseId);
		if (expense == null) {
			throw new ResourceNotFoundException("Expense with id " + expenseId + " was not found");
		}
		return expense;
	}
}