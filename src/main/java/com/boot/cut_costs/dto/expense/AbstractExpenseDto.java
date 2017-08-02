package com.boot.cut_costs.dto.expense;

import java.io.Serializable;

public class AbstractExpenseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
