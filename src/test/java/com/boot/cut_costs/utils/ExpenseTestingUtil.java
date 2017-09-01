package com.boot.cut_costs.utils;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.ExpenseRepository;
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.repository.UserRepository;

@Component
@Transactional
public class ExpenseTestingUtil {

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private UserRepository userRepository;

	public ExpenseTestingUtil() {}

	/**
	 * Create a group object and save the it to the db
	 */
	public Expense createExpense(String title, long amount, String description, String imageId, List<Long> sharers, User owner, long groupId) {
		Expense expense = new Expense();
		Group group = groupRepository.findById(groupId);
		expense.setOwner(owner);
		expense.setTitle(title);
		expense.setDescription(description);
		expense.setImageId(imageId);
		expense.setGroup(group);
		expense.setAmount(amount);
		owner.addExpense(expense);
		List<User> sharerUsers = new ArrayList<User>();
		if (sharers != null && sharers.size() != 0) {
			for (Long sharerId: sharers) {
				if (owner.getId() != sharerId) {
					User sharerUser = userRepository.findById(sharerId);
					sharerUser.addExpense(expense);
					sharerUsers.add(sharerUser);
					// userRepository.save(sharerUser);	
				}
			}
		}
		expense.addShareres(sharerUsers);
		group.addExpense(expense);
		expenseRepository.save(expense);
//		groupRepository.save(group);
//		userRepository.save(owner);
		return expense;
	}
}
