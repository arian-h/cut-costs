package com.boot.cut_costs.dto.group;

import java.util.List;

import com.boot.cut_costs.dto.expense.GetExpenseDto;
import com.boot.cut_costs.dto.user.AbstractUserDto;
import com.boot.cut_costs.dto.user.GetUserDto;

public class ExtendedGetGroupDto extends GetGroupDto {

	private static final long serialVersionUID = 1L;

	private GetUserDto admin;
	
	private List<GetUserDto> members;


	private List<GetExpenseDto> expenses;
	
	public ExtendedGetGroupDto() {
	}
	
	public AbstractUserDto getAdmin() {
		return admin;
	}

	public void setAdmin(GetUserDto admin) {
		this.admin = admin;
	}

	public List<GetUserDto> getMembers() {
		return members;
	}

	public void setMembers(List<GetUserDto> members) {
		this.members = members;
	}
	
	public List<GetExpenseDto> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<GetExpenseDto> expenses) {
		this.expenses = expenses;
	}

}
