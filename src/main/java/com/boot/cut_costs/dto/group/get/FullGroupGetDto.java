package com.boot.cut_costs.dto.group.get;

import java.util.List;

import com.boot.cut_costs.dto.expense.GetExpenseDto;
import com.boot.cut_costs.dto.user.GetUserDto;

public class FullGroupGetDto extends BaseGroupGetDto {

	private static final long serialVersionUID = 4462927336501279501L;

	private List<GetExpenseDto> expenses;

	private String description;

	private GetUserDto admin;

	public FullGroupGetDto() {}

	public GetUserDto getAdmin() {
		return admin;
	}

	public void setAdmin(GetUserDto admin) {
		this.admin = admin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<GetExpenseDto> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<GetExpenseDto> expenses) {
		this.expenses = expenses;
	}

}