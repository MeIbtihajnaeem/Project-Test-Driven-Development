package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;

public class ValidationConfigurationsForValidateStringNumberMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ExtendedValidationConfigurations();
	}

	@Test
	public void testStringNumberMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The number cannot be empty.");
	}

	@Test
	public void testStringNumberMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateStringNumber(""))
				.isInstanceOf(NullPointerException.class).hasMessage("The number cannot be empty.");
	}

	@Test
	public void testStringNumberMethodWithLargeStringGreaterThanTwentyCharacters() {
		assertThatThrownBy(() -> {
			String number = "000000000000000000000";
			validationConfigurations.validateStringNumber(number);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot exceed 20 characters. Please provide a shorter number.");
	}

	@Test
	public void testStringNumberMethodWithLargeStringEqualsToTwentyCharacters() {
		String number = "00000000000000000000";
		long resultNumber = 0L;
		assertEquals(resultNumber, validationConfigurations.validateStringNumber(number));
	}

	@Test
	public void testStringNumberMethodWithAlphabeticCharacters() {
		assertThatThrownBy(() -> {
			String number = "3401372a78";
			validationConfigurations.validateStringNumber(number);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage("Please enter a valid number.");
	}

	@Test
	public void testStringNumberMethodWithLeadingWhiteSpace() {
		assertThatThrownBy(() -> {
			String number = " 1234567890";
			validationConfigurations.validateStringNumber(number);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot contains whitespace. Please provide a valid number.");
	}

	@Test
	public void testStringNumberMethodWithEndingWhiteSpace() {
		assertThatThrownBy(() -> {
			String number = "1234567890 ";
			validationConfigurations.validateStringNumber(number);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot contains whitespace. Please provide a valid number.");
	}

	@Test
	public void testStringNumberMethodWithMiddleWhiteSpace() {
		assertThatThrownBy(() -> {
			String number = "12345 67890";
			validationConfigurations.validateStringNumber(number);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot contains whitespace. Please provide a valid number.");
	}

	@Test
	public void testStringNumberMethodWithValidNumber() {
		String number = "1234567890";
		long resultNumber = 1234567890L;
		assertEquals(resultNumber, validationConfigurations.validateStringNumber(number));
	}

	@Test
	public void testStringNumberMethodWithNegativeNumber() {
		String number = "-123";
		long resultNumber = -123L;
		assertEquals(resultNumber, validationConfigurations.validateStringNumber(number));
	}

	@Test
	public void testStringNumberMethodWithZero() {
		String number = "0";
		long resultNumber = 0L;
		assertEquals(resultNumber, validationConfigurations.validateStringNumber(number));
	}
}