package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValidationConfigurationsTest {

	@Test
	public void testNameMethodWithNullString() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> validationConfigurations.validateName(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty.");
	}

	@Test
	public void testNameMethodWithEmptyString() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> validationConfigurations.validateName("")).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty.");
	}

	@Test
	public void testNameMethodWithShortStringLessThanTwoCharachters() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> {
			String name = "a";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name must be at least 3 characters long. Please provide a valid name.");
	}

	@Test
	public void testNameMethodWithShortStringEqualsToTwoCharachters() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> {
			String name = "ab";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name must be at least 3 characters long. Please provide a valid name.");
	}

	@Test
	public void testNameMethodWithLargeStringGreaterThanTwentyCharachters() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> {
			String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot exceed 20 characters. Please provide a shorter name.");
	}

	@Test
	public void testNameMethodWithSpecialCharacters() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> {
			String name = "testName@";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The name cannot contain special characters. Please remove any special characters from the name.");
	}

	@Test
	public void testNameMethodWithNumbers() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> {
			String name = "testName123";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot contain numbers. Please remove any number from the name.");
	}

	@Test
	public void testNameMethodWithTabs() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		assertThatThrownBy(() -> {
			String name = "test\tName";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot contain tabs. Please remove any tabs from the name.");
	}

	@Test
	public void testNameMethodWithOneLeadingWhiteSpace() {
		ValidationConfigurations validationConfigurations = new ValidationConfigurations();
		String name = " testName";
//		assertThat(validationConfigurations.validateName(name)).equals("testName");
		assertEquals("testName", validationConfigurations.validateName(name));
	}

}
