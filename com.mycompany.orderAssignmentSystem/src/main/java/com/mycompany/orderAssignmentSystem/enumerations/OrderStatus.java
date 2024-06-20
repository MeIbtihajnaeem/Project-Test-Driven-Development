/*
 * OrderStatus: Enumeration representing different statuses of customer orders.
 */
package com.mycompany.orderassignmentsystem.enumerations;

/**
 * Enumeration representing different statuses of customer orders.
 */
public enum OrderStatus {

	/**
	 * Order status: Pending - Waiting to be processed.
	 */
	PENDING,

	/**
	 * Order status: Completed - Successfully processed.
	 */
	COMPLETED,

	/**
	 * Order status: Cancelled - Not processed due to cancellation.
	 */
	CANCELLED
}
