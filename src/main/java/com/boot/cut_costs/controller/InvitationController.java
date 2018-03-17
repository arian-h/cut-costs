package com.boot.cut_costs.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.dto.group.get.GroupGetDtoConverter;
import com.boot.cut_costs.dto.group.get.GroupSnippetGetDto;
import com.boot.cut_costs.dto.invitation.get.InvitationSnippetGetDto;
import com.boot.cut_costs.dto.invitation.get.InvitationGetDtoConverter;
import com.boot.cut_costs.dto.invitation.post.InvitationPostDto;
import com.boot.cut_costs.model.Invitation;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.service.InvitationService;
import com.boot.cut_costs.service.UserService;

@RestController
@RequestMapping("/api/invitation")
public class InvitationController {

	@Autowired
	private InvitationService invitationService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private InvitationGetDtoConverter invitationDtoConverter;

	@Autowired
	private GroupGetDtoConverter groupDtoConverter;

	@RequestMapping(path = "", method = RequestMethod.POST)
	public InvitationSnippetGetDto create(@RequestBody InvitationPostDto invitationDto, Principal principal, BindingResult result) throws IOException {
		Invitation invitation = invitationService.create(invitationDto.getGroupId(), invitationDto.getInviteeId(), principal.getName());
		User currentLoggedInUser = userService.loadByUsername(principal.getName());
		return invitationDtoConverter.convertToDto(invitation, currentLoggedInUser);
	}

	/*
	 * List all invitations a user received
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<InvitationSnippetGetDto> list(Principal principal) throws IOException {
		User currentLoggedInUser = userService.loadByUsername(principal.getName());
		return invitationService
				.list(principal.getName())
				.stream()
				.map(invitation -> invitationDtoConverter.convertToDto(invitation, currentLoggedInUser))
						.collect(Collectors.toList());
	}
	
	@RequestMapping(path = "/{invitationId}/accept", method = RequestMethod.POST)
	public GroupSnippetGetDto accept(@PathVariable long invitationId, Principal principal) throws IOException {
		User currentLoggedInUser = userService.loadByUsername(principal.getName());
		return groupDtoConverter.convertToDto(invitationService.accept(invitationId, principal.getName()), currentLoggedInUser);
	}

	@RequestMapping(path = "/{invitationId}/reject", method = RequestMethod.POST)
	public void reject(@PathVariable long invitationId, Principal principal) throws IOException {
		invitationService.reject(invitationId, principal.getName());
	}
}