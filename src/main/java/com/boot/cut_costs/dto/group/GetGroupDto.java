package com.boot.cut_costs.dto.group;

public class GetGroupDto extends AbstractGroupDto {

	private static final long serialVersionUID = 1L;

	private long id;

	private String imageId;

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
