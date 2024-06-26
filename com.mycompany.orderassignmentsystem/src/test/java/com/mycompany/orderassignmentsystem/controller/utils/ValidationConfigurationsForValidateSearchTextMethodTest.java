/**
 * Unit tests for the validateSearchString method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validateSearchString method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - Search text with null string.
 * - Search text with empty string.
 * - Search text with large string greater than twenty characters.
 * - Search text with large string equals to twenty characters.
 * - Search text with tabs.
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
 * The Class ValidationConfigurationsForValidateSearchTextMethodTest.
 */
public class ValidationConfigurationsForValidateSearchTextMethodTest {

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
	 * Test validate search string with null string.
	 */
	@Test
	public void testValidateSearchStringWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateSearchString(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The search Text field cannot be empty.");
	}

	/**
	 * Test validate search string with empty string.
	 */
	@Test
	public void testValidateSearchStringWithEmptyString() {
		// Setup
		String searchText = "";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateSearchString(searchText))
				.isInstanceOf(NullPointerException.class).hasMessage("The search Text field cannot be empty.");
	}

	/**
	 * Test validate search string with large string greater than twenty characters.
	 */
	@Test
	public void testValidateSearchStringWithLargeStringGreaterThanTwentyCharacters() {
		// Setup
		String searchText = "Muhammad Ibtihaj Naee";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateSearchString(searchText))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The search Text cannot exceed 20 characters. Please provide a shorter search Text.");
	}

	/**
	 * Test validate search string with large string equals to twenty characters.
	 */
	@Test
	public void testValidateSearchStringWithLargeStringEqualsToTwentyCharacters() {
		// Setup
		String searchText = "Muhammad Ibtihaj Nae";

		// Exercise & Verify
		assertThat(validationConfigurations.validateSearchString(searchText)).isEqualTo(searchText);

	}

	/**
	 * Test validate search string with tabs.
	 */
	@Test
	public void testValidateSearchStringWithTabs() {
		// Setup
		String searchText = "test\tSearchText";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateSearchString(searchText))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The search Text cannot contain tabs. Please remove any tabs from the search Text.");
	}

}
