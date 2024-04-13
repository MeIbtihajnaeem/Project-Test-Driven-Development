package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

public class ValidationConfigurationsForValidateAddressMethodTest {
	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
	}

	@Test
	public void testAddressMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateAddress(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The address field cannot be empty.");
	}

	@Test
	public void testAddressMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateAddress("")).isInstanceOf(NullPointerException.class)
				.hasMessage("The address field cannot be empty.");
	}

}
