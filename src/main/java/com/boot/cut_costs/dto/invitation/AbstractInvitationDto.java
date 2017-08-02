package com.boot.cut_costs.dto.invitation;

import java.io.Serializable;

public class AbstractInvitationDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
