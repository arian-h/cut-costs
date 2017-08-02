package com.boot.cut_costs.dto.invitation;

public class PostInvitationDto extends AbstractInvitationDto {
	
	private static final long serialVersionUID = 1L;
	
	private long inviteeId;

	private long groupId;

	public long getInvitee() {
		return inviteeId;
	}

	public void setInvitee(long inviteeId) {
		this.inviteeId = inviteeId;
	}

	public long getGroup() {
		return groupId;
	}

	public void setGroup(long groupId) {
		this.groupId = groupId;
	}

}