package com.boot.cut_costs.utils;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;

public class CustomValidationUtils {

	private final static String IMAGE_BASE64_HEADER_PATTERN = "^data:image/png;base64,";
	private final static String IMAGE_BASE64_TRAIL_PATTERN = "=*$";
	private final static String IMAGE_BASE64_PATTERN = IMAGE_BASE64_HEADER_PATTERN + ".*" + IMAGE_BASE64_TRAIL_PATTERN;
	/* 
	 * all alphanumeric character (starts with alphabet), with at most one period (dot) allowed in the middle
	 * and size between 8 and 15
	*/
	private final static String NAME_PATTERN = "^(?![^.\\n]*\\.[^.\\n]*\\.)[A-Za-z](?:\\.?[A-Za-z0-9]){7,14}$";
	/* 
	 * at least one uppercase, one lowercase, one digit and no special characters
	 * and size between 8 and 30
	 */
	private final static String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9]{7,29}$"; 
	
	private final static EmailValidator emailValidator = EmailValidator.getInstance();
	
	public static boolean validateUserName(String name){
		return !isEmptyOrWhitespace(name) && name.matches(NAME_PATTERN);
	}
	
	public static boolean validatePassword(String password) {
		return !isEmptyOrWhitespace(password) && password.matches(PASSWORD_PATTERN);
	}
	
	/**
	 * Validates email address. It also validates the domain name
	 * @param email
	 * @return true if email address is valid
	 */
	public static boolean validateEmail(String email) {
		return emailValidator.isValid(email);
	}
	
	/**
	 * Validates if the image string matches the expected pattern
	 * and the image size is less than passed in mbSize
	 * @param image
	 * @param mbSize
	 * @return true if the image is valid
	 */
	public static boolean validateImage(String image, int mbSize) {
		if (image != null && !image.matches(IMAGE_BASE64_PATTERN)) {
			return false;
		}
		return getImageSize(image) < Math.pow(2, 20) * mbSize;
	}
	
	public static boolean validateUserDescription(String description) {
		return description == null || description.length() <= 100;
	}
	
	public static boolean validateGroupName(String name) {
		return name.length() >= 5 && name.length() <= 25;
	}

	public static boolean validateGroupDescription(String description) {
		return description == null || description.length() <= 200;
	}
	
	public static boolean isEmptyOrWhitespace(String attr) {
		return attr == null || attr.trim().length() == 0;
	}

	private static double getImageSize(String image) {
		if (isEmptyOrWhitespace(image)) {
			return 0;
		}
		image.replaceAll(IMAGE_BASE64_TRAIL_PATTERN, "");
		image.replaceAll(IMAGE_BASE64_HEADER_PATTERN, "");
		return Math.ceil(image.length() * 3 / 4);
	}

	public static boolean validateExpenseTitle(String title) {
		if (isEmptyOrWhitespace(title)) {
			return false;
		}
		int length = title.trim().length();
		return length > 0 && length <= 20;
	}

	public static boolean validateExpenseAmount(long amount) {
		return amount > 0;
	}

	public static boolean validateInvitationDescription(String description) {
		if (isEmptyOrWhitespace(description)) {
			return false;
		}
		int length = description.trim().length();
		return length > 0 && length <= 25;
	}

	//TODO: fix this
	public static boolean validateSharers(List<Long> sharers) {
		return true;
	}
}
