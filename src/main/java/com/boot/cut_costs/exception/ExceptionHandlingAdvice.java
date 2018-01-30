																																																																																																																					package com.boot.cut_costs.exception;

import io.jsonwebtoken.JwtException;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.boot.cut_costs.utils.CommonUtils;

@Component
@ControllerAdvice
public class ExceptionHandlingAdvice {
    
	private static final Map<String, HttpStatus> ERROR_STATUS_MAP = createHttpStatusCodesMap();
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);
	
	private static Map<String, HttpStatus> createHttpStatusCodesMap() {
        Map<String,HttpStatus> error_status_map = new HashMap<String,HttpStatus>();
        error_status_map.put(BadRequestException.class.getName(), HttpStatus.BAD_REQUEST); // 400
        //error_status_map.put(ValidationException.class.getName(), HttpStatus.BAD_REQUEST); // 400
        error_status_map.put(MethodArgumentTypeMismatchException.class.getName(), HttpStatus.BAD_REQUEST); //400
        error_status_map.put(BadCredentialsException.class.getName(), HttpStatus.UNAUTHORIZED); // 401
        error_status_map.put(JwtException.class.getName(), HttpStatus.UNAUTHORIZED); // 401
        error_status_map.put(AccessDeniedException.class.getName(), HttpStatus.FORBIDDEN); // 403
        error_status_map.put(ResourceNotFoundException.class.getName(), HttpStatus.NOT_FOUND); // 404
        error_status_map.put(DuplicateUsernameException.class.getName(), HttpStatus.CONFLICT); // 409
        return error_status_map;
    }
    
	@SuppressWarnings("serial")
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> RuntimeExceptionHandler(RuntimeException e) {

		logger.debug("Runtime exception: " + e.getMessage());

		if (CustomRuntimeException.class.isAssignableFrom(e.getClass())) {
			return ((CustomRuntimeException)e).getErrorResponse();
		}

		HttpStatus httpStatus = ERROR_STATUS_MAP.get(e.getClass().getName());
		if (httpStatus == null) { //TODO Maybe it's not a good idea to send back getMessage value to the client ?
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return CommonUtils.createErrorResponse(new HashMap<String, String>() {
			{
				put("message", e.getMessage());
			}
		}, httpStatus);
	}
}