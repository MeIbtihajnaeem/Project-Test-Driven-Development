package com.mycompany.orderAssignmentSystem.controller.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationConfigurations {

	public String validateName(String name) {
		if (name == null || name == "") {
			throw new NullPointerException("The name field cannot be empty.");
		}
		if (name.length() <= 2) {
			throw new IllegalArgumentException(
					"The name must be at least 3 characters long. Please provide a valid name.");
		}
		if (name.length() > 20) {
			throw new IllegalArgumentException("The name cannot exceed 20 characters. Please provide a shorter name.");
		}

		if (_containsSpecialCharacters(name)) {
			throw new IllegalArgumentException(
					"The name cannot contain special characters. Please remove any special characters from the name.");
		}

		if (_containsNumbers(name)) {
			throw new IllegalArgumentException(
					"The name cannot contain numbers. Please remove any number from the name.");
		}
		if (_containsTabs(name)) {
			throw new IllegalArgumentException("The name cannot contain tabs. Please remove any tabs from the name.");
		}
		name = name.trim();
		return name;
	}

	public String validatePhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber == "") {
			throw new NullPointerException("The phone number field cannot be empty.");
		}
		if (phoneNumber.length() != 10) {
			throw new IllegalArgumentException(
					"The phone number must be 10 characters long. Please provide a valid phone number.");
		}
		if (phoneNumber.matches("[0-9]+") == false) {
			throw new IllegalArgumentException(
					"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
		}
		return phoneNumber;
	}

	private boolean _containsSpecialCharacters(String str) {
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	private boolean _containsNumbers(String str) {
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	private boolean _containsTabs(String str) {
		Pattern pattern = Pattern.compile("\\t");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

}
