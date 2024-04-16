package com.mycompany.orderAssignmentSystem.enumerations;

public enum OrderSearchOption {
	ORDER_ID("Order ID"), CUSTOMER_NAME("Customer Name"), CUSTOMER_ADDRESS("Customer Address"),
	CUSTOMER_PHONE("Customer Contact"), DATE("Date"), DESCRIPTION("Description"), CATEGORY("Category"),
	STATUS("Status"), WORKER_ID("Worker ID");

	private final String displayName;

	OrderSearchOption(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
