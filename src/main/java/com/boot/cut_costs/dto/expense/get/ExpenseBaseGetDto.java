package com.boot.cut_costs.dto.expense.get;

import com.boot.cut_costs.dto.expense.ExpenseBaseDto;
import com.boot.cut_costs.dto.user.get.UserSnippetGetDto;

public class ExpenseBaseGetDto extends ExpenseBaseDto {

	private static final long serialVersionUID = 4342554135210225790L;

	private long id;

	private UserSnippetGetDto owner;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserSnippetGetDto getOwner() {
		return owner;
	}

	public void setOwner(UserSnippetGetDto owner) {
		this.owner = owner;
	}

}