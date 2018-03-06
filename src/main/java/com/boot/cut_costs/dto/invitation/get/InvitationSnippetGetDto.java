package com.boot.cut_costs.dto.invitation.get;

import com.boot.cut_costs.dto.group.get.GroupSnippetGetDto;
import com.boot.cut_costs.dto.invitation.InvitationBaseDto;
import com.boot.cut_costs.dto.user.get.UserSnippetGetDto;

public class InvitationSnippetGetDto extends InvitationBaseDto {

	private static final long serialVersionUID = 1L;
	
	private long id;

	private UserSnippetGetDto inviter;

	private GroupSnippetGetDto group;

	public UserSnippetGetDto getInviter() {
		return inviter;
	}

	public void setInviter(UserSnippetGetDto inviter) {
		this.inviter = inviter;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public GroupSnippetGetDto getGroup() {
		return group;
	}

	public void setGroup(GroupSnippetGetDto group) {
		this.group = group;
	}
}
