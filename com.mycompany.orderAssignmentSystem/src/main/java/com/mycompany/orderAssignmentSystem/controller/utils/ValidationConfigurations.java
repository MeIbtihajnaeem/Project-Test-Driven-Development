package com.mycompany.orderAssignmentSystem.controller.utils;

public class ValidationConfigurations {

	public String validateName(String name) {
		if (name == null || name == "") {
			throw new NullPointerException("The name field cannot be empty");
		}
		return null;
	}

}
