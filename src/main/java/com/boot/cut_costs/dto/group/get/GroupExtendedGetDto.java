package com.boot.cut_costs.dto.group.get;

import java.util.List;

import com.boot.cut_costs.dto.expense.get.ExpenseSnippetGetDto;
import com.boot.cut_costs.dto.user.get.UserSnippetGetDto;

public class GroupExtendedGetDto extends GroupBaseGetDto {

	private static final long serialVersionUID = 4462927336501279501L;
	private List<ExpenseSnippetGetDto> expenses;
	private String imageId = null;
	private String description;
	private UserSnippetGetDto admin;
	private List<UserSnippetGetDto> members;

	public GroupExtendedGetDto() {}

	public List<UserSnippetGetDto> getMembers() {
		return members;
	}

	public void setMembers(List<UserSnippetGetDto> members) {
		this.members = members;
	}

	public UserSnippetGetDto getAdmin() {
		return admin;
	}

	public void setAdmin(UserSnippetGetDto admin) {
		this.admin = admin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<ExpenseSnippetGetDto> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<ExpenseSnippetGetDto> expenses) {
		this.expenses = expenses;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}