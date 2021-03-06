package com.boot.cut_costs.dto.group.get;

import com.boot.cut_costs.dto.group.GroupBaseDto;

public abstract class GroupBaseGetDto extends GroupBaseDto {

	private static final long serialVersionUID = -9046561626087441811L;

	private String name;
	private boolean isAdmin;
	private boolean isSubscribed;

	public GroupBaseGetDto() {
		this.isSubscribed = false;
		this.isAdmin = false;
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