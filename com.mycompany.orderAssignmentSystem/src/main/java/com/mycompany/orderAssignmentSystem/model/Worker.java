/*
 * Worker: Represents an entity of a worker in the system.
 */
package com.mycompany.orderAssignmentSystem.model;

import java.util.List;
import java.util.Objects;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;

/**
 * Represents a worker in the system.
 */
public class Worker {

	/** The worker id. */
	private String workerId;

	/** The worker name. */
	private String workerName;

	/** The worker phone number. */
	private String workerPhoneNumber;

	/** The worker category. */
	private OrderCategory workerCategory;

	/** The orders. */
	private List<CustomerOrder> orders;

	/**
	 * Default constructor.
	 */
	public Worker() {
	};

	/**
	 * Parameterised constructor.
	 *
	 * @param workerId          the worker id
	 * @param workerName        the worker name
	 * @param workerPhoneNumber the worker phone number
	 * @param workerCategory    the worker category
	 */
	public Worker(String workerId, String workerName, String workerPhoneNumber, OrderCategory workerCategory) {
		super();
		this.workerId = workerId;
		this.workerName = workerName;
		this.workerPhoneNumber = workerPhoneNumber;
		this.workerCategory = workerCategory;
	}

	/**
	 * Gets the worker id.
	 *
	 * @return the worker id
	 */
	public String getWorkerId() {
		return workerId;
	}

	/**
	 * Sets the worker id.
	 *
	 * @param workerId the new worker id
	 */
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	/**
	 * Gets the worker name.
	 *
	 * @return the worker name
	 */
	public String getWorkerName() {
		return workerName;
	}

	/**
	 * Sets the worker name.
	 *
	 * @param workerName the new worker name
	 */
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	/**
	 * Gets the worker phone number.
	 *
	 * @return the worker phone number
	 */
	public String getWorkerPhoneNumber() {
		return workerPhoneNumber;
	}

	/**
	 * Sets the worker phone number.
	 *
	 * @param workerPhoneNumber the new worker phone number
	 */
	public void setWorkerPhoneNumber(String workerPhoneNumber) {
		this.workerPhoneNumber = workerPhoneNumber;
	}

	/**
	 * Gets the worker category.
	 *
	 * @return the worker category
	 */
	public OrderCategory getWorkerCategory() {
		return workerCategory;
	}

	/**
	 * Sets the worker category.
	 *
	 * @param workerCategory the new worker category
	 */
	public void setWorkerCategory(OrderCategory workerCategory) {
		this.workerCategory = workerCategory;
	}

	/**
	 * Gets the orders associated with the worker.
	 *
	 * @return the orders
	 */
	public List<CustomerOrder> getOrders() {
		return orders;
	}

	/**
	 * Sets the orders associated with the worker.
	 *
	 * @param orders the new orders
	 */
	public void setOrders(List<CustomerOrder> orders) {
		this.orders = orders;
	}

	/**
	 * Generates the hash code for the Worker object.
	 *
	 * @return the hash code value
	 */
	@Override
	public int hashCode() {
		return Objects.hash(orders, workerCategory, workerId, workerName, workerPhoneNumber);
	}

	/**
	 * Compares this Worker object to another object for equality.
	 *
	 * @param obj the object to compare
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Worker other = (Worker) obj;
		return Objects.equals(orders, other.orders) && workerCategory == other.workerCategory
				&& Objects.equals(workerId, other.workerId) && Objects.equals(workerName, other.workerName)
				&& Objects.equals(workerPhoneNumber, other.workerPhoneNumber);
	}

	@Override
	public String toString() {
		return this.workerId + ") " + this.workerName + " -- " + this.workerCategory;
	}

	/**
	 * Note: If the hashCode or equals method is removed, WorkerSwingViewTest will
	 * fail. This is because these methods are used to compare objects during
	 * operations like adding, updating, and deleting.
	 */
}
