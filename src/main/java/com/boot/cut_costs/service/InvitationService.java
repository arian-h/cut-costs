package com.boot.cut_costs.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.exception.BadRequestException;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.Invitation;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.InvitationRepository;

@Service
public class InvitationService {

	@Autowired
	private InvitationRepository invitationRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
	private static Logger logger = LoggerFactory.getLogger(InvitationService.class);

	public Invitation create(long groupId, long inviteeId, String inviterUsername) {
		User inviter = userService.loadByUsername(inviterUsername);
		Group group = groupService.loadById(groupId);
		groupService.validateMemberAccessToGroup(group, inviter);
		User invitee = userService.loadById(inviteeId);
		if (groupService.isMemberOrAdmin(group, invitee)) {
			throw new BadRequestException("User with id " + inviteeId + " is already a member of group with id " + groupId);
		}
		Invitation invitation = new Invitation();
		invitation.setGroup(group);
		invitation.setInvitee(invitee);
		invitation.setInviter(inviter);
		invitee.addReceivedInvitation(invitation);
		inviter.addOwnedInvitation(invitation);
		invitationRepository.save(invitation);
		logger.debug("invitation ");
		return invitation;
	}
	
	public List<Invitation> list(String username) {
		User user = userService.loadByUsername(username);
		return user.getReceivedInvitations();
	}
	
	public Invitation loadById(long invitationId) {
		Invitation invitation = invitationRepository.findById(invitationId);
		if (invitation == null) {
			throw new ResourceNotFoundException("Invitation with id " + invitationId + " was not found");
		}
		return invitation;
	}
	
	public void accept(long invitationId, String username) {
		Invitation invitation = this.loadById(invitationId);
		User invitee = userService.loadByUsername(username);
		validateAccessToInvitation(invitation, invitee);
		Group group = invitation.getGroup();
		groupService.addMember(group.getId(), invitee.getId());
		invitee.addMemberGroup(group);
		invitee.removeReceivedInvitation(invitation);
		User inviter = userService.loadById(invitation.getInviter().getId());
		inviter.removeOwnedInvitation(invitation);
		invitationRepository.delete(invitation);
	}
	
	public void reject(long invitationId, String username) {
		Invitation invitation = this.loadById(invitationId);
		User invitee = userService.loadByUsername(username);
		validateAccessToInvitation(invitation, invitee);
		invitee.removeReceivedInvitation(invitation);
		User inviter = userService.loadById(invitation.getInviter().getId());
		inviter.removeOwnedInvitation(invitation);
		invitationRepository.delete(invitation);
	}
	
	//check if user is invitee of the invitation
	private void validateAccessToInvitation(Invitation invitation, User user) {
		if (!invitation.getInvitee().equals(user)) {
			throw new AccessDeniedException("User with id " + user.getId() + " does not have access to invitation with id " + invitation.getId()); 
		}
	}
}