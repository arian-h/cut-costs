package com.boot.cut_costs.exception;

import org.springframework.http.ResponseEntity;

public abstract class CustomRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -458792061586838235L;
	public abstract ResponseEntity<String> getErrorResponse();
}
