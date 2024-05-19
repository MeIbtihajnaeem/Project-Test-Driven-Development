/*
 * OrderSearchOptions: Enumeration representing different search options for customer orders.
 */
package com.mycompany.orderAssignmentSystem.enumerations;

/**
 * Enumeration representing different search options for customer orders.
 */
public enum OrderSearchOptions {

	/**
	 * Search by None.
	 */
	None,
	/**
	 * Search by order ID.
	 */
	ORDER_ID,

	/**
	 * Search by customer name.
	 */
	CUSTOMER_NAME,

	/**
	 * Search by customer phone number.
	 */
	CUSTOMER_PHONE,

	/**
	 * Search by date.
	 */
	DATE,

	/**
	 * Search by order category.
	 */
	CATEGORY,

	/**
	 * Search by order status.
	 */
	STATUS,

	/**
	 * Search by worker ID.
	 */
	WORKER_ID;
}
