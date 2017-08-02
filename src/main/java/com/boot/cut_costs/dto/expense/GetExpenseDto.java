package com.boot.cut_costs.dto.expense;

public class GetExpenseDto extends AbstractExpenseDto {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
