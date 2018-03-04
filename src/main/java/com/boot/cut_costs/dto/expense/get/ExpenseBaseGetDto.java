package com.boot.cut_costs.dto.expense.get;

import com.boot.cut_costs.dto.expense.ExpenseBaseDto;

public class ExpenseBaseGetDto extends ExpenseBaseDto {

	private static final long serialVersionUID = 4342554135210225790L;

	private long id;
	private boolean isOwner;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

}