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

	@Test
	public void testAddressMethodWithShortStringLessThanTenCharachters() {
		assertThatThrownBy(() -> {
			String address = "42 Will";
			validationConfigurations.validateAddress(address);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address must be at least 10 characters long. Please provide a valid Address.");
	}

	@Test
	public void testNameMethodWithShortStringEqualsToTenCharachters() {
		assertThatThrownBy(() -> {
			String address = "42 Willowa";
			validationConfigurations.validateAddress(address);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address must be at least 10 characters long. Please provide a valid Address.");
	}

	@Test
	public void testNameMethodWithLargeStringGreaterThanFourtyCharachters() {
		assertThatThrownBy(() -> {
			String address = "123 Main Street Near Bakary, Apt 101, Springfield, USA 12345";
			validationConfigurations.validateAddress(address);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address cannot exceed 50 characters. Please provide a shorter Address.");
	}

}
