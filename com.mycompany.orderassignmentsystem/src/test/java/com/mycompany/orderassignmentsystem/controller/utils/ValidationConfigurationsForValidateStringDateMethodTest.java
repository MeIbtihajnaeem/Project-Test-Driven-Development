/*
 * Unit tests for the validateStringDate method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validateStringDate method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - Date with null string.
 * - Date with empty string.
 * - Date with string less then nine length.
 * - Date with string equal to nine length.
 * - Date with string greater then nine length.
 * - Date with in valid string date.
 * - Date with valid string date.
 *
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 */

package com.mycompany.orderassignmentsystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;

/**
 * The Class ValidationConfigurationsForValidateStringDateMethodTest.
 */
public class ValidationConfigurationsForValidateStringDateMethodTest {

	/** The validation configurations. */
	private ValidationConfigurations validationConfigurations;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		validationConfigurations = new ExtendedValidationConfigurations();
	}

	/**
	 * Test validate string date method with null string.
	 */
	@Test
	public void testValidateStringDateMethodWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(null))
				.isInstanceOf(NullPointerException.class).hasMessage("Date cannot be null.");
	}

	/**
	 * Test validate string date method with empty string.
	 */
	@Test
	public void testValidateStringDateMethodWithEmptyString() {
		// Setup
		String date = "";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(date))
				.isInstanceOf(NullPointerException.class).hasMessage("Date cannot be empty.");
	}

	/**
	 * Test validate string date method with string less then nine length.
	 */
	@Test
	public void testValidateStringDateMethodWithStringLessThenNineLength() {
		// Setup
		String date = "12/12/";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(date))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Date must not be less then 10 characters.");
	}

	/**
	 * Test validate string date method with string equal to nine length.
	 */
	@Test
	public void testValidateStringDateMethodWithStringEqualToNineLength() {
		// Setup
		String date = "12/12/202";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(date))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Date must not be less then 10 characters.");
	}

	/**
	 * Test validate string date method with string greater then nine length.
	 */
	@Test
	public void testValidateStringDateMethodWithStringGreaterThenNineLength() {
		// Setup
		String date = "12/12/20224";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(date))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Date must not be greater then 10 characters.");
	}

	/**
	 * Test validate string date method with in valid string date.
	 */
	@Test
	public void testValidateStringDateMethodWithInValidStringDate() {

		// Setup
		String pattern = "dd-MM-yyyy";
		String date = "12/12-2024";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(date))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please ensure that the date follows the format" + pattern);
	}

	/**
	 * Test validate string date method with valid string date.
	 */
	@Test
	public void testValidateStringDateMethodWithValidStringDate() {
		// Setup
		String validStringDate = "30-12-2024";

		// Exercise & Verify
		assertThat(validationConfigurations.validateStringDate(validStringDate)).isEqualTo(validStringDate);

	}

}
