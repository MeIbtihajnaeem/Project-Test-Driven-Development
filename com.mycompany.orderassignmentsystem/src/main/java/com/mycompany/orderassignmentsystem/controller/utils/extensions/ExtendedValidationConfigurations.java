package com.mycompany.orderassignmentsystem.controller.utils.extensions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;

public class ExtendedValidationConfigurations implements ValidationConfigurations {

	private static final Logger LOGGER = LogManager.getLogger(ExtendedValidationConfigurations.class);

	public String validateName(String name) {
		if (name == null || name.equals("")) {
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

		if (containsSpecialCharacters(name)) {
			LOGGER.info(
					"The name cannot contain special characters. Please remove any special characters from the name.");

			throw new IllegalArgumentException(
					"The name cannot contain special characters. Please remove any special characters from the name.");
		}

		if (containsNumbers(name)) {
			LOGGER.info("The name cannot contain numbers. Please remove any number from the name.");

			throw new IllegalArgumentException(
					"The name cannot contain numbers. Please remove any number from the name.");
		}
		if (containsTabs(name)) {
			LOGGER.info("The name cannot contain tabs. Please remove any tabs from the name.");

			throw new IllegalArgumentException("The name cannot contain tabs. Please remove any tabs from the name.");
		}
		name = name.trim();
		return name;
	}

	public Long validateStringNumber(String str) {
		if (str == null || str.equals("")) {
			LOGGER.info("The text cannot be empty.");
			throw new NullPointerException("The number cannot be empty.");
		}
		if (str.length() > 10) {
			LOGGER.info("The number cannot exceed 20 characters. Please provide a shorter number.");

			throw new IllegalArgumentException(
					"The number cannot exceed 20 characters. Please provide a shorter number.");
		}
		if (containsWhitespace(str)) {
			LOGGER.info("The number cannot contains whitespace. Please provide a valid number.");

			throw new IllegalArgumentException("The number cannot contains whitespace. Please provide a valid number.");
		}

		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Please enter a valid number.");
		}

		return Long.parseLong(str);

	}

	public String validateAddress(String address) {
		if (address == null || address.equals("")) {
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
		if (containsTabs(address)) {
			LOGGER.info("The address cannot contain tabs. Please remove any tabs from the address.");

			throw new IllegalArgumentException(
					"The address cannot contain tabs. Please remove any tabs from the address.");
		}
		address = address.trim();
		return address;
	}

	public String validateDescription(String description) {
		if (description == null || description.equals("")) {
			LOGGER.info("The description field cannot be empty.");

			throw new NullPointerException("The description field cannot be empty.");
		}
		if (description.length() <= 10) {
			LOGGER.info("The description must be at least 10 characters long. Please provide a valid description.");

			throw new IllegalArgumentException(
					"The description must be at least 10 characters long. Please provide a valid description.");
		}
		if (description.length() > 50) {
			LOGGER.info("The description cannot exceed 50 characters. Please provide a shorter description.");

			throw new IllegalArgumentException(
					"The description cannot exceed 50 characters. Please provide a shorter description.");
		}
		if (containsTabs(description)) {
			LOGGER.info("The description cannot contain tabs. Please remove any tabs from the description.");

			throw new IllegalArgumentException(
					"The description cannot contain tabs. Please remove any tabs from the description.");
		}
		description = description.trim();
		return description;
	}

	public String validatePhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.equals("")) {
			LOGGER.info("The phone number field cannot be empty.");

			throw new NullPointerException("The phone number field cannot be empty.");
		}
		if (phoneNumber.length() != 10) {
			LOGGER.info("The phone number must be 10 characters long. Please provide a valid phone number.");

			throw new IllegalArgumentException(
					"The phone number must be 10 characters long. Please provide a valid phone number.");
		}
		if (!phoneNumber.matches("\\d+")) {
			LOGGER.info(
					"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");

			throw new IllegalArgumentException(
					"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
		}
		if (!phoneNumber.matches("^3\\d*$")) {
			LOGGER.info("The phone number must start with 3. Please provide a valid phone number.");

			throw new IllegalArgumentException(
					"The phone number must start with 3. Please provide a valid phone number.");
		}
		return phoneNumber;
	}

	public String validateStringDate(String dateString) {

		String pattern = "dd-MM-yyyy";
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		if (dateString == null) {
			throw new NullPointerException("Date cannot be null.");
		}
		if (dateString.equals("")) {
			throw new NullPointerException("Date cannot be empty.");
		}
		if (dateString.length() <= 9) {
			throw new IllegalArgumentException("Date must not be less then 10 characters.");
		}
		if (dateString.length() > 10) {
			throw new IllegalArgumentException("Date must not be greater then 10 characters.");
		}
		try {
			LocalDate.parse(dateString, formatter);
			return dateString;
		} catch (Exception e) {
			throw new IllegalArgumentException("Please ensure that the date follows the format" + pattern);
		}

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
			LOGGER.info("The {} field cannot be empty.", message);

			throw new NullPointerException("The " + message + " field cannot be empty.");
		}
		value = value.toUpperCase();

		if (containsTabs(value)) {
			LOGGER.info("The {} cannot contain tabs. Please remove any tabs from the {}.", message, message);

			throw new IllegalArgumentException(
					String.format("The %s cannot contain tabs. Please remove any tabs from the %s.", message, message));
		}
		if (containsWhitespace(value)) {
			LOGGER.info("The {} cannot contain whitespaces. Please remove any whitespaces from the {}.", message,
					message);

			throw new IllegalArgumentException("The " + message
					+ " cannot contain whitespaces. Please remove any whitespaces from the " + message + ".");
		}
		if (!EnumUtils.isValidEnum(enumType, value)) {
			LOGGER.info("The specified {} was not found. Please provide a valid {}.", message, message);

			throw new NoSuchElementException(
					"The specified " + message + " was not found. Please provide a valid " + message + ".");
		}
		return Enum.valueOf(enumType, value);
	}

	public String validateSearchString(String searchString) {
		if (searchString == null || searchString.equals("")) {
			LOGGER.info("The search Text field cannot be empty.");
			throw new NullPointerException("The search Text field cannot be empty.");
		}
		if (searchString.length() > 20) {
			LOGGER.info("The search Text cannot exceed 20 characters. Please provide a shorter search Text.");

			throw new IllegalArgumentException(
					"The search Text cannot exceed 20 characters. Please provide a shorter search Text.");
		}

		if (containsTabs(searchString)) {
			LOGGER.info("The search Text cannot contain tabs. Please remove any tabs from the search Text.");

			throw new IllegalArgumentException(
					"The search Text cannot contain tabs. Please remove any tabs from the search Text.");
		}
		return searchString;
	}

	/**
	 * Checks if a string contains special characters.
	 *
	 * @param str the string to check
	 * @return true, if the string contains special characters, false otherwise
	 */
	private boolean containsSpecialCharacters(String str) {
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	/**
	 * Checks if a string contains numbers.
	 *
	 * @param str the string to check
	 * @return true, if the string contains numbers, false otherwise
	 */
	private boolean containsNumbers(String str) {
		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	/**
	 * Checks if a string contains tabs.
	 *
	 * @param str the string to check
	 * @return true, if the string contains tabs, false otherwise
	 */
	private boolean containsTabs(String str) {
		Pattern pattern = Pattern.compile("\t");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	/**
	 * Checks if a string contains whitespace.
	 *
	 * @param str the string to check
	 * @return true, if the string contains whitespace, false otherwise
	 */
	private boolean containsWhitespace(String str) {
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
}
