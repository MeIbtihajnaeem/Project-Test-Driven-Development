package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;

public class ValidationConfigurationsForValidateNameMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ExtendedValidationConfigurations();
	}

	@Test
	public void testNameMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateName(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty.");
	}

	@Test
	public void testNameMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateName("")).isInstanceOf(NullPointerException.class)
				.hasMessage("The name field cannot be empty.");
	}

	@Test
	public void testNameMethodWithShortStringLessThanTwoCharachters() {
		assertThatThrownBy(() -> {
			String name = "a";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name must be at least 3 characters long. Please provide a valid name.");
	}

	@Test
	public void testNameMethodWithShortStringEqualsToTwoCharachters() {
		assertThatThrownBy(() -> {
			String name = "ab";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name must be at least 3 characters long. Please provide a valid name.");
	}

	@Test
	public void testNameMethodWithLargeStringGreaterThanTwentyCharachters() {
		assertThatThrownBy(() -> {
			String name = "Muhammad Ibtihaj Naeem";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot exceed 20 characters. Please provide a shorter name.");
	}

	@Test
	public void testNameMethodWithLargeStringEqualsToTwentyCharachters() {
		String name = "Muhammad Ibtihaj Nae";
		assertEquals(name, validationConfigurations.validateName(name));

	}

	@Test
	public void testNameMethodWithSpecialCharacters() {
		assertThatThrownBy(() -> {
			String name = "testName@";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The name cannot contain special characters. Please remove any special characters from the name.");
	}

	@Test
	public void testNameMethodWithNumbers() {
		assertThatThrownBy(() -> {
			String name = "testName123";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot contain numbers. Please remove any number from the name.");
	}

	@Test
	public void testNameMethodWithTabs() {
		assertThatThrownBy(() -> {
			String name = "test\tName";
			validationConfigurations.validateName(name);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The name cannot contain tabs. Please remove any tabs from the name.");
	}

	@Test
	public void testNameMethodWithOneLeadingWhiteSpace() {
		String name = " testName";
		assertEquals("testName", validationConfigurations.validateName(name));
	}

	@Test
	public void testNameMethodWithTwoLeadingWhiteSpace() {
		String name = "  testName";
		assertEquals("testName", validationConfigurations.validateName(name));
	}

	@Test
	public void testNameMethodWithOneMiddleWhiteSpace() {
		String name = "test Name";
		assertEquals("test Name", validationConfigurations.validateName(name));
	}

	@Test
	public void testNameMethodWithOneEndingWhiteSpace() {
		String name = "testName ";
		assertEquals("testName", validationConfigurations.validateName(name));
	}

}
