/**
 * Unit tests for the validatePhoneNumber method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validatePhoneNumber method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - Phone number with null string.
 * - Phone number with empty string.
 * - Phone number with short string less than ten characters.
 * - Phone number with long string greater than ten
 * - Phone number with long string equals ten characters.
 * - Phone number with special characters.
 * - Phone number with alphabet characters.
 * - Phone number with middle white space characters.
 * - Phone number with leading white space characters.
 * - Phone number with ending white space characters.
 * - Phone number with tabs characters.
 * - Phone number with leading number except three.
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
 * The Class ValidationConfigurationsForValidatePhoneNumberMethodTest.
 */
public class ValidationConfigurationsForValidatePhoneNumberMethodTest {

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
	 * Test validate phone number method with null string.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The phone number field cannot be empty.");
	}

	/**
	 * Test phone number with empty string.
	 */
	@Test
	public void testPhoneNumberWithEmptyString() {
		// Setup
		String phoneNumber = "";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(NullPointerException.class).hasMessage("The phone number field cannot be empty.");
	}

	/**
	 * Test validate phone number method with short string less than ten characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithShortStringLessThanTenCharacters() {
		// Setup
		String phoneNumber = "000000000";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The phone number must be 10 characters long. Please provide a valid phone number.");
	}

	/**
	 * Test validate phone number method with long string greater than ten
	 * characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithLongStringGreaterThanTenCharacters() {
		// Setup
		String phoneNumber = "00000000000";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The phone number must be 10 characters long. Please provide a valid phone number.");
	}

	/**
	 * Test validate phone number method with long string equals ten characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithLongStringEqualsTenCharacters() {
		// Setup
		String phoneNumber = "3401372678";

		// Exercise & Verify
		assertThat(validationConfigurations.validatePhoneNumber(phoneNumber)).isEqualTo(phoneNumber);

	}

	/**
	 * Test validate phone number method with special characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithSpecialCharacters() {
		// Setup
		String phoneNumber = "3401372@78";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	/**
	 * Test validate phone number method with alphabet characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithAlphabetCharacters() {
		// Setup
		String phoneNumber = "3401372a78";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	/**
	 * Test validate phone number method with middle white space characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithMiddleWhiteSpaceCharacters() {
		// Setup
		String phoneNumber = "3401372 78";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	/**
	 * Test validate phone number method with leading white space characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithLeadingWhiteSpaceCharacters() {
		// Setup
		String phoneNumber = " 401372078";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	/**
	 * Test validate phone number method with ending white space characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithEndingWhiteSpaceCharacters() {
		// Setup
		String phoneNumber = "340137207 ";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	/**
	 * Test validate phone number method with tabs characters.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithTabsCharacters() {
		// Setup
		String phoneNumber = "\t401372073";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	/**
	 * Test validate phone number method with leading number except three.
	 */
	@Test
	public void testValidatePhoneNumberMethodWithLeadingNumberExceptThree() {
		// Setup
		String phoneNumber = "4401372078";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(phoneNumber))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The phone number must start with 3. Please provide a valid phone number.");
	}

}
