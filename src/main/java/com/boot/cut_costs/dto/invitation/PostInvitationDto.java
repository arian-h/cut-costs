package com.boot.cut_costs.dto.invitation;

public class PostInvitationDto extends AbstractInvitationDto {
	
	private static final long serialVersionUID = 1L;
	
	private long inviteeId;

	private long groupId;

	public long getInviteeId() {
		return inviteeId;
	}

	public void setInviteeId(long inviteeId) {
		this.inviteeId = inviteeId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

}