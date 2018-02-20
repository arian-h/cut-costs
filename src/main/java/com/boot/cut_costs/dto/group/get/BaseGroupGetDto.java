package com.boot.cut_costs.dto.group.get;

import com.boot.cut_costs.dto.group.BaseGroupDto;

public abstract class BaseGroupGetDto extends BaseGroupDto {

	private static final long serialVersionUID = -9046561626087441811L;

	private String name;
	private boolean isAdmin = false;

	public BaseGroupGetDto() {}

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
}