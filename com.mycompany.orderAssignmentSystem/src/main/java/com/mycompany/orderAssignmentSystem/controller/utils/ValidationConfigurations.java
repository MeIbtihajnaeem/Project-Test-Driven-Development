package com.mycompany.orderAssignmentSystem.controller.utils;

public class ValidationConfigurations {

	public String validateName(String name) {
		if (name == null || name == "") {
			throw new NullPointerException("The name field cannot be empty");
		}
		if (name.length() <= 2) {
			throw new IllegalArgumentException(
					"The name must be at least 3 characters long. Please provide a valid name");
		}
		if (name.length() > 20) {
			throw new IllegalArgumentException("The name cannot exceed 20 characters. Please provide a shorter name.");
		}
		return null;
	}

}
