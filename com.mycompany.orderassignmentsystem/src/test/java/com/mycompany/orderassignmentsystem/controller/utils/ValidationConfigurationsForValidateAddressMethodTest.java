/*
 * Unit tests for the validateAddress method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validateAddress method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - Null address string
 * - Empty address string
 * - Short address string (less than 10 characters)
 * - Address string exactly 10 characters long
 * - Address string 11 characters long
 * - Overly long address string (greater than 50 characters)
 * - Address string containing tabs
 * - Address string with leading whitespace
 * - Address string with trailing whitespace
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
 * The Class ValidationConfigurationsForValidateAddressMethodTest.
 */
public class ValidationConfigurationsForValidateAddressMethodTest {

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
	 * Test Validate Address Method with null string.
	 */
	@Test
	public void testValidateAddressMethodWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateAddress(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The address field cannot be empty.");
	}

	/**
	 * Test Validate Address Method with empty string.
	 */
	@Test
	public void testValidateAddressMethodWithEmptyString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateAddress("")).isInstanceOf(NullPointerException.class)
				.hasMessage("The address field cannot be empty.");
	}

	/**
	 * Test Validate Address Method with short string less than ten characters.
	 */
	@Test
	public void testValidateAddressMethodWithShortStringLessThanTenCharacters() {
		// Setup
		String address = "42 Will";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateAddress(address))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address must be at least 10 characters long. Please provide a valid Address.");
	}

	/**
	 * Test Validate Address Method with short string equals to ten characters.
	 */
	@Test
	public void testValidateAddressMethodWithShortStringEqualsToTenCharacters() {
		// Setup
		String address = "42 Willowa";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateAddress(address))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address must be at least 10 characters long. Please provide a valid Address.");
	}

	/**
	 * Test Validate Address Method with large string equals to eleven characters.
	 */
	@Test
	public void testValidateAddressMethodWithLargeStringEqualsToElevenCharacters() {
		// Setup
		String address = "1234 Main S";

		// Exercise & Verify
		assertThat(validationConfigurations.validateAddress(address)).isEqualTo(address);
	}

	/**
	 * Test Validate Address Method with large string greater than fifty characters.
	 */
	@Test
	public void testValidateAddressMethodWithLargeStringGreaterThanFiftyCharacters() {
		// Setup
		String address = "123 Main Street Near Bakary, Apt 10, Springfield, 1";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateAddress(address))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address cannot exceed 50 characters. Please provide a shorter Address.");
	}

	/**
	 * Test Validate Address Method with tabs.
	 */
	@Test
	public void testValidateAddressMethodWithTabs() {
		// Setup
		String address = "1234 Main Street\t Apt 101, Springfield, USA 12345";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateAddress(address))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The address cannot contain tabs. Please remove any tabs from the address.");
	}

	/**
	 * Test Validate Address Method with one leading white space.
	 */
	@Test
	public void testValidateAddressMethodWithOneLeadingWhiteSpace() {
		// Setup
		String address = " 123 Main Street , Apt 101, Springfield, USA 12345";
		String expectedAdddress = "123 Main Street , Apt 101, Springfield, USA 12345";

		// Exercise & Verify
		assertThat(validationConfigurations.validateAddress(address)).isEqualTo(expectedAdddress);

	}

	/**
	 * Test Validate Address Method with two leading white space.
	 */
	@Test
	public void testValidateAddressMethodWithTwoLeadingWhiteSpace() {
		// Setup
		String address = "  123 Main Street, Apt 101, Springfield, USA 12345";
		String expectedAdddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		// Exercise & Verify
		assertThat(validationConfigurations.validateAddress(address)).isEqualTo(expectedAdddress);

	}

	/**
	 * Test Validate Address Method with large string equals to fifty characters.
	 */
	@Test
	public void testValidateAddressMethodWithLargeStringEqualsToFiftyCharacters() {
		// Setup
		String address = "1234 Main Street , Apt 101, Springfield, USA 12345";

		// Exercise & Verify
		assertThat(validationConfigurations.validateAddress(address)).isEqualTo(address);

	}

	/**
	 * Test Validate Address Method with one ending white space.
	 */
	@Test
	public void testValidateAddressMethodWithOneEndingWhiteSpace() {
		// Setup
		String address = "123 Main Street, Apt 101, Springfield, USA 12345 ";
		String expectedAdddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		// Exercise & Verify
		assertThat(validationConfigurations.validateAddress(address)).isEqualTo(expectedAdddress);

	}
}
