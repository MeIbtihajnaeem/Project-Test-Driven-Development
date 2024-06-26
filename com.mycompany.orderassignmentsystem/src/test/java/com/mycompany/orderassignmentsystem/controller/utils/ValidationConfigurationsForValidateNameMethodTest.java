/**
 * Unit tests for the validateName method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validateName method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - Name with null string.
 * - Name with empty string.
 * - Name with short string less than two characters.
 * - Name with short string equals to two characters.
 * - Name wit short string equals to three characters.
 * - Name with large string greater than twenty characters.
 * - Name with large string equals to twenty characters.
 * - Name with special characters.
 * - Name with numbers.
 * - Name with tabs.
 * - Name with one leading white space.
 * - Name with two leading white space.
 * - Name with one middle white space.
 * - Name with one ending white space.
 * - Name with no space.
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
 * The Class ValidationConfigurationsForValidateNameMethodTest.
 */
public class ValidationConfigurationsForValidateNameMethodTest {

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
	 * Test validate name method with null string.
	 */
	@Test
	public void testValidateNameMethodWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty.");
	}

	/**
	 * Test validate name method with empty string.
	 */
	@Test
	public void testValidateNameMethodWithEmptyString() {
		// Setup
		String name = "";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(name)).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty.");
	}

	/**
	 * Test validate name method with short string less than two characters.
	 */
	@Test
	public void testValidateNameMethodWithShortStringLessThanTwoCharacters() {
		// Setup
		String name = "a";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(name))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name must be at least 3 characters long. Please provide a valid name.");
	}

	/**
	 * Test validate name method with short string equals to two characters.
	 */
	@Test
	public void testValidateNameMethodWithShortStringEqualsToTwoCharacters() {
		// Setup
		String name = "ab";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(name))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name must be at least 3 characters long. Please provide a valid name.");
	}

	/**
	 * Test validate name method wit short string equals to three characters.
	 */
	@Test
	public void testValidateNameMethodWitShortStringEqualsToThreeCharacters() {
		// Setup
		String name = "Ibt";

		// Exercise & Verify
		assertThat(validationConfigurations.validateName(name)).isEqualTo(name);
	}

	/**
	 * Test validate name method with large string greater than twenty characters.
	 */
	@Test
	public void testValidateNameMethodWithLargeStringGreaterThanTwentyCharacters() {
		// Setup
		String name = "Muhammad Ibtihaj Naee";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(name))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot exceed 20 characters. Please provide a shorter name.");
	}

	/**
	 * Test validate name method with large string equals to twenty characters.
	 */
	@Test
	public void testValidateNameMethodWithLargeStringEqualsToTwentyCharacters() {
		// Setup
		String name = "Muhammad Ibtihaj Nae";

		// Exercise & Verify
		assertThat(validationConfigurations.validateName(name)).isEqualTo(name);

	}

	/**
	 * Test validate name method with special characters.
	 */
	@Test
	public void testValidateNameMethodWithSpecialCharacters() {
		// Setup
		String name = "testName@";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(name))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The name cannot contain special characters. Please remove any special characters from the name.");
	}

	/**
	 * Test validate name method with numbers.
	 */
	@Test
	public void testValidateNameMethodWithNumbers() {
		// Setup
		String name = "testName123";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(name))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot contain numbers. Please remove any number from the name.");
	}

	/**
	 * Test validate name method with tabs.
	 */
	@Test
	public void testValidateNameMethodWithTabs() {
		// Setup
		String name = "test\tName";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateName(name))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot contain tabs. Please remove any tabs from the name.");
	}

	/**
	 * Test validate name method with one leading white space.
	 */
	@Test
	public void testValidateNameMethodWithOneLeadingWhiteSpace() {
		// Setup
		String name = " testName";
		String expected = "testName";

		// Exercise & Verify
		assertThat(validationConfigurations.validateName(name)).isEqualTo(expected);

	}

	/**
	 * Test validate name method with two leading white space.
	 */
	@Test
	public void testValidateNameMethodWithTwoLeadingWhiteSpace() {
		// Setup
		String name = "  testName";
		String expected = "testName";

		// Exercise & Verify
		assertThat(validationConfigurations.validateName(name)).isEqualTo(expected);
	}

	/**
	 * Test validate name method with one middle white space.
	 */
	@Test
	public void testValidateNameMethodWithOneMiddleWhiteSpace() {
		// Setup
		String name = "test Name";

		// Exercise & Verify
		assertThat(validationConfigurations.validateName(name)).isEqualTo(name);
	}

	/**
	 * Test validate name method with one ending white space.
	 */
	@Test
	public void testValidateNameMethodWithOneEndingWhiteSpace() {
		// Setup
		String name = "testName ";
		String expected = "testName";

		// Exercise & Verify
		assertThat(validationConfigurations.validateName(name)).isEqualTo(expected);
	}

	/**
	 * Test validate name method with no space.
	 */
	@Test
	public void testValidateNameMethodWithNoSpace() {
		// Setup
		String name = "testName";

		// Exercise & Verify
		assertThat(validationConfigurations.validateName(name)).isEqualTo(name);
	}

}
