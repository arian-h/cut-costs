package com.boot.cut_costs.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.boot.cut_costs.utils.AWS.FILE_TYPE;

public class CommonUtils {
	
	public final static String IMAGE_FILE_EXTENSION = "png";
	
	public static ResponseEntity<String> createErrorResponse(HashMap<String, String> responseBodyMap, HttpStatus httpStatus) {
        
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject responseBodyJson = new JSONObject();
        try {
            for (String key: responseBodyMap.keySet()) {
    			responseBodyJson.put(key, responseBodyMap.get(key));
            }        	
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return new ResponseEntity<String>(responseBodyJson.toString(), headers, httpStatus);
	}

	public static File convertToFile(FILE_TYPE fileType, MultipartFile multipart) {
		File convFile = null;
		try {
			convFile = File.createTempFile(fileType.getFilePrefix(), IMAGE_FILE_EXTENSION);
			multipart.transferTo(convFile);
		} catch (Exception e) {
			convFile = null;
			e.printStackTrace();
		}
		return convFile;
	}

}