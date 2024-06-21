package com.mycompany.orderassignmentsystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;

public class ValidationConfigurationsForValidateStringDateMethod {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ExtendedValidationConfigurations();
	}

	@Test
	public void testValidateStringDateMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(null))
				.isInstanceOf(NullPointerException.class).hasMessage("Date cannot be null.");
	}

	@Test
	public void testValidateStringDateMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateStringDate(""))
				.isInstanceOf(NullPointerException.class).hasMessage("Date cannot be empty.");
	}

	@Test
	public void testValidateStringDateMethodWithStringLessThenNineLength() {
		assertThatThrownBy(() -> validationConfigurations.validateStringDate("12/12/"))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Date must not be less then 10 characters.");
	}

	@Test
	public void testValidateStringDateMethodWithStringEqualToNineLength() {
		assertThatThrownBy(() -> validationConfigurations.validateStringDate("12/12/202"))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Date must not be less then 10 characters.");
	}

	@Test
	public void testValidateStringDateMethodWithStringGreaterThenNineLength() {
		assertThatThrownBy(() -> validationConfigurations.validateStringDate("12/12/20224"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Date must not be greater then 10 characters.");
	}

	@Test
	public void testValidateStringDateMethodWithInValidStringDate() {
		String pattern = "dd-MM-yyyy";

		assertThatThrownBy(() -> validationConfigurations.validateStringDate("12/12-2024"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Please ensure that the date follows the format" + pattern);
	}

	@Test
	public void testValidateStringDateMethodWithValidStringDate() {
		String validStringDate = "30-12-2024";
		assertEquals(validStringDate, validationConfigurations.validateStringDate(validStringDate));
	}

}
