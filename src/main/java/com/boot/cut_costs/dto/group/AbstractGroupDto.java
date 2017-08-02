package com.boot.cut_costs.dto.group;

import java.io.Serializable;

public abstract class AbstractGroupDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;

	private String description;

	public AbstractGroupDto() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}