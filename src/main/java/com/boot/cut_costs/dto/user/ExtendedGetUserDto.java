package com.boot.cut_costs.dto.user;

import java.util.List;

import com.boot.cut_costs.dto.expense.GetExpenseDto;
import com.boot.cut_costs.dto.group.GetGroupDto;
import com.boot.cut_costs.dto.invitation.GetInvitationDto;

public class ExtendedGetUserDto extends GetUserDto {
	
	private static final long serialVersionUID = 1L;

	private List<GetInvitationDto> receivedInvitations;
	private List<GetExpenseDto> receivedExpenses;
	private List<GetExpenseDto> ownedExpenses;
	private List<GetGroupDto> ownedGroups;
	private List<GetGroupDto> memberGroups;
	
	public ExtendedGetUserDto() {
	}

	public List<GetInvitationDto> getReceivedInvitations() {
		return receivedInvitations;
	}
	
	public void setReceivedInvitations(List<GetInvitationDto> receivedInvitations) {
		this.receivedInvitations = receivedInvitations;
	}
	
	public List<GetExpenseDto> getReceivedExpenses() {
		return receivedExpenses;
	}
	
	public void setReceivedExpenses(List<GetExpenseDto> receivedExpenses) {
		this.receivedExpenses = receivedExpenses;
	}
	
	public List<GetExpenseDto> getOwnedExpenses() {
		return ownedExpenses;
	}
	
	public void setOwnedExpenses(List<GetExpenseDto> ownedExpenses) {
		this.ownedExpenses = ownedExpenses;
	}
	
	public List<GetGroupDto> getOwnedGroups() {
		return ownedGroups;
	}
	
	public void setOwnedGroups(List<GetGroupDto> ownedGroups) {
		this.ownedGroups = ownedGroups;
	}
	
	public List<GetGroupDto> getMemberGroups() {
		return memberGroups;
	}

	public void setMemberGroups(List<GetGroupDto> memberGroups) {
		this.memberGroups = memberGroups;
	}

}
