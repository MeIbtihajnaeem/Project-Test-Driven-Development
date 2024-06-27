/*
 * Unit tests for the validateCategory, validateStatus and validateEnum methods in the ValidationConfigurations interface.
 *
 * These tests ensure that the enumeration validation methods correctly handle various edge cases.
 * The tests utilise AssertJ for exception assertions and equality checks.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases for validateCategory include:
 * - Null category value
 * - Valid category enum value
 * 
 * Test cases for validateEnum for category include:
 * - Null string for category
 * - Empty string for category
 * - String with tabs for category
 * - String with whitespaces for category
 * - Valid string value for category
 * - Invalid string value for category
 *
 * Test cases for validateStatus include:
 * - Null status value
 * - Valid status enum value
 * 
 * Test cases for validateEnum for status include:
 * - Null string for status
 * - Empty string for status
 * - String with tabs for status
 * - String with whitespaces for status
 * - Valid string value for status
 * - Invalid string value for status
 *
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 */

package com.mycompany.orderassignmentsystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;

/**
 * The Class ValidationConfigurationsForValidateEnumerationMethodsTest.
 */
public class ValidationConfigurationsForValidateEnumerationMethodsTest {

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
	 * Test validate category method for category enum with null enum value.
	 */
	@Test
	public void testValidateCategoryMethodForCategoryEnumWithNullEnumValue() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateCategory(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The category field cannot be empty.");
	}

	/**
	 * Test validate category method for category enum with valid enum value.
	 */
	@Test
	public void testValidateCategoryMethodForCategoryEnumWithValidEnumValue() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		// Exercise & Verify
		assertThat(validationConfigurations.validateCategory(category)).isEqualTo(category);
	}

	/**
	 * Test validate status method for status enum with null enum value.
	 */
	@Test
	public void testValidateStatusMethodForStatusEnumWithNullEnumValue() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateStatus(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The status field cannot be empty.");
	}

	/**
	 * Test validate status method for status enum with valid enum value.
	 */
	@Test
	public void testValidateStatusMethodForStatusEnumWithValidEnumValue() {
		// Setup
		OrderStatus status = OrderStatus.PENDING;

		// Exercise & Verify
		assertThat(validationConfigurations.validateStatus(status)).isEqualTo(status);

	}

	/**
	 * Test validate enum method for category enum with null string.
	 */
	@Test
	public void testValidateEnumMethodForCategoryEnumWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(null, OrderCategory.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The category field cannot be empty.");
	}

	/**
	 * Test validate enum method for category enum with empty string.
	 */
	@Test
	public void testValidateEnumMethodForCategoryEnumWithEmptyString() {
		// Setup
		String category = "";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(category, OrderCategory.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The category field cannot be empty.");
	}

	/**
	 * Test validate enum method for category enum with tabs in string.
	 */
	@Test
	public void testValidateEnumMethodForCategoryEnumWithTabsInString() {
		// Setup
		String category = "\t";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(category, OrderCategory.class))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The category cannot contain tabs. Please remove any tabs from the category.");
	}

	/**
	 * Test validate enum method for category enum with white spaces in string.
	 */
	@Test
	public void testValidateEnumMethodForCategoryEnumWithWhiteSpacesInString() {
		// Setup
		String category = " Plumber ";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(category, OrderCategory.class))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The category cannot contain whitespaces. Please remove any whitespaces from the category.");
	}

	/**
	 * Test validate enum method for category with valid string value.
	 */
	@Test
	public void testValidateEnumMethodForCategoryWithValidStringValue() {
		// Setup
		OrderCategory expectedResult = OrderCategory.PLUMBER;
		// Exercise & Verify
		assertThat(validationConfigurations.validateEnum("PLUMBER", OrderCategory.class)).isEqualTo(expectedResult);

	}

	/**
	 * Test validate enum method for category with in valid string value.
	 */
	@Test
	public void testValidateEnumMethodForCategoryWithInValidStringValue() {
		// Setup
		String category = "Plumbers";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(category, OrderCategory.class))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("The specified category was not found. Please provide a valid category.");
	}

	/**
	 * Test validate enum method for status enum with null string.
	 */
	@Test
	public void testValidateEnumMethodForStatusEnumWithNullString() {
		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(null, OrderStatus.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The status field cannot be empty.");
	}

	/**
	 * Test validate enum method for status enum with empty string.
	 */
	@Test
	public void testValidateEnumMethodForStatusEnumWithEmptyString() {
		// Setup
		String status = "";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(status, OrderStatus.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The status field cannot be empty.");
	}

	/**
	 * Test validate enum method for status enum with tabs in string.
	 */
	@Test
	public void testValidateEnumMethodForStatusEnumWithTabsInString() {
		// Setup
		String status = "\t";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(status, OrderStatus.class))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The status cannot contain tabs. Please remove any tabs from the status.");
	}

	/**
	 * Test validate enum method for status enum with white spaces in string.
	 */
	@Test
	public void testValidateEnumMethodForStatusEnumWithWhiteSpacesInString() {
		// Setup
		String status = " PENDING ";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(status, OrderStatus.class))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The status cannot contain whitespaces. Please remove any whitespaces from the status.");
	}

	/**
	 * Test validate enum method for status with valid string value.
	 */
	@Test
	public void testValidateEnumMethodForStatusWithValidStringValue() {
		// Setup
		OrderStatus expectedResult = OrderStatus.PENDING;

		// Exercise & Verify
		assertThat(validationConfigurations.validateEnum("PENDING", OrderStatus.class)).isEqualTo(expectedResult);

	}

	/**
	 * Test validate enum method for status with invalid string value.
	 */
	@Test
	public void testValidateEnumMethodForStatusWithInvalidStringValue() {
		// Setup
		String status = "Complete";

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateEnum(status, OrderStatus.class))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("The specified status was not found. Please provide a valid status.");
	}

}
