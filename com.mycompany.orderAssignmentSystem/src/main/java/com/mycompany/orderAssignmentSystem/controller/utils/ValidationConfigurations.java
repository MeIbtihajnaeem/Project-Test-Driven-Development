package com.mycompany.orderAssignmentSystem.controller.utils;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.controller.WorkerController;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;

public class ValidationConfigurations {
	private static final Logger LOGGER = LogManager.getLogger(WorkerController.class);

	public String validateName(String name) {
		if (name == null || name == "") {
			LOGGER.info("The name field cannot be empty.");
			throw new NullPointerException("The name field cannot be empty.");
		}
		if (name.length() <= 2) {
			LOGGER.info("The name must be at least 3 characters long. Please provide a valid name.");

			throw new IllegalArgumentException(
					"The name must be at least 3 characters long. Please provide a valid name.");
		}
		if (name.length() > 20) {
			LOGGER.info("The name cannot exceed 20 characters. Please provide a shorter name.");

			throw new IllegalArgumentException("The name cannot exceed 20 characters. Please provide a shorter name.");
		}

		if (_containsSpecialCharacters(name)) {
			LOGGER.info(
					"The name cannot contain special characters. Please remove any special characters from the name.");

			throw new IllegalArgumentException(
					"The name cannot contain special characters. Please remove any special characters from the name.");
		}

		if (_containsNumbers(name)) {
			LOGGER.info("The name cannot contain numbers. Please remove any number from the name.");

			throw new IllegalArgumentException(
					"The name cannot contain numbers. Please remove any number from the name.");
		}
		if (_containsTabs(name)) {
			LOGGER.info("The name cannot contain tabs. Please remove any tabs from the name.");

			throw new IllegalArgumentException("The name cannot contain tabs. Please remove any tabs from the name.");
		}
		name = name.trim();
		return name;
	}

	public boolean validateStringNumber(String str) {
		if (str == null || str == "") {
			LOGGER.info("The text cannot be empty.");
			throw new NullPointerException("The number cannot be empty.");
		}

		if (str.length() > 20) {
			LOGGER.info("The number cannot exceed 20 characters. Please provide a shorter number.");

			throw new IllegalArgumentException(
					"The number cannot exceed 20 characters. Please provide a shorter number.");
		}

		Pattern pattern = Pattern.compile("^-?[0-9]+$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public String validateAddress(String address) {
		if (address == null || address == "") {
			LOGGER.info("The address field cannot be empty.");

			throw new NullPointerException("The address field cannot be empty.");
		}
		if (address.length() <= 10) {
			LOGGER.info("The Address must be at least 10 characters long. Please provide a valid Address.");

			throw new IllegalArgumentException(
					"The Address must be at least 10 characters long. Please provide a valid Address.");
		}
		if (address.length() > 50) {
			LOGGER.info("The Address cannot exceed 50 characters. Please provide a shorter Address.");

			throw new IllegalArgumentException(
					"The Address cannot exceed 50 characters. Please provide a shorter Address.");
		}
		if (_containsTabs(address)) {
			LOGGER.info("The address cannot contain tabs. Please remove any tabs from the address.");

			throw new IllegalArgumentException(
					"The address cannot contain tabs. Please remove any tabs from the address.");
		}
		address = address.trim();
		return address;
	}

	public String validatePhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber == "") {
			LOGGER.info("The phone number field cannot be empty.");

			throw new NullPointerException("The phone number field cannot be empty.");
		}
		if (phoneNumber.length() != 10) {
			LOGGER.info("The phone number must be 10 characters long. Please provide a valid phone number.");

			throw new IllegalArgumentException(
					"The phone number must be 10 characters long. Please provide a valid phone number.");
		}
		if (phoneNumber.matches("[0-9]+") == false) {
			LOGGER.info(
					"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");

			throw new IllegalArgumentException(
					"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
		}
		if (phoneNumber.matches("^3[0-9]*$") == false) {
			LOGGER.info("The phone number must start with 3. Please provide a valid phone number.");

			throw new IllegalArgumentException(
					"The phone number must start with 3. Please provide a valid phone number.");
		}
		return phoneNumber;
	}

	public long validateId(Long id) {
		if (id == null) {
			LOGGER.info("The id field cannot be empty.");

			throw new NullPointerException("The id field cannot be empty.");
		}
		if (id <= 0) {
			LOGGER.info("The id field cannot be less than 1. Please provide a valid id.");

			throw new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id.");
		}
		return id;
	}

	public OrderCategory validateCategory(OrderCategory category) {
		if (category == null) {
			LOGGER.info("The category field cannot be empty.");

			throw new NullPointerException("The category field cannot be empty.");
		}
		return category;
	}

	public OrderStatus validateStatus(OrderStatus status) {
		if (status == null) {
			LOGGER.info("The status field cannot be empty.");

			throw new NullPointerException("The status field cannot be empty.");
		}
		return status;
	}

	public <T extends Enum<T>> T validateEnum(String value, Class<T> enumType) {
		String message = "status";
		if (enumType == OrderCategory.class) {
			message = "category";
		}
		if (value == null || value.isEmpty()) {
			LOGGER.info("The " + message + " field cannot be empty.");

			throw new NullPointerException("The " + message + " field cannot be empty.");
		}
		value = value.toUpperCase();

		if (_containsTabs(value)) {
			LOGGER.info("The " + message + " cannot contain tabs. Please remove any tabs from the " + message + ".");

			throw new IllegalArgumentException(
					"The " + message + " cannot contain tabs. Please remove any tabs from the " + message + ".");
		}
		if (_containsWhitespace(value)) {
			LOGGER.info("The " + message + " cannot contain whitespaces. Please remove any whitespaces from the "
					+ message + ".");

			throw new IllegalArgumentException("The " + message
					+ " cannot contain whitespaces. Please remove any whitespaces from the " + message + ".");
		}
		if (!EnumUtils.isValidEnum(enumType, value)) {
			LOGGER.info("The specified " + message + " was not found. Please provide a valid " + message + ".");

			throw new NoSuchElementException(
					"The specified " + message + " was not found. Please provide a valid " + message + ".");
		}
		return Enum.valueOf(enumType, value);
	}

	public String validateSearchString(String searchString) {
		if (searchString == null || searchString == "") {
			LOGGER.info("The search Text field cannot be empty.");
			throw new NullPointerException("The search Text field cannot be empty.");
		}
		if (searchString.length() > 20) {
			LOGGER.info("The search Text cannot exceed 20 characters. Please provide a shorter search Text.");

			throw new IllegalArgumentException(
					"The search Text cannot exceed 20 characters. Please provide a shorter search Text.");
		}

		if (_containsTabs(searchString)) {
			LOGGER.info("The search Text cannot contain tabs. Please remove any tabs from the search Text.");

			throw new IllegalArgumentException(
					"The search Text cannot contain tabs. Please remove any tabs from the search Text.");
		}
		return searchString;
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
		Pattern pattern = Pattern.compile("\t");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	private boolean _containsWhitespace(String str) {
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

}
