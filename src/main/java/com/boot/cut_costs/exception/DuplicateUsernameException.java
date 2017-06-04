package com.boot.cut_costs.exception;

public class DuplicateUsernameException extends RuntimeException {
	private static final long serialVersionUID = 6699623945573914987L;
	public DuplicateUsernameException(String msg) {
		super(msg);
	}
}
