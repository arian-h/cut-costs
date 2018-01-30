package com.boot.cut_costs.exception;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.boot.cut_costs.utils.CommonUtils;

public class InputValidationException extends CustomRuntimeException {

	private static final long serialVersionUID = -7385439645153449387L;

	private final String fieldName;
	private final static HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	
	public InputValidationException(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getMessage() {
		return String.format("Bad value for the input argument %s", fieldName);
	}
	
	@SuppressWarnings("serial")
	@Override
	public ResponseEntity<String> getErrorResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return CommonUtils.createErrorResponse(new HashMap<String, String>() {
			{
				put("message", getMessage());
				put("field", fieldName);
			}
		}, httpStatus);
	}
}
