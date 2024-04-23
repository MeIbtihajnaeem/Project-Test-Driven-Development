package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ValidationConfigurationsForValidateDescriptionMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
	}

	@Test
	public void testDescriptionMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateDescription(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The description field cannot be empty.");
	}

	@Test
	public void testDescriptionMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateDescription(""))
				.isInstanceOf(NullPointerException.class).hasMessage("The description field cannot be empty.");
	}

	@Test
	public void testDescriptionMethodWithShortStringLessThanTenCharachters() {
		assertThatThrownBy(() -> {
			String description = "repair";
			validationConfigurations.validateDescription(description);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description must be at least 10 characters long. Please provide a valid description.");
	}

	@Test
	public void testDescriptionMethodWithShortStringEqualsToTenCharachters() {
		assertThatThrownBy(() -> {
			String description = "change pip";
			validationConfigurations.validateDescription(description);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description must be at least 10 characters long. Please provide a valid description.");
	}

	@Test
	public void testDescriptionMethodWithLargeStringGreaterThanFiftyCharachters() {
		assertThatThrownBy(() -> {
			String description = "Please ensure the pipes are tightly sealed and all connections are leak-proof. Thank you!";
			validationConfigurations.validateDescription(description);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description cannot exceed 50 characters. Please provide a shorter description.");
	}

	@Test
	public void testDescriptionMethodWithLargeStringEqualsToFiftyCharachters() {
		String description = "Please ensure all connection are leak-proof.Thanks";

		assertEquals(description, validationConfigurations.validateDescription(description));
	}

	@Test
	public void testDescriptionMethodWithTabs() {
		assertThatThrownBy(() -> {
			String description = "Please ensure all connection \t are leak-proof.";
			validationConfigurations.validateDescription(description);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The description cannot contain tabs. Please remove any tabs from the description.");
	}

	@Test
	public void testDescriptionMethodWithOneLeadingWhiteSpace() {
		String description = " Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";

		assertEquals(expectedDescription, validationConfigurations.validateDescription(description));
	}

	@Test
	public void testDescriptionMethodWithTwoLeadingWhiteSpace() {

		String description = "  Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";

		assertEquals(expectedDescription, validationConfigurations.validateDescription(description));
	}

	@Test
	public void testDescriptionMethodWithOneMiddleWhiteSpace() {
		String description = "Please ensure all connection are leak-proof.";
		assertEquals(description, validationConfigurations.validateDescription(description));
	}

	@Test
	public void testDescriptionMethodWithOneEndingWhiteSpace() {

		String description = "Please ensure all connection are leak-proof. ";
		String expectedDescription = "Please ensure all connection are leak-proof.";

		assertEquals(expectedDescription, validationConfigurations.validateDescription(description));
	}

}
