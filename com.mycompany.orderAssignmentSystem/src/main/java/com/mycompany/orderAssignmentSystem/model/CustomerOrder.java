/*
 * CustomerOrder: Represents an order placed by a customer in the system.
 * This class holds information about an order, including customer details, appointment date, order description, category, status, and the assigned worker.
 */

package com.mycompany.orderAssignmentSystem.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;

/**
 * Represents an order placed by a customer in the system.
 */
@Entity
public class CustomerOrder {

	/** The order id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	/** The customer name. */
	@Column(name = "customer_name")
	private String customerName;

	/** The customer address. */
	@Column(name = "customer_address")
	private String customerAddress;

	/** The customer phone number. */
	@Column(name = "customer_phone")
	private String customerPhoneNumber;

	/** The appointment date. */
	@Column(name = "appointment_date")
	private String appointmentDate;

	/** The order description. */
	@Column(name = "order_description")
	private String orderDescription;

	/** The order category. */
	@Column(name = "order_category")
	@Enumerated(EnumType.STRING)
	private OrderCategory orderCategory;

	/** The order status. */
	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	/** The worker assigned to the order. */
	@OneToOne
	private Worker worker;

	/**
	 * Default constructor.
	 */
	public CustomerOrder() {

	}

	/**
	 * Parameterised constructor.
	 *
	 * @param orderId             the order id
	 * @param customerName        the customer name
	 * @param customerAddress     the customer address
	 * @param customerPhoneNumber the customer phone number
	 * @param appointmentDate     the appointment date
	 * @param orderDescription    the order description
	 * @param orderCategory       the order category
	 * @param orderStatus         the order status
	 * @param worker              the worker assigned to the order
	 */
	public CustomerOrder(Long orderId, String customerName, String customerAddress, String customerPhoneNumber,
			String appointmentDate, String orderDescription, OrderCategory orderCategory, OrderStatus orderStatus,
			Worker worker) {
		super();
		this.orderId = orderId;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerPhoneNumber = customerPhoneNumber;
		this.appointmentDate = appointmentDate;
		this.orderDescription = orderDescription;
		this.orderCategory = orderCategory;
		this.orderStatus = orderStatus;
		this.worker = worker;
	}

	public CustomerOrder(String customerName, String customerAddress, String customerPhoneNumber,
			String appointmentDate, String orderDescription, OrderCategory orderCategory, OrderStatus orderStatus,
			Worker worker) {
		super();
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerPhoneNumber = customerPhoneNumber;
		this.appointmentDate = appointmentDate;
		this.orderDescription = orderDescription;
		this.orderCategory = orderCategory;
		this.orderStatus = orderStatus;
		this.worker = worker;
	}

	/**
	 * Gets the order id.
	 *
	 * @return the order id
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * Sets the order id.
	 *
	 * @param orderId the new order id
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * Gets the customer name.
	 *
	 * @return the customer name
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * Sets the customer name.
	 *
	 * @param customerName the new customer name
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Gets the customer address.
	 *
	 * @return the customer address
	 */
	public String getCustomerAddress() {
		return customerAddress;
	}

	/**
	 * Sets the customer address.
	 *
	 * @param customerAddress the new customer address
	 */
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	/**
	 * Gets the customer phone number.
	 *
	 * @return the customer phone number
	 */
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	/**
	 * Sets the customer phone number.
	 *
	 * @param customerPhoneNumber the new customer phone number
	 */
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	/**
	 * Gets the appointment date.
	 *
	 * @return the appointment date
	 */
	public String getAppointmentDate() {
		return appointmentDate;
	}

	/**
	 * Sets the appointment date.
	 *
	 * @param appointmentDate the new appointment date
	 */
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	/**
	 * Gets the order description.
	 *
	 * @return the order description
	 */
	public String getOrderDescription() {
		return orderDescription;
	}

	/**
	 * Sets the order description.
	 *
	 * @param orderDescription the new order description
	 */
	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	/**
	 * Gets the order category.
	 *
	 * @return the order category
	 */
	public OrderCategory getOrderCategory() {
		return orderCategory;
	}

	/**
	 * Sets the order category.
	 *
	 * @param orderCategory the new order category
	 */
	public void setOrderCategory(OrderCategory orderCategory) {
		this.orderCategory = orderCategory;
	}

	/**
	 * Gets the order status.
	 *
	 * @return the order status
	 */
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Sets the order status.
	 *
	 * @param orderStatus the new order status
	 */
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Gets the worker assigned to the order.
	 *
	 * @return the worker
	 */
	public Worker getWorker() {
		return worker;
	}

	/**
	 * Sets the worker assigned to the order.
	 *
	 * @param worker the new worker
	 */
	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CustomerOrder other = (CustomerOrder) obj;
		return Objects.equals(orderId, other.orderId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId);
	}

	@Override
	public String toString() {
		if (customerName == null || customerAddress == null || customerPhoneNumber == null || appointmentDate == null
				|| orderDescription == null || orderCategory == null || orderStatus == null || worker == null) {
			return null;
		}
		return "Order ID: " + orderId + ", Name: " + customerName + ", Address: " + customerAddress + ", " + "Phone: "
				+ customerPhoneNumber + ", Date: " + appointmentDate + ", Description: " + orderDescription
				+ ", Category: " + orderCategory + ", Status: " + orderStatus + ", " + "Worker: " + worker;
	}

}
