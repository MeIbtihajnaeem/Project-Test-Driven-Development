/*
 * OrderView: Interface for displaying and interacting with customer orders in the system.
 */
package com.mycompany.orderassignmentsystem.view;

import java.util.List;

import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;

/**
 * Interface for displaying and interacting with customer orders in the system.
 */
public interface OrderView {

	/**
	 * Displays all customer orders.
	 *
	 * @param order the list of customer orders to be displayed
	 */
	void showAllOrder(List<CustomerOrder> order);

	/**
	 * Displays all workers.
	 *
	 * @param worker the worker
	 */

	void showAllWorkers(List<Worker> worker);

	/**
	 * Notifies when a new customer order is added.
	 *
	 * @param order the newly added customer order
	 */
	void orderAdded(CustomerOrder order);

	/**
	 * Notifies when a customer order is modified.
	 *
	 * @param order the modified customer order
	 */
	void orderModified(CustomerOrder order);

	/**
	 * Displays a fetched customer order.
	 *
	 * @param order the customer order to be displayed
	 */
	void showFetchedOrder(CustomerOrder order);

	/**
	 * Shows the search result for customer orders.
	 *
	 * @param order the list of customer orders matching the search criteria
	 */
	void showSearchResultForOrder(List<CustomerOrder> order);

	/**
	 * Notifies when a customer order is removed.
	 *
	 * @param order the removed customer order
	 */
	void orderRemoved(CustomerOrder order);

	/**
	 * Displays an error message related to a specific customer order.
	 *
	 * @param message the error message to be displayed
	 * @param order   the customer order associated with the error
	 */
	void showError(String message, CustomerOrder order);

	/**
	 * Displays an error message when a customer order is not found.
	 *
	 * @param message the error message to be displayed
	 * @param order   the customer order associated with the error
	 */
	void showErrorNotFound(String message, CustomerOrder order);

	/**
	 * Displays an error message related to a search operation.
	 *
	 * @param message    the error message to be displayed
	 * @param searchText the search text associated with the error
	 */
	void showSearchError(String message, String searchText);
}
