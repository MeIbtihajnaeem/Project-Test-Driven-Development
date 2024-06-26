/**
 * Unit tests for the validateDate method in the ValidationConfigurations interface.
 *
 * These tests ensure that the validateDate method correctly handles various
 * edge cases. The tests utilise AssertJ for exception assertions and equality assertions.
 *
 * The setup method initialises an instance of ExtendedValidationConfigurations.
 *
 * Test cases include:
 * - Null address string
 * - Previous two days date
 * - Previous one day date
 * - After seven month date
 * - After six month date
 * - With current date
 *
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 */
package com.mycompany.orderassignmentsystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;

/**
 * The Class ValidationConfigurationsForValidateDateTimeMethodTest.
 */
public class ValidationConfigurationsForValidateDateTimeMethodTest {

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
	 * Test date time method with null date time.
	 */
	@Test
	public void testDateTimeMethodWithNullDateTime() {

		// Setup & Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDate(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The Date field cannot be empty.");
	}

	/**
	 * Test date time method with previous two days date.
	 */
	@Test
	public void testDateTimeMethodWithPreviousTwoDaysDate() {
		// Setup
		LocalDate dateTime = LocalDate.now();
		LocalDate twoDaysBeforeToday = dateTime.minusDays(2);

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDate(twoDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not before today's date.");
	}

	/**
	 * Test date time method with previous one days date.
	 */
	@Test
	public void testDateTimeMethodWithPreviousOneDaysDate() {
		// Setup
		LocalDate dateTime = LocalDate.now();
		LocalDate oneDaysBeforeToday = dateTime.minusDays(1);

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDate(oneDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not before today's date.");
	}

	/**
	 * Test date time method with current date.
	 */
	@Test
	public void testDateTimeMethodWithCurrentDate() {
		// Setup
		LocalDate dateTime = LocalDate.now();

		// Exercise & Verify
		assertThat(validationConfigurations.validateDate(dateTime)).isEqualTo(dateTime);
	}

	/**
	 * Test date time method with after seven month date.
	 */
	@Test
	public void testDateTimeMethodWithAfterSevenMonthDate() {
		// Setup
		LocalDate dateTime = LocalDate.now();
		LocalDate sevenDaysBeforeToday = dateTime.plusMonths(7);

		// Exercise & Verify
		assertThatThrownBy(() -> validationConfigurations.validateDate(sevenDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not 6 months after today's date.");
	}

	/**
	 * Test date time method with date plus six months.
	 */
	@Test
	public void testDateTimeMethodWithDatePlusSixMonths() {
		// Setup
		LocalDate dateTime = LocalDate.now().plusMonths(6);

		// Exercise & Verify
		assertThat(validationConfigurations.validateDate(dateTime)).isEqualTo(dateTime);
	}

}
