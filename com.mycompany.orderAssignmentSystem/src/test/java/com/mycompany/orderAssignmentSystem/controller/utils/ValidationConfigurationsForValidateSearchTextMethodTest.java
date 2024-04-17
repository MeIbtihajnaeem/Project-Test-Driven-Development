package com.mycompany.orderAssignmentSystem.controller.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ValidationConfigurationsForValidateSearchTextMethodTest {

	private ValidationConfigurations validationConfigurations;

	@Before
	public void setup() {
		validationConfigurations = new ValidationConfigurations();
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

	@Test
	public void testSearchTextMethodWithOneLeadingWhiteSpace() {
		String searchText = " testSearchText";
		assertEquals("testSearchText", validationConfigurations.validateSearchString(searchText));
	}

	@Test
	public void testSearchTextMethodWithTwoLeadingWhiteSpace() {
		String searchText = "  testSearchText";
		assertEquals("testSearchText", validationConfigurations.validateSearchString(searchText));
	}

	@Test
	public void testSearchTextMethodWithOneMiddleWhiteSpace() {
		String searchText = "test SearchText";
		assertEquals("test SearchText", validationConfigurations.validateSearchString(searchText));
	}

	@Test
	public void testSearchTextMethodWithOneEndingWhiteSpace() {
		String searchText = "testSearchText ";
		assertEquals("testSearchText", validationConfigurations.validateSearchString(searchText));
	}

}
