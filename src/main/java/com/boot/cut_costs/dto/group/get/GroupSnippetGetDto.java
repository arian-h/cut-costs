package com.boot.cut_costs.dto.group.get;

public class GroupSnippetGetDto extends GroupBaseGetDto {

	private static final long serialVersionUID = 6006211962066876153L;

	private int numberOfMembers = 0;
	
	private int numberOfExpenses = 0;

	public int getNumberOfExpenses() {
		return numberOfExpenses;
	}

	public void setNumberOfExpenses(int numberOfExpenses) {
		this.numberOfExpenses = numberOfExpenses;
	}

	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

	public int getNumberOfMembers() {
		return this.numberOfMembers;
	}

}