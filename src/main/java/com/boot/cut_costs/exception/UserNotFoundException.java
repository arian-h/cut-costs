package com.boot.cut_costs.exception;

public class UserNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 4674364457249643951L;

	public UserNotFoundException(long resourceId) {
		super(resourceId);
	}

	@Override
	public String getMessage() {
		return String.format("User with id %s was not found", super.resourceId);
	}
	
}
