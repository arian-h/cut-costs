package com.boot.cut_costs.dto.user.get;

public class UserExtendedGetDto extends UserBaseGetDto {

	private static final long serialVersionUID = -8577723382753128124L;
	private String description;
	private String imageId;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}
