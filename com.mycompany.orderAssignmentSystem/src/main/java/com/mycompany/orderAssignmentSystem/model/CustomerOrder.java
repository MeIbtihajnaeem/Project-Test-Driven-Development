package com.mycompany.orderAssignmentSystem.model;

import java.time.LocalDateTime;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;

public class CustomerOrder {
	private Long orderId;
	private String customerName;
	private String customerAddress;
	private String customerPhoneNumber;
	private LocalDateTime appointmentDate;
	private String orderDescription;
	private OrderCategory orderCategory;
	private OrderStatus orderStatus;
	private Worker worker;

	public CustomerOrder() {

	}

//	public CustomerOrder(String customerName, String customerAddress, String customerPhoneNumber,
//			LocalDateTime appointmentDate, String orderDescription, OrderCategory orderCategory,
//			OrderStatus orderStatus, Worker worker) {
//		this.customerName = customerName;
//		this.customerAddress = customerAddress;
//		this.customerPhoneNumber = customerPhoneNumber;
//		this.appointmentDate = appointmentDate;
//		this.orderDescription = orderDescription;
//		this.orderCategory = orderCategory;
//		this.orderStatus = orderStatus;
//		this.worker = worker;
//	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
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

	public LocalDateTime getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDateTime appointmentDate) {
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

	public Worker getWorker() {
		return worker;
	}

	public void setWorkers(Worker worker) {
		this.worker = worker;
	}

//	public String displayOrder() {
//		return String.format(
//				"Order ID: %d\nCustomer Name: %s\nCustomer Address: %s\nCustomer Phone Number: %s\nAppointment Date: %s\nOrder Description: %s\nOrder Category: %s\nOrder Status: %s",
//				orderId, customerName, customerAddress, customerPhoneNumber, appointmentDate, orderDescription,
//				orderCategory, orderStatus);
//	}

}
