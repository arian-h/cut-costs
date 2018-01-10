package com.boot.cut_costs.dto.group;

public class GetGroupDto extends AbstractGroupDto {

	private static final long serialVersionUID = 1L;

	private long id;

	private String imageId = null;

	private int numberOfMembers = 0;
	
	private int numberOfExpenses = 0;
	
	private boolean isAdmin = false;
	
	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

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

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}
	
	public int getNumberOfMembers() {
		return this.numberOfMembers;
	}
	
	
}
