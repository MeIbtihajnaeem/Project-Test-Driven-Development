/**
 * Unit tests for the validateStringNumber method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validateStringNumber method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - String number with null string.
 * - String number with empty string.
 * - String number with large string greater than twenty
 * - String number with large string exactly than twenty
 * - String number with alphabetic characters.
 * - String number with leading white space.
 * - String number with ending white space.
 * - String number with valid number.
 * - String number with negative number.
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
 * The Class ValidationConfigurationsForValidateStringNumberMethodTest.
 */
public class ValidationConfigurationsForValidateStringNumberMethodTest {

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
	 * Test validate string number method with null string.
	 */
	@Test
	public void testValidateStringNumberMethodWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The number cannot be empty.");
	}

	/**
	 * Test validate string number method with empty string.
	 */
	@Test
	public void testValidateStringNumberMethodWithEmptyString() {
		// Setup
		String phoneNumber = "";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(phoneNumber))
				.isInstanceOf(NullPointerException.class).hasMessage("The number cannot be empty.");
	}

	/**
	 * Test validate string number method with large string greater than twenty
	 * characters.
	 */
	@Test
	public void testValidateStringNumberMethodWithLargeStringGreaterThanTwentyCharacters() {
		// Setup
		String number = "00000000000";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(number))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot exceed 20 characters. Please provide a shorter number.");
	}

	/**
	 * Test validate string number method with large string exactly than twenty
	 * characters.
	 */
	@Test
	public void testValidateStringNumberMethodWithLargeStringExactlyThanTwentyCharacters() {
		// Setup
		Long number = 1234567891l;

		// Exercise & Verify
		assertThat(validationConfigurations.validateStringNumber(number.toString())).isEqualTo(number);

	}

	/**
	 * Test validate string number method with alphabetic characters.
	 */
	@Test
	public void testValidateStringNumberMethodWithAlphabeticCharacters() {
		// Setup
		String number = "3401372a78";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(number))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please enter a valid number.");
	}

	/**
	 * Test validate string number method with leading white space.
	 */
	@Test
	public void testValidateStringNumberMethodWithLeadingWhiteSpace() {
		// Setup
		String number = " 12367890";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(number))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot contains whitespace. Please provide a valid number.");
	}

	/**
	 * Test validate string number method with ending white space.
	 */
	@Test
	public void testValidateStringNumberMethodWithEndingWhiteSpace() {
		// Setup
		String number = "12567890 ";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(number))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot contains whitespace. Please provide a valid number.");
	}

	/**
	 * Test validate string number method with middle white space.
	 */
	@Test
	public void testValidateStringNumberMethodWithMiddleWhiteSpace() {
		// Setup
		String number = "12345 678";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(number))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot contains whitespace. Please provide a valid number.");
	}

	/**
	 * Test validate string number method with valid number.
	 */
	@Test
	public void testValidateStringNumberMethodWithValidNumber() {
		// Setup
		Long number = 1234567890l;

		// Exercise & Verify
		assertThat(validationConfigurations.validateStringNumber(number.toString())).isEqualTo(number);

	}

	/**
	 * Test validate string number method with negative number.
	 */
	@Test
	public void testValidateStringNumberMethodWithNegativeNumber() {
		// Setup
		String number = "-123l";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(number))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please enter a valid number.");
	}

}