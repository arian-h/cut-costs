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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.dto.group.get.GroupExtendedGetDto;
import com.boot.cut_costs.dto.group.get.GroupSnippetGetDto;
import com.boot.cut_costs.dto.group.get.GroupGetDtoConverter;
import com.boot.cut_costs.dto.group.post.GroupPostDto;
import com.boot.cut_costs.dto.user.get.UserGetDtoConverter;
import com.boot.cut_costs.dto.user.get.UserSnippetGetDto;
import com.boot.cut_costs.exception.DuplicateGroupNameException;
import com.boot.cut_costs.exception.InputValidationException;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.service.GroupService;
import com.boot.cut_costs.service.UserService;
import com.boot.cut_costs.validator.GroupDtoValidator;

@RestController
@RequestMapping("/api/group")
public class GroupController {

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private GroupDtoValidator groupDtoValidator;
	
	@Autowired
	private GroupGetDtoConverter groupDtoConverter;
	
	@Autowired
	private UserGetDtoConverter userDtoConverter;

	/*
	 * Create a group
	 */
	@RequestMapping(path = "", method = RequestMethod.POST)
	public GroupSnippetGetDto create(@RequestBody GroupPostDto groupDto, Principal principal, BindingResult result) throws IOException {
		groupDtoValidator.validate(groupDto, result);
		User loggedInUser = userService.loadByUsername(principal.getName());
		if (result.hasErrors()) {
			throw new InputValidationException(result.getFieldError().getField());
		}
		if (groupService.nameIsTaken(groupDto.getName(), loggedInUser.getId())) {
			throw new DuplicateGroupNameException(groupDto.getName());
		}
		Group group = groupService.create(groupDto.getName(), groupDto.getDescription(), principal.getName());
		return groupDtoConverter.convertToDto(group, loggedInUser);
	}

	/*
	 * Update a group with specific id
	 * If user is the group admin
	 */
	@RequestMapping(path = "/{groupId}", method = RequestMethod.PUT)
	public GroupSnippetGetDto update(@RequestBody GroupPostDto groupDto, @PathVariable long groupId, Principal principal, BindingResult result) throws IOException {
		groupDtoValidator.validate(groupDto, result);
		User loggedInUser = userService.loadByUsername(principal.getName());
		if (result.hasErrors()) {
			throw new InputValidationException(result.getFieldError().getField());
		}
		Group group = groupService.update(groupId, groupDto.getName(), groupDto.getDescription(), principal.getName());
		return groupDtoConverter.convertToDto(group, loggedInUser);
	}

	/*
	 * Get information of a specific group
	 */
	@RequestMapping(path = "/{groupId}", method = RequestMethod.GET)
	public GroupExtendedGetDto get(@PathVariable String groupId, Principal principal) {
		Group group = groupService.get(Long.valueOf(groupId), principal.getName());
		User loggedInUser = userService.loadByUsername(principal.getName());
		return groupDtoConverter.convertToExtendedDto(group, loggedInUser);
	}

	/*
	 * Delete a group
	 * If user is the group admin
	 */
	@RequestMapping(path = "/{groupId}", method = RequestMethod.DELETE)
	public long delete(@PathVariable long groupId, Principal principal) {
		return groupService.delete(groupId, principal.getName());
	}

	/*
	 * Get a list of all groups a user is a member of
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<GroupSnippetGetDto> list(Principal principal) {
		List<Group> groups = groupService.list(principal.getName());
		List<GroupSnippetGetDto> result = new ArrayList<GroupSnippetGetDto>();
		User loggedInUser = userService.loadByUsername(principal.getName());
		for (Group group: groups) {
			result.add(groupDtoConverter.convertToDto(group, loggedInUser));
		}
		return result;
	}

	/*
	 * Get all members of a group
	 * If user is a group member
	 */
	@RequestMapping(path = "/{groupId}/user", method = RequestMethod.GET)
	public List<UserSnippetGetDto> listMembers(@PathVariable long groupId, Principal principal) {
		List<User> members = groupService.listMembers(groupId, principal.getName());
		List<UserSnippetGetDto> result = new ArrayList<UserSnippetGetDto>();
		for (User member: members) {
			result.add(userDtoConverter.convertToDto(member));
		}
		return result;
	}

	/*
	 * Delete a specific member of a group. Deleting admin is not possible.
	 * Only enabled for admins
	 */
	@RequestMapping(path = "/{groupId}/user/{userId}", method = RequestMethod.DELETE)
	public long removeMember(@PathVariable long groupId, @PathVariable long userId, Principal principal) {
		return groupService.removeMember(groupId, userId, principal.getName());
	}

	/**
	 * Subscribe to a group
	 * Subscriber should be a group member
	 * @param groupId
	 * @param subscribe whether to subscribe (true) to a group 
	 * or unsubscribe (false) from a group
	 * @param principal
	 */
	@RequestMapping(path = "/{groupId}/subscribe", method = RequestMethod.POST)
	public void subscribe(@PathVariable long groupId, @RequestParam(required=true, value="value") boolean subscribe, Principal principal) {
		groupService.alterSubscription(groupId, subscribe, principal.getName());
	}

}
