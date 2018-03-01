package com.boot.cut_costs.dto.group;

import java.io.Serializable;

public abstract class GroupBaseDto implements Serializable {

	private static final long serialVersionUID = -5879725932532878152L;

	private String name;
	private long id;
	private String description;

	public GroupBaseDto() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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