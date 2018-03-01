package com.boot.cut_costs.dto.expense;

import java.io.Serializable;

public class ExpenseBaseDto implements Serializable {

	private static final long serialVersionUID = -7417251455801169323L;

	private String title;

	private long amount;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

}