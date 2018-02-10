package com.boot.cut_costs.exception;

public class DuplicateGroupNameException extends RuntimeException {

	private static final long serialVersionUID = -1902381723652471133L;

	public DuplicateGroupNameException(String groupName) {
		super(String.format("Group name %s is already taken", groupName));
	}
	
}