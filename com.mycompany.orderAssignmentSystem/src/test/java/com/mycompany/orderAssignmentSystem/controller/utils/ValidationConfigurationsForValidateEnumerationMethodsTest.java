package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

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

}
