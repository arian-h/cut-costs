package com.boot.cut_costs.exception;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.boot.cut_costs.utils.CommonUtils;

public abstract class ResourceNotFoundException extends CustomRuntimeException {

	private static final long serialVersionUID = 7651831515009195762L;

	protected final long resourceId;
	private final static HttpStatus httpStatus = HttpStatus.NOT_FOUND;
	
	public ResourceNotFoundException(long resourceId) {
		this.resourceId = resourceId;
	}
	
	public abstract String getMessage();
	
	@SuppressWarnings("serial")
	@Override
	public ResponseEntity<String> getErrorResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return CommonUtils.createErrorResponse(new HashMap<String, String>() {
			{
				put("message", getMessage());
				put("id", Long.toString(resourceId));
			}
		}, httpStatus);
	}

}
