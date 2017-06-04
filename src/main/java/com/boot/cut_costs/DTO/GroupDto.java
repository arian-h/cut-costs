package com.boot.cut_costs.DTO;

import java.io.Serializable;

public class GroupDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private String description;

	/*
	 * Base 64 image format
	 */
	private String image;
	
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
