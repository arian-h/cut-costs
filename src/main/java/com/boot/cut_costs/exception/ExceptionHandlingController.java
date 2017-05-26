package com.boot.cut_costs.exception;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.boot.cut_costs.security.exception.DuplicateUsernameException;

@ControllerAdvice
public class ExceptionHandlingController {
	
	  @ResponseStatus(value=HttpStatus.BAD_REQUEST)  // 400
	  @ExceptionHandler(ValidationException.class)
	  public void ValidationExceptionHandler() {}
	  
	  @ResponseStatus(value=HttpStatus.CONFLICT)  // 409
	  @ExceptionHandler(DuplicateUsernameException.class)
	  public void DuplicateUsernameExceptionHandler() {}
	  
	  
	  @ResponseStatus(value=HttpStatus.UNAUTHORIZED)  // 401
	  @ExceptionHandler(BadCredentialsException.class)
	  public void BadCredentialsExceptionHandler() {}
	  
}