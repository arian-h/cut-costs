package com.boot.cut_costs.dto.user.get;

import com.boot.cut_costs.dto.user.UserBaseDto;

public abstract class UserBaseGetDto extends UserBaseDto {

	private static final long serialVersionUID = -1049650217208825270L;
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
