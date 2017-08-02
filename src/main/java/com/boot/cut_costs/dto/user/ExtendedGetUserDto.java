package com.boot.cut_costs.dto.user;

import java.util.Set;

import com.boot.cut_costs.dto.expense.GetExpenseDto;
import com.boot.cut_costs.dto.group.GetGroupDto;
import com.boot.cut_costs.dto.invitation.GetInvitationDto;

//TODO : 
public class ExtendedGetUserDto extends GetUserDto {
	
	private static final long serialVersionUID = 1L;

	private Set<GetInvitationDto> receivedInvitations;
	private Set<GetExpenseDto> receivedExpenses;
	private Set<GetExpenseDto> ownedExpenses;
	private Set<GetGroupDto> ownedGroups;
	private Set<GetGroupDto> memberGroups;
	
	public ExtendedGetUserDto() {
	}

	public Set<GetInvitationDto> getReceivedInvitations() {
		return receivedInvitations;
	}
	
	public void setReceivedInvitations(Set<GetInvitationDto> receivedInvitations) {
		this.receivedInvitations = receivedInvitations;
	}
	
	public Set<GetExpenseDto> getReceivedExpenses() {
		return receivedExpenses;
	}
	
	public void setReceivedExpenses(Set<GetExpenseDto> receivedExpenses) {
		this.receivedExpenses = receivedExpenses;
	}
	
	public Set<GetExpenseDto> getOwnedExpenses() {
		return ownedExpenses;
	}
	
	public void setOwnedExpenses(Set<GetExpenseDto> ownedExpenses) {
		this.ownedExpenses = ownedExpenses;
	}
	
	public Set<GetGroupDto> getOwnedGroups() {
		return ownedGroups;
	}
	
	public void setOwnedGroups(Set<GetGroupDto> ownedGroups) {
		this.ownedGroups = ownedGroups;
	}
	
	public Set<GetGroupDto> getMemberGroups() {
		return memberGroups;
	}

	public void setMemberGroups(Set<GetGroupDto> memberGroups) {
		this.memberGroups = memberGroups;
	}

}
