package com.mycompany.orderAssignmentSystem.model;

import java.util.List;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.toedter.calendar.JDateChooser;

public class CustomerOrder {
	private long orderId;
	private String customerName;
	private String customerAddress;
	private String customerPhoneNumber;
	private JDateChooser appointmentDate;
	private String orderDescription;
	private OrderCategory orderCategory;
	private OrderStatus orderStatus;
	private List<Worker> workers;

	public CustomerOrder(String customerName, String customerAddress, String customerPhoneNumber,
			JDateChooser appointmentDate, String orderDescription, OrderCategory orderCategory, OrderStatus orderStatus,
			List<Worker> workers) {
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerPhoneNumber = customerPhoneNumber;
		this.appointmentDate = appointmentDate;
		this.orderDescription = orderDescription;
		this.orderCategory = orderCategory;
		this.orderStatus = orderStatus;
		this.workers = workers;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public JDateChooser getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(JDateChooser appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public OrderCategory getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(OrderCategory orderCategory) {
		this.orderCategory = orderCategory;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<Worker> getWorkers() {
		return workers;
	}

	public void setWorkers(List<Worker> workers) {
		this.workers = workers;
	}

	public String displayOrder() {
		return String.format(
				"Order ID: %d\nCustomer Name: %s\nCustomer Address: %s\nCustomer Phone Number: %s\nAppointment Date: %s\nOrder Description: %s\nOrder Category: %s\nOrder Status: %s",
				orderId, customerName, customerAddress, customerPhoneNumber, appointmentDate, orderDescription,
				orderCategory, orderStatus);
	}

}
