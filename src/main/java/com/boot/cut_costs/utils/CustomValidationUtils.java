package com.boot.cut_costs.utils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public class CustomValidationUtils {

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
	 * Validates if the file is image and its size is less than mbSize
	 * @param image
	 * @param mbSize
	 * @return true if the image is valid
	 */
	public static boolean validateImage(MultipartFile image, int mbSize) {
		return image == null || (getFileType(image).startsWith("image") && (image.getSize() / Math.pow(2, 20)) < mbSize);
	}

	private static String getFileType(MultipartFile file) {
		Tika tika = new Tika();
		System.out.println(file.getSize() / Math.pow(2, 20));
		try {
			return tika.detect(file.getBytes());
		} catch (IOException e) {
			return "";
		}
	}

	public static boolean validateUserDescription(String description) {
		return description == null || description.length() <= 100;
	}
	
	public static boolean validateGroupName(String name) {
		return name.trim().length() >= 5 && name.trim().length() <= 25;
	}

	public static boolean validateGroupDescription(String description) {
		return description == null || description.trim().length() <= 200;
	}
	
	public static boolean isEmptyOrWhitespace(String attr) {
		return attr == null || attr.trim().length() == 0;
	}

	public static boolean validateExpenseTitle(String title) {
		if (isEmptyOrWhitespace(title)) {
			return false;
		}
		int length = title.trim().length();
		return length >= 5 && length <= 30;
	}

	public static boolean validateExpenseDescription(String description) {
		return description == null || description.trim().length() <= 200;
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
