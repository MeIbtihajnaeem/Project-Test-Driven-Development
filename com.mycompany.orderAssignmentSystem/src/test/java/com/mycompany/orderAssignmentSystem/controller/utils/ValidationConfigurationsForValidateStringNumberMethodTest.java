package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ValidationConfigurationsForValidateStringNumberMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
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
	public void testStringNumberMethodWithLargeStringGreaterThanTwentyCharachters() {
		assertThatThrownBy(() -> {
			String number = "000000000000000000000";
			validationConfigurations.validateStringNumber(number);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The number cannot exceed 20 characters. Please provide a shorter number.");
	}

	@Test
	public void testStringNumberMethodWithLargeStringEqualsToTwentyCharachters() {
		String number = "00000000000000000000";
		assertEquals(true, validationConfigurations.validateStringNumber(number));
	}

	@Test
	public void testStringNumberMethodWithAlphabetCharachters() {
		String number = "3401372a78";
		assertEquals(false, validationConfigurations.validateStringNumber(number));

	}

}
