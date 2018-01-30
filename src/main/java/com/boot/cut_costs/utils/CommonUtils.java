package com.boot.cut_costs.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.boot.cut_costs.exception.BadRequestException;

public class CommonUtils {
	
	public final static File IMAGE_FOLDER = new File("/tmp");
	public final static String IMAGE_FORMAT_NAME = "png";

    public static String decodeBase64AndSaveImage(String data) throws IOException, BadRequestException {
		try {
			if (data == null) {
				return null;
			}
			String base64Image = data.split(",")[1];
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			String imageId = RandomStringUtils.randomAlphanumeric(15);
			ImageIO.write(img, IMAGE_FORMAT_NAME, getImageFile(imageId));
			return imageId;
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Bad image data");
		}
	}
	
	/**
	 * Convert id in String format to long format
	 * @return the id in long format
	 */
	public static long convertId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Bad id passed: " + id);
		}
	}
	
	public static File getImageFile(String imageId) {
		return new File(IMAGE_FOLDER, imageId + "." + IMAGE_FORMAT_NAME);
	}
	
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
}
