package com.boot.cut_costs.dto.expense.get;


public class ExpenseSnippetGetDto extends ExpenseBaseGetDto {

	private static final long serialVersionUID = 6013151585993739738L;

	private long ownerId;
	private String ownerName;
	private long groupId;
	private String groupName;

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}