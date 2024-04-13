package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

import org.junit.Test;

public class ValidationConfigurationsTest {

	@Test
	public void testNameMethodWithNullString() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> validationConfigurations.validateName(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty");
	}

	@Test
	public void testNameMethodWithEmptyString() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> validationConfigurations.validateName("")).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty");
	}

}
