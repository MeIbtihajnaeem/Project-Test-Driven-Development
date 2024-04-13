package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ValidationConfigurationsForValidatePhoneNumberMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
	}

	@Test
	public void testPhoneNumberMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The phone number field cannot be empty.");
	}

	@Test
	public void testPhoneNumberWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validatePhoneNumber(""))
				.isInstanceOf(NullPointerException.class).hasMessage("The phone number field cannot be empty.");
	}

	@Test
	public void testPhoneNumberMethodWithShortStringLessThanTenCharachters() {
		assertThatThrownBy(() -> {
			String phoneNumber = "aaaaaaaaa";
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The phone number must be 10 characters long. Please provide a valid phone number.");
	}

	@Test
	public void testPhoneNumberMethodWithLongStringGreaterThanTenCharachters() {
		assertThatThrownBy(() -> {
			String phoneNumber = "aaaaaaaaaaa";
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The phone number must be 10 characters long. Please provide a valid phone number.");
	}

	@Test
	public void testPhoneNumberMethodWithLongStringEqualsTenCharachters() {
		String phoneNumber = "3401372678";
		assertEquals(phoneNumber, validationConfigurations.validatePhoneNumber(phoneNumber));
	}

	@Test
	public void testPhoneNumberMethodWithSpecialCharachters() {
		String phoneNumber = "3401372@78";
		assertThatThrownBy(() -> {
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	@Test
	public void testPhoneNumberMethodWithAlphabetCharachters() {
		String phoneNumber = "3401372a78";
		assertThatThrownBy(() -> {
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	@Test
	public void testPhoneNumberMethodWithMiddleWhiteSpaceCharachters() {
		String phoneNumber = "3401372 78";
		assertThatThrownBy(() -> {
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	@Test
	public void testPhoneNumberMethodWithLeadingWhiteSpaceCharachters() {
		String phoneNumber = " 401372078";
		assertThatThrownBy(() -> {
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	@Test
	public void testPhoneNumberMethodWithEndingWhiteSpaceCharachters() {
		String phoneNumber = "340137207 ";
		assertThatThrownBy(() -> {
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

	@Test
	public void testPhoneNumberMethodWithTabsCharachters() {
		String phoneNumber = "\t401372073";
		assertThatThrownBy(() -> {
			validationConfigurations.validatePhoneNumber(phoneNumber);
		}).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.");
	}

}
