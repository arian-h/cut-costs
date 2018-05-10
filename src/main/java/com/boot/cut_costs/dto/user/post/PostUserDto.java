package com.boot.cut_costs.dto.user.post;

import org.springframework.web.multipart.MultipartFile;

import com.boot.cut_costs.dto.user.UserBaseDto;

public class PostUserDto extends UserBaseDto {

	private static final long serialVersionUID = 1L;
	/* Base64 image data*/
	private MultipartFile image;
	private String description;

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public MultipartFile getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}