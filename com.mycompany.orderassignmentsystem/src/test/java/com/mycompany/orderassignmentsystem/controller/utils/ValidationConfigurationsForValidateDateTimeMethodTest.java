package com.mycompany.orderassignmentsystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;

public class ValidationConfigurationsForValidateDateTimeMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ExtendedValidationConfigurations();
	}

	@Test
	public void testDateTimeMethodWithNullDateTime() {
		assertThatThrownBy(() -> validationConfigurations.validateDate(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The Date field cannot be empty.");
	}

	@Test
	public void testDateTimeMethodWithPreviousTwoDaysDate() {
		LocalDate dateTime = LocalDate.now();
		LocalDate twoDaysBeforeToday = dateTime.minusDays(2);
		assertThatThrownBy(() -> validationConfigurations.validateDate(twoDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not before today's date.");
	}

	@Test
	public void testDateTimeMethodWithPreviousOneDaysDate() {
		LocalDate dateTime = LocalDate.now();
		LocalDate oneDaysBeforeToday = dateTime.minusDays(1);
		assertThatThrownBy(() -> validationConfigurations.validateDate(oneDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not before today's date.");
	}

	@Test
	public void testDateTimeMethodWithCurrentDate() {
		LocalDate dateTime = LocalDate.now();
		assertThat(dateTime).isEqualTo(validationConfigurations.validateDate(dateTime));
	}

	@Test
	public void testDateTimeMethodWithAfterSevenMonthDate() {
		LocalDate dateTime = LocalDate.now();
		LocalDate sevenDaysBeforeToday = dateTime.plusMonths(7);
		assertThatThrownBy(() -> validationConfigurations.validateDate(sevenDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not 6 months after today's date.");
	}

	@Test
	public void testDateTimeMethodWithDatePlusSixMonths() {
		LocalDate dateTime = LocalDate.now().plusMonths(6);
		assertThat(dateTime).isEqualTo(validationConfigurations.validateDate(dateTime));
	}

}
