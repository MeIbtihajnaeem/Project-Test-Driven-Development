/*
 * OrderRepository: Interface for accessing and managing customer orders in the system.
 */
package com.mycompany.orderAssignmentSystem.repository;

import java.util.List;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;

/**
 * Interface for accessing and managing customer orders in the system.
 */
public interface OrderRepository {

	/**
	 * Retrieves all customer orders in the system.
	 *
	 * @return the list of customer orders
	 */
	public List<CustomerOrder> findAll();

	/**
	 * Saves a new customer order
	 *
	 * @param order the customer order to be saved or updated
	 * @return the saved or updated customer order
	 */
	public CustomerOrder save(CustomerOrder order);

	/**
	 * Deletes a customer order from the system.
	 *
	 * @param order the customer order to be deleted
	 */
	public void delete(CustomerOrder order);

	/**
	 * Finds a customer order by its ID.
	 *
	 * @param orderId the ID of the customer order
	 * @return the found customer order, or null if not found
	 */
	public CustomerOrder findById(String orderId);

	/**
	 * Finds customer orders by customer name.
	 *
	 * @param name the name of the customer
	 * @return the list of customer orders with matching customer names or empty
	 *         list if not found
	 */
	public List<CustomerOrder> findByCustomerName(String name);

	/**
	 * Finds customer orders by customer phone number.
	 *
	 * @param phoneNumber the phone number of the customer
	 * @return the list of customer orders with matching phone numbers or empty list
	 *         if not found
	 */
	public List<CustomerOrder> findByCustomerPhoneNumber(String phoneNumber);

	/**
	 * Finds customer orders by date.
	 *
	 * @param date the date of the orders
	 * @return the list of customer orders placed on the specified date or empty
	 *         list if not found
	 */
	public List<CustomerOrder> findByDate(String date);

	/**
	 * Finds customer orders by order category.
	 *
	 * @param category the category of the orders
	 * @return the list of customer orders with matching order categories or empty
	 *         list if not found
	 */
	public List<CustomerOrder> findByOrderCategory(OrderCategory category);

	/**
	 * Finds customer orders by order status.
	 *
	 * @param status the status of the orders
	 * @return the list of customer orders with matching order statuses or empty
	 *         list if not found
	 */
	public List<CustomerOrder> findByOrderStatus(OrderStatus status);

}
