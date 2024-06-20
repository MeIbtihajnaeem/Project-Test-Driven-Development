/*
 * Worker: Represents an entity of a worker in the system.
 */
package com.mycompany.orderassignmentsystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;

/**
 * Represents a worker in the system.
 */
@Entity
public class Worker {

	/** The worker id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long workerId;

	/** The worker name. */
	@Column(name = "workerName")
	private String workerName;

	/** The worker phone number. */
	@Column(name = "workerPhoneNumber", unique = true)
	private String workerPhoneNumber;

	/** The worker category. */
	@Column(name = "workerCategory")
	@Enumerated(EnumType.STRING)
	private OrderCategory workerCategory;

	/** The orders. */
	@OneToMany
	@JoinTable(name = "Worker_Order", joinColumns = { @JoinColumn(name = "ord_id") }, inverseJoinColumns = {
			@JoinColumn(name = "work_id") })
	private List<CustomerOrder> orders = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public Worker() {
	}

	/**
	 * Parameterised constructor.
	 *
	 * @param workerId          the worker id
	 * @param workerName        the worker name
	 * @param workerPhoneNumber the worker phone number
	 * @param workerCategory    the worker category
	 */
	public Worker(Long workerId, String workerName, String workerPhoneNumber, OrderCategory workerCategory) {
		super();
		this.workerId = workerId;
		this.workerName = workerName;
		this.workerPhoneNumber = workerPhoneNumber;
		this.workerCategory = workerCategory;
	}

	public Worker(String workerName, String workerPhoneNumber, OrderCategory workerCategory) {
		super();

		this.workerName = workerName;
		this.workerPhoneNumber = workerPhoneNumber;
		this.workerCategory = workerCategory;
	}

	/**
	 * Gets the worker id.
	 *
	 * @return the worker id
	 */
	public Long getWorkerId() {
		return workerId;
	}

	/**
	 * Sets the worker id.
	 *
	 * @param workerId the new worker id
	 */
	public void setWorkerId(Long workerId) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Worker other = (Worker) obj;
		return Objects.equals(workerId, other.workerId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(workerId);
	}

	@Override
	public String toString() {
		return this.workerId + ") " + this.workerName + " -- " + this.workerCategory + " -- " + this.workerPhoneNumber;
	}

	/**
	 * Note: If the hashCode or equals method is removed, WorkerSwingViewTest will
	 * fail. This is because these methods are used to compare objects during
	 * operations like adding, updating, and deleting.
	 */
}
