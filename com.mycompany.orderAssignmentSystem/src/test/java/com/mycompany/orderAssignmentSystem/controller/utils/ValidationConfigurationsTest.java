package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

	@Test
	public void testNameMethodWithShortStringLessThanTwoCharachters () {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> {
			String name = "a";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name must be at least 3 characters long. Please provide a valid name");
	}

}
