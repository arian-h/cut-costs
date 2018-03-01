package com.boot.cut_costs.dto.user;

import java.io.Serializable;

public class UserBaseDto implements Serializable {

	private static final long serialVersionUID = -5165800665955534856L;
	private String name;
	public UserBaseDto() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
