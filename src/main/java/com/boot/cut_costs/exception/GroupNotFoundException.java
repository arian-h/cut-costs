package com.boot.cut_costs.exception;

public class GroupNotFoundException extends ResourceNotFoundException {
	
	private static final long serialVersionUID = 4674364457249643951L;

	public GroupNotFoundException(long resourceId) {
		super(resourceId);
	}

	@Override
	public String getMessage() {
		return String.format("Group with id %s was not found", resourceId);
	}
	
}
