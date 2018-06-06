package com.boot.cut_costs.dto.group.get;

import com.boot.cut_costs.dto.group.GroupBaseDto;

public abstract class GroupBaseGetDto extends GroupBaseDto {

	private static final long serialVersionUID = -9046561626087441811L;

	private String name;
	private boolean isAdmin;
	private boolean isSubscribed;
	private long totalAmount;

	public GroupBaseGetDto() {
		this.isSubscribed = false;
		this.isAdmin = false;
		this.totalAmount = 0;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

}