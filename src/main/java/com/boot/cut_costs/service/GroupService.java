package com.boot.cut_costs.service;

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

import com.boot.cut_costs.exception.BadRequestException;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.utils.CommonUtils;

@Service
@Transactional
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private ExpenseService expenseService;
	
	private static Logger logger = LoggerFactory.getLogger(GroupService.class);
	
	public Group create(String groupName, String description, String image, String username) throws IOException {
		User user = userService.loadByUsername(username);
		Group group = new Group();
		group.setAdmin(user);
		group.setName(groupName);
		group.setDescription(description);
		String imageId = CommonUtils.decodeBase64AndSaveImage(image);
		if (imageId != null) {
			group.setImageId(imageId);
		}
		user.addOwnedGroup(group);
		groupRepository.save(group);
		logger.debug("Group with name " + group.getName() + " and id " + group.getId() + " was created");
		return group;
	}

	public void delete(long groupId, String username) {
		Group group = this.loadById(groupId);
		User owner = userService.loadByUsername(username);
		validateAdminAccessToGroup(group, owner);
		owner.removeOwnedGroup(group);
		for (User user: group.getMembers()) {
			user.removeMemberGroup(group);
		}
		List<Long> expenseIds = new ArrayList<Long>();
		//TODO fix here
		for (Expense e: group.getExpenses()) {
			expenseIds.add(e.getId());
		}
		for (Long expenseId: expenseIds) {
			expenseService.delete(expenseId, username);
		}
		groupRepository.delete(groupId);
		logger.debug("Group with id " + groupId + " was deleted");
	}

	public Group update(long groupId, String groupName, String description, String image, String username) throws IOException {
		Group group = this.loadById(groupId);
		User user = userService.loadByUsername(username);
		validateAdminAccessToGroup(group, user);
		group.setName(groupName);
		group.setDescription(description);
		String imageId = CommonUtils.decodeBase64AndSaveImage(image);
		if (imageId != null) {
			group.setImageId(imageId);
		}
		groupRepository.save(group);
		logger.debug("Group with id " + groupId + " was updated");
		return group;
	}
	
	public Group get(long groupId, String username) {
		Group group = this.loadById(groupId);
		User user = userService.loadByUsername(username);
		validateMemberAccessToGroup(group, user);
		return group;
	}
	
	public List<Group> list(String username) {
		User user = userService.loadByUsername(username);
		return user.getAllGroups();
	}

	public List<User> listMembers(long groupId, String username) {
		Group group = this.loadById(groupId);
		User user = userService.loadByUsername(username);
		validateMemberAccessToGroup(group, user);
		List<User> result = new ArrayList<User>();
		result.add(group.getAdmin());
		result.addAll(group.getMembers());
		return result;
	}
	
	public void removeMember(long groupId, long userId, String username) {
		Group group = this.loadById(groupId);
		User user = userService.loadByUsername(username);
		validateAdminAccessToGroup(group, user);
		User target = userService.loadById(userId);
		validateMemberAccessToGroup(group, target);
		if (group.isAdmin(target)) {
			throw new BadRequestException("Group admin cannot delete himself/herself from the group");
		}
		group.removeMember(target);
		user.removeMemberGroup(group);
		groupRepository.save(group);
	}

	public void addMember(long groupId, long userId) {
		Group group = this.loadById(groupId);
		User user = userService.loadById(userId);
		if (this.isMemberOrAdmin(group, user)) {
			throw new BadRequestException("User with id " + userId + " is already a member of group with id " + groupId);
		}
		group.addMember(user);
		groupRepository.save(group);
	}
	
	public Group loadById(long groupId) {
		Group group = groupRepository.findOne(groupId);
		if (group == null) {
			throw new ResourceNotFoundException("Group with id " + groupId + " was not found");
		}
		return group;
	}
	
	public void validateAdminAccessToGroup(Group group, User user) {
		if (!group.isAdmin(user)) {
			throw new AccessDeniedException("User with id " + user.getId() + " does not have admin access to group with id " + group.getId()); 
		}
	}
	
	public void validateMemberAccessToGroup(Group group, User user) {
		if (!this.isMemberOrAdmin(group, user)) {
			throw new AccessDeniedException("User with id " + user.getId() + " does not have access to group with id " + group.getId()); 
		}
	}
	
	public boolean isMemberOrAdmin(Group group, User user) {
		return group.isMember(user) || group.isAdmin(user);
	}
}