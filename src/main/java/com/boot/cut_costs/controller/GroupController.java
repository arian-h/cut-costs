package com.boot.cut_costs.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.DTO.GroupDto;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.service.GroupService;
import com.boot.cut_costs.service.UserService;
import com.boot.cut_costs.validator.GroupDtoValidator;

@RestController
@RequestMapping("/group")
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private GroupDtoValidator groupDtoValidator;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path="",method=RequestMethod.POST)
	public void create(@RequestBody GroupDto groupDto, Principal principal, BindingResult result) throws IOException {
		groupDtoValidator.validate(groupDto, result);
		if (result.hasErrors()) {
			throw new ValidationException();
		}
		groupService.create(groupDto, principal.getName());
	}
	
	@RequestMapping(path="/{groupId}",method=RequestMethod.DELETE)
	public void delete(@PathVariable long groupId, Principal principal) {
		groupService.delete(groupId, principal.getName());
	}
	
	@RequestMapping(path="/{groupId}",method=RequestMethod.PUT)
	public void update(@RequestBody GroupDto groupDto, @PathVariable long groupId, Principal principal, BindingResult result) throws IOException {
		groupDtoValidator.validate(groupDto, result);
		if (result.hasErrors()) {
			throw new ValidationException();
		}
		groupService.update(groupId, groupDto, principal.getName());
	}

	/*
	 * get information about a specific group
	 */
	@RequestMapping(path="/{groupId}",method=RequestMethod.GET)
	public Group get(@PathVariable long groupId, Principal principal) {
		return groupService.get(groupId, principal.getName());
	}
	
	/*
	 * get list of all groups user is member of
	 */
	@RequestMapping(path="",method=RequestMethod.GET)
	public Set<Group> list(Principal principal) {
		return groupService.list(principal.getName());
	}
	
	@RequestMapping(path="/{groupId}/user",method=RequestMethod.GET)
	public Set<User> listMembers(@PathVariable long groupId, Principal principal) {
		return groupService.listMembers(groupId, principal.getName());
	}
	
	@RequestMapping(path="/{groupId}/user/{userId}",method=RequestMethod.GET)
	public void deleteMember(@PathVariable long groupId, @PathVariable long userId, Principal principal) {
		groupService.deleteMember(groupId, userId, principal.getName());
	}
}
