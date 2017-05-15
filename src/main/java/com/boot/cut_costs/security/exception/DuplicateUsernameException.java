package com.boot.cut_costs.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Username already exists")  // 409
public class DuplicateUsernameException extends RuntimeException{
	private static final long serialVersionUID = 6699623945573914987L;
}
