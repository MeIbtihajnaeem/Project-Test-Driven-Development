package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

public class ValidationConfigurationsForValidatePhoneNumberMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
	}

	@Test
	public void testNameMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The phone number field cannot be empty.");
	}

}
