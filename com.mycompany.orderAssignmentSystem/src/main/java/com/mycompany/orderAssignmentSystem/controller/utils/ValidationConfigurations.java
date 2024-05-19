/*
 * ValidationConfigurations: Utility class for input validation.
 */
package com.mycompany.orderAssignmentSystem.controller.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;

/**
 * Utility class for validating input fields.
 */
public interface ValidationConfigurations {

	/**
	 * Validate name.
	 *
	 * @param name the name
	 * @return the string
	 * @throws IllegalArgumentException if validation fails
	 */
	public String validateName(String name);

	/**
	 * Validates a numeric string.
	 * 
	 * @param str the string to validate
	 * @return the validated numeric value
	 * @throws IllegalArgumentException if validation fails
	 */
	public long validateStringNumber(String str);

	/**
	 * Validates an address.
	 *
	 * @param address the address to validate
	 * @return the validated address
	 * @throws IllegalArgumentException if validation fails
	 */
	public String validateAddress(String address);

	/**
	 * Validates a description.
	 *
	 * @param description the description to validate
	 * @return the validated description
	 * @throws IllegalArgumentException if validation fails
	 */
	public String validateDescription(String description);

	/**
	 * Validates a phone number.
	 *
	 * @param phoneNumber the phone number to validate
	 * @return the validated phone number
	 * @throws IllegalArgumentException if validation fails
	 */
	public String validatePhoneNumber(String phoneNumber);

	/**
	 * Validates an ID.
	 *
	 * @param id the ID to validate
	 * @return the validated ID
	 * @throws IllegalArgumentException if validation fails
	 */
	public long validateId(Long id);

	/**
	 * Validates a date.
	 *
	 * @param dateTime the date to validate
	 * @return the validated date
	 * @throws IllegalArgumentException if validation fails
	 */
	public LocalDateTime validateDate(LocalDateTime dateTime);

	/**
	 * Validates a string date.
	 *
	 * @param dateString the string date to validate
	 * @return the validated date
	 * @throws IllegalArgumentException if validation fails
	 */
	public LocalDate validateStringDate(String dateString);

	/**
	 * Validates an order category.
	 *
	 * @param category the category to validate
	 * @return the validated category
	 * @throws IllegalArgumentException if validation fails
	 */
	public OrderCategory validateCategory(OrderCategory category);

	/**
	 * Validates an order status.
	 *
	 * @param status the status to validate
	 * @return the validated status
	 * @throws IllegalArgumentException if validation fails
	 */
	public OrderStatus validateStatus(OrderStatus status);

	/**
	 * Validates an enum value.
	 *
	 * @param <T>      the generic type of the enum
	 * @param value    the value to validate
	 * @param enumType the enum type
	 * @return the validated enum value
	 * @throws IllegalArgumentException if validation fails
	 */
	public <T extends Enum<T>> T validateEnum(String value, Class<T> enumType);

	/**
	 * Validates a search string.
	 *
	 * @param searchString the search string to validate
	 * @return the validated search string
	 * @throws IllegalArgumentException if validation fails
	 */
	public String validateSearchString(String searchString);

}
