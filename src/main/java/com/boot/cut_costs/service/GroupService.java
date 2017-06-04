package com.boot.cut_costs.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.DTO.GroupDto;
import com.boot.cut_costs.exception.BadRequestException;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.util.ImageUtils;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired 
	private UserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(GroupService.class);

	private static enum GroupAcessLevel {
		MEMBER, ADMIN;
	}
	
	@Transactional
	public void create(GroupDto groupDto, String username) throws IOException {
		User user = userService.getUserByUsername(username);
		Group group = new Group();
		convertGroupDtoToGroup(groupDto, group, user);
		groupRepository.save(group);
		logger.debug("Group with name " + group.getName() + " was created");
	}
	
	@Transactional
	public void delete(long groupId, String username) {
		validateAccessToGroup(groupId, username, GroupAcessLevel.ADMIN);
		groupRepository.delete(groupId);
		logger.debug("Group with id " + groupId + " was deleted");
	}

	@Transactional
	public void update(long groupId, GroupDto groupDto, String username) throws IOException {
		validateAccessToGroup(groupId, username, GroupAcessLevel.ADMIN);		
		Group group = loadGroupById(groupId);
		validateAccessToGroup(groupId, username, GroupAcessLevel.ADMIN);
		convertGroupDtoToGroup(groupDto, group, null);
		logger.debug("Group with id " + groupId + " was updated");
	}
	
	public Group get(long groupId, String username) {
		validateAccessToGroup(groupId, username, GroupAcessLevel.MEMBER);
		return loadGroupById(groupId);
	}
	
	public Set<Group> list(String username) {
		User user = userService.getUserByUsername(username);
		Set<Group> result = new HashSet<Group>();
		result.addAll(user.getMemberGroups());
		result.addAll(user.getOwnedGroups());
		return result;
	}
	
	public Set<User> listMembers(long groupId, String username) {
		validateAccessToGroup(groupId, username, GroupAcessLevel.MEMBER);
		Group group = loadGroupById(groupId);
		Set<User> result = new HashSet<User>();
		result.addAll(group.getMembers());
		result.add(group.getAdmin());
		return result;
	}
	
	public void deleteMember(long groupId, long userId, String username) {
		validateAccessToGroup(groupId, username, GroupAcessLevel.ADMIN);
		Group group = loadGroupById(groupId);
		User user = userService.load(userId);
		validateAccessToGroup(groupId, user.getName(), GroupAcessLevel.MEMBER);
		if (group.getAdmin().getId() == user.getId()) {
			throw new BadRequestException("Group admin cannot delete himself/herself from the group");
		}
		group.getMembers().remove(user);
	}
	
	public Group loadGroupById(long groupId) {
		Group group = groupRepository.findOne(groupId);
		if (group == null) {
			throw new ResourceNotFoundException("Group with id " + groupId + " was not found");
		}
		return group;
	}
	
	/*
	 * Check if user has the required access to a group
	 */
	private void validateAccessToGroup(long groupId, String username, GroupAcessLevel access) {
		User user = userService.getUserByUsername(username);
		Group group = loadGroupById(groupId);
		Set<User> members = group.getMembers();
		if (access == GroupAcessLevel.ADMIN) {
			if (group.getAdmin().getId() != user.getId()) {
				throw new AccessDeniedException("User with id " + user.getId() + " was denied to access group with id " + group.getId() + " as an admin"); 
			}			
		} else {
			if (group.getAdmin().getId() != user.getId() && (members == null || !members.contains(user))) {
				throw new AccessDeniedException("User with id " + user.getId() + " was denied to access group with id " + group.getId()); 
			}			
		}
	}
	
	private void convertGroupDtoToGroup(GroupDto groupDTO, Group group, User admin) throws IOException {
		String description = groupDTO.getDescription();
		String name = groupDTO.getName();
		String image = groupDTO.getImage();
		if (description != null) {
			group.setDescription(description);
		}
		if (name != null) {
			group.setName(name);
		}
		if (image != null) {
			String imageId = ImageUtils.decodeBase64AndSaveImage(image);
			group.setImageId(imageId);
		}
		if (admin != null) {
			group.setAdmin(admin);			
		}
	}

}
