package com.boot.cut_costs.utils;

import java.util.Set;

import org.apache.commons.validator.routines.EmailValidator;

public class CustomValidationUtils {

	/* 
	 * at least one uppercase, one lowercase, one digit and no special characters
	 * and size between 6 and 20 
	 */
	private final static String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9]{6,20}$"; 
	
	private final static String IMAGE_HEADER_PATTERN = "data:image/.+;base64";
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
		if (image == null) {
			return true;
		}
		if (!image.contains(",")) {
			return false;
		}
		String[] imageContent = image.split(",");
		if (!imageContent[0].matches(IMAGE_HEADER_PATTERN)) {
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

	public static boolean validateExpenseTitle(String title) {
		if (isEmptyOrWhitespace(title)) {
			return false;
		}
		int length = title.trim().length();
		return length > 0 && length <= 10;
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

	public static boolean validateSharers(Set<Long> sharers) {
		return true;
	}
}
