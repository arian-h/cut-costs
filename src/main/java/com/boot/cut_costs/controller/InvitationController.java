package com.boot.cut_costs.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;
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
import com.boot.cut_costs.service.InvitationService;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

	@Autowired
	private InvitationService invitationService;

	@Autowired
	private InvitationDtoConverter invitationDtoConverter;

	@RequestMapping(path = "/{invitationId}", method = RequestMethod.GET)
	public GetInvitationDto get(@PathVariable long invitationId, Principal principal, BindingResult result) throws IOException {
		return invitationDtoConverter
				.convertToDto(invitationService.get(invitationId, principal.getName()));
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public Set<GetInvitationDto> list(Principal principal, BindingResult result) throws IOException {
		return invitationService
				.list(principal.getName())
				.stream()
				.map(invitation -> invitationDtoConverter.convertToDto(invitation))
						.collect(Collectors.toSet());
	}
	
	@RequestMapping(path = "/{invitationId}/accept", method = RequestMethod.GET)
	public void accept(@PathVariable long invitationId, Principal principal, BindingResult result) throws IOException {
		invitationService.accept(invitationId, principal.getName());
	}

	@RequestMapping(path = "/{invitationId}/reject", method = RequestMethod.GET)
	public void reject(@PathVariable long invitationId, Principal principal, BindingResult result) throws IOException {
		invitationService.reject(invitationId, principal.getName());
	}
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public void create(@RequestBody PostInvitationDto invitationDto, Principal principal, BindingResult result) throws IOException {
		invitationService.create(invitationDto.getGroup(), invitationDto.getInvitee(), principal.getName(), invitationDto.getDescription());
	}
}