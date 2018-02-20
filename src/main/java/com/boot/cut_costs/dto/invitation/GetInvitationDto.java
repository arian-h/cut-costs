package com.boot.cut_costs.dto.invitation;

import com.boot.cut_costs.dto.group.get.SnippetGetGroupDto;
import com.boot.cut_costs.dto.user.GetUserDto;

public class GetInvitationDto extends AbstractInvitationDto {

	private static final long serialVersionUID = 1L;
	
	private long id;

	private GetUserDto inviter;

	private SnippetGetGroupDto group;

	public GetUserDto getInviter() {
		return inviter;
	}

	public void setInviter(GetUserDto inviter) {
		this.inviter = inviter;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public SnippetGetGroupDto getGroup() {
		return group;
	}

	public void setGroup(SnippetGetGroupDto group) {
		this.group = group;
	}
}
