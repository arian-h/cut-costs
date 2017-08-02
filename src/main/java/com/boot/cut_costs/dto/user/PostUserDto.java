package com.boot.cut_costs.dto.user;

public class PostUserDto extends AbstractUserDto {

	private static final long serialVersionUID = 1L;
	/*
	 * Base64 image data
	 */
	private String image;

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}
}
