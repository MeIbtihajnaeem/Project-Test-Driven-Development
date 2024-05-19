package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;

public class ValidationConfigurationsForValidateAddressMethodTest {
	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ExtendedValidationConfigurations();
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
	public void testAddressMethodWithShortStringEqualsToTenCharachters() {
		assertThatThrownBy(() -> {
			String address = "42 Willowa";
			validationConfigurations.validateAddress(address);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address must be at least 10 characters long. Please provide a valid Address.");
	}

	@Test
	public void testAddressMethodWithLargeStringGreaterThanFiftyCharachters() {
		assertThatThrownBy(() -> {
			String address = "123 Main Street Near Bakary, Apt 101, Springfield, USA 12345";
			validationConfigurations.validateAddress(address);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The Address cannot exceed 50 characters. Please provide a shorter Address.");
	}

	@Test
	public void testAddressMethodWithLargeStringEqualsToFiftyCharachters() {
		String address = "1234 Main Street , Apt 101, Springfield, USA 12345";

		assertEquals(address, validationConfigurations.validateAddress(address));
	}

	@Test
	public void testAddressMethodWithTabs() {
		assertThatThrownBy(() -> {
			String address = "1234 Main Street\t Apt 101, Springfield, USA 12345";
			validationConfigurations.validateAddress(address);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The address cannot contain tabs. Please remove any tabs from the address.");
	}

	@Test
	public void testAddressMethodWithOneLeadingWhiteSpace() {
		String address = " 123 Main Street , Apt 101, Springfield, USA 12345";
		String expectedAdddress = "123 Main Street , Apt 101, Springfield, USA 12345";

		assertEquals(expectedAdddress, validationConfigurations.validateAddress(address));
	}

	@Test
	public void testAddressMethodWithTwoLeadingWhiteSpace() {

		String address = "  123 Main Street, Apt 101, Springfield, USA 12345";
		String expectedAdddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		assertEquals(expectedAdddress, validationConfigurations.validateAddress(address));
	}

	@Test
	public void testAddressMethodWithOneMiddleWhiteSpace() {
		String address = "1234 Main Street , Apt 101, Springfield, USA 12345";
		assertEquals(address, validationConfigurations.validateAddress(address));
	}

	@Test
	public void testAddressMethodWithOneEndingWhiteSpace() {

		String address = "123 Main Street, Apt 101, Springfield, USA 12345 ";
		String expectedAdddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		assertEquals(expectedAdddress, validationConfigurations.validateAddress(address));
	}
}
