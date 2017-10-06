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

import com.boot.cut_costs.dto.invitation.GetInvitationDto;
import com.boot.cut_costs.dto.invitation.InvitationDtoConverter;
import com.boot.cut_costs.dto.invitation.PostInvitationDto;
import com.boot.cut_costs.model.Invitation;
import com.boot.cut_costs.service.InvitationService;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

	@Autowired
	private InvitationService invitationService;

	@Autowired
	private InvitationDtoConverter invitationDtoConverter;

	@RequestMapping(path = "", method = RequestMethod.POST)
	public GetInvitationDto create(@RequestBody PostInvitationDto invitationDto, Principal principal, BindingResult result) throws IOException {
		Invitation invitation = invitationService.create(invitationDto.getGroupId(), invitationDto.getInviteeId(), principal.getName());
		return invitationDtoConverter.convertToDto(invitation);
	}

	/*
	 * List all invitations a user received
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<GetInvitationDto> list(Principal principal) throws IOException {
		return invitationService
				.list(principal.getName())
				.stream()
				.map(invitation -> invitationDtoConverter.convertToDto(invitation))
						.collect(Collectors.toList());
	}
	
	@RequestMapping(path = "/{invitationId}/accept", method = RequestMethod.GET)
	public void accept(@PathVariable long invitationId, Principal principal) throws IOException {
		invitationService.accept(invitationId, principal.getName());
	}

	@RequestMapping(path = "/{invitationId}/reject", method = RequestMethod.GET)
	public void reject(@PathVariable long invitationId, Principal principal) throws IOException {
		invitationService.reject(invitationId, principal.getName());
	}
}