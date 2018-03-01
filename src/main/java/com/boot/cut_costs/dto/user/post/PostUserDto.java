package com.boot.cut_costs.dto.user.post;

import com.boot.cut_costs.dto.user.UserBaseDto;

public class PostUserDto extends UserBaseDto {

	private static final long serialVersionUID = 1L;
	/* Base64 image data*/
	private String image;
	private String description;

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}