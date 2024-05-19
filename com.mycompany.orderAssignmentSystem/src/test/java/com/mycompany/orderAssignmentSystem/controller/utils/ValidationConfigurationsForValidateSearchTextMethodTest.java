package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;

public class ValidationConfigurationsForValidateSearchTextMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ExtendedValidationConfigurations();
	}

	@Test
	public void testSearchTextMethodWithNullString() {
		assertThatThrownBy(() -> validationConfigurations.validateSearchString(null))
				.isInstanceOf(NullPointerException.class).hasMessage("The search Text field cannot be empty.");
	}

	@Test
	public void testSearchTextMethodWithEmptyString() {
		assertThatThrownBy(() -> validationConfigurations.validateSearchString(""))
				.isInstanceOf(NullPointerException.class).hasMessage("The search Text field cannot be empty.");
	}

	@Test
	public void testSearchTextMethodWithLargeStringGreaterThanTwentyCharachters() {
		assertThatThrownBy(() -> {
			String searchText = "Muhammad Ibtihaj Naeem";
			validationConfigurations.validateSearchString(searchText);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The search Text cannot exceed 20 characters. Please provide a shorter search Text.");
	}

	@Test
	public void testSearchTextMethodWithLargeStringEqualsToTwentyCharachters() {
		String searchText = "Muhammad Ibtihaj Nae";
		assertEquals(searchText, validationConfigurations.validateSearchString(searchText));

	}

	@Test
	public void testSearchTextMethodWithTabs() {
		assertThatThrownBy(() -> {
			String searchText = "test\tSearchText";
			validationConfigurations.validateSearchString(searchText);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The search Text cannot contain tabs. Please remove any tabs from the search Text.");
	}

}
