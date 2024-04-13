package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;

public class ValidationConfigurationsForValidateEnumerationMethodsTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
	}

// These tests are to validate category enumeration method
	@Test
	public void testCategoryEnumMethodWithNullEnum() {
		assertThatThrownBy(() -> validationConfigurations.validateCategory(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The category field cannot be empty.");
	}

	@Test
	public void testCategoryEnumMethodWithEnumValue() {
		OrderCategory category = OrderCategory.PLUMBER;
		assertEquals(category, validationConfigurations.validateCategory(category));
	}

// These tests are to validate status enumeration method
	@Test
	public void testStatusEnumMethodWithNullEnum() {
		assertThatThrownBy(() -> validationConfigurations.validateStatus(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("The status field cannot be empty.");
	}

	@Test
	public void testStatusEnumMethodWithEnumValue() {
		OrderStatus status = OrderStatus.PENDING;
		assertEquals(status, validationConfigurations.validateStatus(status));
	}

	// These tests are to convert and validate string to order category enum
	@Test
	public void testStringToCategoryEnumMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum(null, OrderCategory.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The category field cannot be empty.");
	}

	@Test
	public void testStringToCategoryEnumMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum("", OrderCategory.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The category field cannot be empty.");
	}

	@Test
	public void testStringToCategoryEnumMethodWithTabsInString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum("\t", OrderCategory.class))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The category cannot contain tabs. Please remove any tabs from the category.");
	}

	@Test
	public void testStringToCategoryEnumMethodWithWhiteSpacesInString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum(" Plumber ", OrderCategory.class))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(
						"The category cannot contain whitespaces. Please remove any whitespaces from the category.");
	}

	@Test
	public void testStringToCategoryMethodWithRightStringValue() {
		OrderCategory expectedResult = OrderCategory.PLUMBER;
		assertEquals(expectedResult, validationConfigurations.validateEnum("PLUMBER", OrderCategory.class));
	}

	@Test
	public void testStringToCategoryMethodWithWrongStringValue() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum("Plumbers", OrderCategory.class))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("The specified category was not found. Please provide a valid category.");
	}

	// These tests are to convert and validate string to order status enum
	@Test
	public void testStringToStatusEnumMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum(null, OrderStatus.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The status field cannot be empty.");
	}

	@Test
	public void testStringToStatusEnumMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum("", OrderStatus.class))
				.isInstanceOf(NullPointerException.class).hasMessage("The status field cannot be empty.");
	}

	@Test
	public void testStringToStatusEnumMethodWithTabsInString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum("\t", OrderStatus.class))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The status cannot contain tabs. Please remove any tabs from the status.");
	}

	@Test
	public void testStringToStatusEnumMethodWithWhiteSpacesInString() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum(" PENDING ", OrderStatus.class))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The status cannot contain whitespaces. Please remove any whitespaces from the status.");
	}

	@Test
	public void testStringToStatusMethodWithRightStringValue() {
		OrderStatus expectedResult = OrderStatus.PENDING;
		assertEquals(expectedResult, validationConfigurations.validateEnum("PENDING", OrderStatus.class));
	}

	@Test
	public void testStringToStatusMethodWithWrongStringValue() {
		assertThatThrownBy(() -> validationConfigurations.validateEnum("Complete", OrderStatus.class))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("The specified status was not found. Please provide a valid status.");
	}

}
