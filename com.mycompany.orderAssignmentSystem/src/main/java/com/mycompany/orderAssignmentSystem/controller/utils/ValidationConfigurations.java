package com.mycompany.orderAssignmentSystem.controller.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;

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

	public String validateAddress(String address) {
		if (address == null || address == "") {
			throw new NullPointerException("The address field cannot be empty.");
		}
		if (address.length() <= 10) {
			throw new IllegalArgumentException(
					"The Address must be at least 10 characters long. Please provide a valid Address.");
		}
		if (address.length() > 50) {
			throw new IllegalArgumentException(
					"The Address cannot exceed 50 characters. Please provide a shorter Address.");
		}
		if (_containsTabs(address)) {
			throw new IllegalArgumentException(
					"The address cannot contain tabs. Please remove any tabs from the address.");
		}
		address = address.trim();
		return address;
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
		if (phoneNumber.matches("^3[0-9]*$") == false) {
			throw new IllegalArgumentException(
					"The phone number must start with 3. Please provide a valid phone number.");
		}
		return phoneNumber;
	}

	public Long validateId(Long id) {
		if (id == null) {
			throw new NullPointerException("The id field cannot be empty.");
		}
		if (id <= 0) {
			throw new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id.");
		}
		return id;
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

	public OrderCategory validateCategory(OrderCategory category) {
		if (category == null) {
			throw new NullPointerException("The category field cannot be empty.");
		}
		return category;
	}

	public OrderStatus validateStatus(OrderStatus status) {
		if (status == null) {
			throw new NullPointerException("The status field cannot be empty.");
		}
		return status;
	}

}
