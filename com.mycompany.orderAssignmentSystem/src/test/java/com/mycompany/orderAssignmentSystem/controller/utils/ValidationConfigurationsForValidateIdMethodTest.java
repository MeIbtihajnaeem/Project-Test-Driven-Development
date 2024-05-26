//package com.mycompany.orderAssignmentSystem.controller.utils;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
//
//public class ValidationConfigurationsForValidateIdMethodTest {
//
//	private ValidationConfigurations validationConfigurations;
//
//	@Before
//	public void setup() {
//		validationConfigurations = new ExtendedValidationConfigurations();
//	}
//
//	@Test
//	public void testIdMethodWithNullIntegar() {
//		assertThatThrownBy(() -> validationConfigurations.validateId(null)).isInstanceOf(NullPointerException.class)
//				.hasMessage("The id field cannot be empty.");
//	}
//
//	@Test
//	public void testIdMethodWithZeroIntegar() {
//		assertThatThrownBy(() -> validationConfigurations.validateId(0l)).isInstanceOf(IllegalArgumentException.class)
//				.hasMessage("The id field cannot be less than 1. Please provide a valid id.");
//	}
//
//	@Test
//	public void testIdMethodWithNegativeIntegar() {
//		assertThatThrownBy(() -> validationConfigurations.validateId(-1l)).isInstanceOf(IllegalArgumentException.class)
//				.hasMessage("The id field cannot be less than 1. Please provide a valid id.");
//	}
//
//	@Test
//	public void testIdMethodWithPositiveIntegarAndGreaterThanZero() {
//		assertThat(validationConfigurations.validateId(1l)).isPositive();
//	}
//
//	@Test
//	public void testIdMethodWithIdEqualsLong() {
//		long number = 1l;
//		assertThat(validationConfigurations.validateId(number)).isEqualTo(number);
//
//	}
//
//}
