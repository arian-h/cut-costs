package com.boot.cut_costs.dto.user;

import java.io.Serializable;

public abstract class AbstractUserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	public AbstractUserDto() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
