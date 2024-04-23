package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class ValidationConfigurationsForValidateDateTimeMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
	}

	@Test
	public void testDateTimeMethodWithNullDateTime() {
		assertThatThrownBy(() -> validationConfigurations.validateDate(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The Date field cannot be empty.");
	}

	@Test
	public void testDateTimeMethodWithPreviousTwoDaysDate() {
		LocalDateTime dateTime = LocalDateTime.now();
		LocalDateTime twoDaysBeforeToday = dateTime.minusDays(2);
		assertThatThrownBy(() -> validationConfigurations.validateDate(twoDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not before today's date.");
	}

	@Test
	public void testDateTimeMethodWithPreviousOneDaysDate() {
		LocalDateTime dateTime = LocalDateTime.now();
		LocalDateTime oneDaysBeforeToday = dateTime.minusDays(1);
		assertThatThrownBy(() -> validationConfigurations.validateDate(oneDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not before today's date.");
	}

	@Test
	public void testDateTimeMethodWithCurrentDate() {
		LocalDateTime dateTime = LocalDateTime.now();
		assertThat(dateTime).isEqualTo(validationConfigurations.validateDate(dateTime));
	}

	@Test
	public void testDateTimeMethodWithAfterSevenMonthDate() {
		LocalDateTime dateTime = LocalDateTime.now();
		LocalDateTime sevenDaysBeforeToday = dateTime.plusMonths(7);
//		System.out.println(sevenDaysBeforeToday);
		assertThatThrownBy(() -> validationConfigurations.validateDate(sevenDaysBeforeToday))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please provide a valid date that is not 6 months after today's date.");
	}

	@Test
	public void testDateTimeMethodWithDatePlusSixMonths() {
		LocalDateTime dateTime = LocalDateTime.now().plusMonths(6);
		assertThat(dateTime).isEqualTo(validationConfigurations.validateDate(dateTime));
	}

}
