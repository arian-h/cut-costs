package com.boot.cut_costs.service;

import java.util.List;

import javax.transaction.Transactional;

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
import com.boot.cut_costs.repository.GroupRepository;
import com.boot.cut_costs.repository.InvitationRepository;
import com.boot.cut_costs.repository.UserRepository;

@Service
@Transactional
public class InvitationService {

	@Autowired
	private InvitationRepository invitationRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static Logger logger = LoggerFactory.getLogger(InvitationService.class);

	public Invitation create(long groupId, long inviteeId, String inviterUsername) {
		User inviter = userService.loadByUsername(inviterUsername);
		Group group = groupService.loadById(groupId);
		groupService.validateMemberAccessToGroup(group, inviter);
		long inviterId = inviter.getId();
		if (inviterId == inviteeId) {
			throw new BadRequestException("User with id " + inviterId + " cannot invite themself to the group");
		}
		User invitee = userService.loadById(inviteeId);
		if (groupService.isMemberOrAdmin(group, invitee)) {
			throw new BadRequestException("User with id " + inviteeId + " is already a member of group with id " + groupId);
		}
		if (alreadyInvited(inviterId, inviteeId, groupId)) {
			throw new BadRequestException("User with id " + inviterId + " already invited user with id " + inviteeId + " to the group with id " + groupId);
		}
		Invitation invitation = new Invitation();
		invitation.setGroup(group);
		invitation.setInvitee(invitee);
		invitation.setInviter(inviter);
		invitee.addReceivedInvitation(invitation);
		inviter.addOwnedInvitation(invitation);
		invitationRepository.save(invitation);
		logger.debug("invitation was created");
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
	
	public Group accept(long invitationId, String username) {
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
		return group;
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
	
	/*
	 * if user is invitee of the invitation
	 */
	private void validateAccessToInvitation(Invitation invitation, User user) {
		if (!invitation.getInvitee().equals(user)) {
			throw new AccessDeniedException("User with id " + user.getId() + " does not have access to invitation with id " + invitation.getId()); 
		}
	}
	
	/*
	 * only admin of the group while deleting a group can delete an invitation
	 */
	public void delete(long invitationId, String username) {
		User admin = userService.loadByUsername(username);
		Invitation invitation = invitationRepository.findById(invitationId);
		Group group = invitation.getGroup();
		if (!group.isAdmin(admin)) {
			throw new BadRequestException("User is not allowed to delete the invitation as it is not admin of the group");
		}
		User invitee = invitation.getInvitee();
		invitee.removeReceivedInvitation(invitation);
		userRepository.save(invitee);
		User inviter = invitation.getInviter();
		inviter.removeOwnedInvitation(invitation);
		userRepository.save(inviter);
		group.removeInvitation(invitation);
		groupRepository.save(group);
		invitationRepository.delete(invitation);
		logger.debug("Invitation with id " + invitationId + "was deleted");
	}

	private boolean alreadyInvited(long inviterId, long inviteeId, long groupId) {
		return invitationRepository.countInvitation(inviterId, inviteeId, groupId) > 0;
	}

}
