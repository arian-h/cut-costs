package com.boot.cut_costs.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.boot.cut_costs.exception.BadRequestException;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.ExpenseRepository;
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.utils.CommonUtils;
import com.boot.cut_costs.utils.AWS.FILE_TYPE;
import com.boot.cut_costs.utils.AWS.S3Services;

@Service
@Transactional
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

	@Autowired
	private S3Services s3Services;

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
	public Expense update(long expenseId, String title, long amount, String description, List<Long> sharersIds, MultipartFile image, String username) throws BadRequestException, IOException {
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
		File convFile = CommonUtils.convertToFile(FILE_TYPE.EXPENSE_PHOTO, image);
		String imageId = s3Services.uploadFile(FILE_TYPE.EXPENSE_PHOTO, convFile);
		if (imageId != null) {
			s3Services.deleteFile(FILE_TYPE.EXPENSE_PHOTO, expense.getImageId());
			expense.setImageId(imageId);
		}
		expenseRepository.save(expense);
		logger.debug("Expense with title " + title + " was updated by user " + username);
		return expense;
	}

	public Expense create(String title, long amount, String description, List<Long> sharersIds, MultipartFile image, long groupId, String username) throws IOException {
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
				}
			}
		}
		for (User sharer: sharers) {
			sharer.addReceivedExpense(expense);
		}
		expense.setTitle(title);
		expense.setAmount(amount);
		expense.setDescription(description);
		expense.setGroup(group);
		expense.setOwner(owner);
		expense.setShareres(sharers);
		File convFile = CommonUtils.convertToFile(FILE_TYPE.EXPENSE_PHOTO, image);
		String imageId = s3Services.uploadFile(FILE_TYPE.EXPENSE_PHOTO, convFile);
		if (imageId != null) {
			expense.setImageId(imageId);
		}
		group.addExpense(expense);
		owner.addOwnedExpense(expense);
		expenseRepository.save(expense);
		logger.debug("Expense with title " + title + " was added to group with id " + groupId + "by user " + username);
		return expense;
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

	public long removeSharer(long expenseId, long sharerId, String username) {
		Expense expense = this.loadById(expenseId);
		User user = userService.loadByUsername(username);
		validateOwnerAccessToExpense(expense, user);
		User sharer = userService.loadById(sharerId);
		if (!expense.hasSharer(sharer)) {
			throw new BadRequestException("Expense with id " + expense.getId() + " is not shared with user with id " + sharer.getId());
		}
		expense.removeSharer(sharer);
		expenseRepository.save(expense);
		logger.debug("User with id " + sharerId + " was deleted from expense with id" + expenseId);
		return sharerId;
	}

	public User addSharer(long expenseId, long sharerId, String username) {
		Expense expense = this.loadById(expenseId);
		User user = userService.loadByUsername(username);
		validateOwnerAccessToExpense(expense, user);
		User newSharer = userService.loadById(sharerId);
		Group group = groupService.loadById(expense.getGroup().getId());
		groupService.validateMemberAccessToGroup(group, newSharer);
		if (expense.hasSharer(newSharer)) {
			throw new BadRequestException("Expense with id " + expense.getId() + " is already shared with user with id " + newSharer.getId());
		}
		expense.addSharer(newSharer);
		expenseRepository.save(expense);
		logger.debug("User with id " + sharerId + " was added to the expense with id" + expenseId);
		return newSharer;
	}

	public void validateOwnerAccessToExpense(Expense expense, User user) {
		if (expense.getOwner().getId() != user.getId()) {
			throw new AccessDeniedException("User with id " + user.getId() + " is not owner of the expense with id " + expense.getId()); 
		}
	}
}