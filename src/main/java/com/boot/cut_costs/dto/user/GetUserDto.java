package com.boot.cut_costs.dto.user;

public class GetUserDto extends AbstractUserDto {
	
	private static final long serialVersionUID = 1L;
	
	private String imageId;

	private long id;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
