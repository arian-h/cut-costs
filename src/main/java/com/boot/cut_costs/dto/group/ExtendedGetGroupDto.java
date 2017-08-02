package com.boot.cut_costs.dto.group;

import java.util.Set;

import com.boot.cut_costs.dto.expense.GetExpenseDto;
import com.boot.cut_costs.dto.user.AbstractUserDto;
import com.boot.cut_costs.dto.user.GetUserDto;

public class ExtendedGetGroupDto extends GetGroupDto {

	private static final long serialVersionUID = 1L;

	private GetUserDto admin;
	
	private Set<GetUserDto> members;


	private Set<GetExpenseDto> expenses;
	
	public ExtendedGetGroupDto() {
	}
	
	public AbstractUserDto getAdmin() {
		return admin;
	}

	public void setAdmin(GetUserDto admin) {
		this.admin = admin;
	}

	public Set<GetUserDto> getMembers() {
		return members;
	}

	public void setMembers(Set<GetUserDto> members) {
		this.members = members;
	}
	
	public Set<GetExpenseDto> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<GetExpenseDto> expenses) {
		this.expenses = expenses;
	}

}
