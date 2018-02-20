package com.boot.cut_costs.dto.group.get;

public class SnippetGetGroupDto extends BaseGroupGetDto {

	private static final long serialVersionUID = 1L;

	private String imageId = null;

	private int numberOfMembers = 0;
	
	private int numberOfExpenses = 0;

	public int getNumberOfExpenses() {
		return numberOfExpenses;
	}

	public void setNumberOfExpenses(int numberOfExpenses) {
		this.numberOfExpenses = numberOfExpenses;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}
	
	public int getNumberOfMembers() {
		return this.numberOfMembers;
	}
	
}
