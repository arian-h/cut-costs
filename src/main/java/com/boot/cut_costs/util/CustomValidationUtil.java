package com.boot.cut_costs.util;

import org.apache.commons.validator.routines.EmailValidator;

public class CustomValidationUtil {

	/* 
	 * at least one uppercase, one lowercase, one digit and no special characters
	 * and size between 6 and 20 
	 */
	private final static String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9]{6,20}$"; 
	
	/* 
	 * all alphanumeric character (starts with alphabet), with at most one period (dot) allowed in the middle
	 * and size between 8 and 15
	*/
	private final static String NAME_PATTERN = "^(?!.*\\..*\\..*)[A-Za-z]([A-Za-z0-9.]*[A-Za-z0-9]){8,15}$";
	
	private final static EmailValidator emailValidator = EmailValidator.getInstance();
	
	public static boolean validateUserName(String name){
		return !isEmptyOrWhitespace(name) && name.matches(NAME_PATTERN);
	}
	
	public static boolean validatePassword(String password) {
		return !isEmptyOrWhitespace(password) && password.matches(PASSWORD_PATTERN);
	}
	
	public static boolean validateEmail(String email) {
		return emailValidator.isValid(email);
	}
	
	//size in megabytes
	public static boolean validateImage(String image, int mbSize) {
		return getImageSize(image) < Math.pow(2, 20) * mbSize;
	}
	
	public static boolean validateUserDescription(String description) {
		return description == null || description.length() <= 100;
	}
	
	public static boolean validateGroupName(String name) {
		return !isEmptyOrWhitespace(name) && name.length() >= 5 && name.length() <= 25;
	}

	public static boolean validateGroupDescription(String description) {
		return description == null || description.length() <= 200;
	}
	
	private static boolean isEmptyOrWhitespace(String attr) {
		return attr == null || attr.trim().length() == 0;
	}

	private static double getImageSize(String image) {
		if (image == null || image.length() == 0) {
			return 0;
		}
		int length = image.length();
		int countEqualSigns = 0;
		int index = length - 1;
		while (index >= 0 && image.charAt(index) == '=') {
			countEqualSigns++;
			index--;
		}
		length -= countEqualSigns;
		return Math.ceil(length * 3 / 4);
	}
}
