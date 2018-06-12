package com.boot.cut_costs.dto.group.get;

public class GroupSnippetGetDto extends GroupBaseGetDto {

	private static final long serialVersionUID = 6006211962066876153L;

	private int numberOfMembers;
	private long totalAmount;
	private int numberOfExpenses;

	public GroupSnippetGetDto() {
		this.numberOfMembers = 0;
		this.totalAmount = 0;
		this.numberOfExpenses = 0;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

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