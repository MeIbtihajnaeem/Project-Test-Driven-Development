/**
 * Unit tests for the validateDescription method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validateDescription method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - Description with null string.
 * - Description with empty string
 * - Description with short string less than ten characters.
 * - Description with short string equals to ten characters.
 * - Description with large string equals to eleven characters.
 * - Description with large string greater than fifty characters.
 * - Description with large string equals to fifty characters.
 * - Description with tabs.
 * - Description with one leading white space.
 * - Description with two leading white space.
 * - Description with one ending white space.
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
 * The Class ValidationConfigurationsForValidateDescriptionMethodTest.
 */
public class ValidationConfigurationsForValidateDescriptionMethodTest {

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
	 * Test description method with null string.
	 */
	@Test
	public void testDescriptionMethodWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDescription(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The description field cannot be empty.");
	}

	/**
	 * Test description method with empty string.
	 */
	@Test
	public void testDescriptionMethodWithEmptyString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDescription(""))
				.isInstanceOf(NullPointerException.class).hasMessage("The description field cannot be empty.");
	}

	/**
	 * Test description method with short string less than ten characters.
	 */
	@Test
	public void testDescriptionMethodWithShortStringLessThanTenCharacters() {
		// Setup
		String description = "repair";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDescription(description))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description must be at least 10 characters long. Please provide a valid description.");
	}

	/**
	 * Test description method with short string equals to ten characters.
	 */
	@Test
	public void testDescriptionMethodWithShortStringEqualsToTenCharacters() {
		// Setup
		String description = "change pip";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDescription(description))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description must be at least 10 characters long. Please provide a valid description.");
	}

	/**
	 * Test description method with large string equals to eleven characters.
	 */
	@Test
	public void testDescriptionMethodWithLargeStringEqualsToElevenCharacters() {
		// Setup
		String description = "change pips";

		// Exercise & Verify
		assertThat(validationConfigurations.validateDescription(description)).isEqualTo(description);
	}

	/**
	 * Test description method with large string greater than fifty characters.
	 */
	@Test
	public void testDescriptionMethodWithLargeStringGreaterThanFiftyCharacters() {
		// Setup
		String description = "Please ensure the pipes are tightly sealed and all!";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDescription(description))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description cannot exceed 50 characters. Please provide a shorter description.");
	}

	/**
	 * Test description method with large string equals to fifty characters.
	 */
	@Test
	public void testDescriptionMethodWithLargeStringEqualsToFiftyCharacters() {
		// Setup
		String description = "Please ensure all connection are leak-proof.Thanks";

		// Exercise & Verify
		assertThat(validationConfigurations.validateDescription(description)).isEqualTo(description);
	}

	/**
	 * Test description method with tabs.
	 */
	@Test
	public void testDescriptionMethodWithTabs() {
		// Setup
		String description = "Please ensure all connection \t are leak-proof.";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDescription(description))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description cannot contain tabs. Please remove any tabs from the description.");
	}

	/**
	 * Test description method with one leading white space.
	 */
	@Test
	public void testDescriptionMethodWithOneLeadingWhiteSpace() {
		// Setup
		String description = " Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";

		// Exercise & Verify
		assertThat(validationConfigurations.validateDescription(description)).isEqualTo(expectedDescription);
	}

	/**
	 * Test description method with two leading white space.
	 */
	@Test
	public void testDescriptionMethodWithTwoLeadingWhiteSpace() {
		// Setup
		String description = "  Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";

		// Exercise & Verify
		assertThat(validationConfigurations.validateDescription(description)).isEqualTo(expectedDescription);

	}

	/**
	 * Test description method with one ending white space.
	 */
	@Test
	public void testDescriptionMethodWithOneEndingWhiteSpace() {
		// Setup
		String description = "Please ensure all connection are leak-proof. ";
		String expectedDescription = "Please ensure all connection are leak-proof.";

		// Exercise & Verify
		assertThat(validationConfigurations.validateDescription(description)).isEqualTo(expectedDescription);

	}

}
