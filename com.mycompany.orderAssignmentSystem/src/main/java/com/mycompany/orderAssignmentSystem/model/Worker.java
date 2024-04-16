package com.mycompany.orderAssignmentSystem.model;

import java.util.List;

public class Worker {
	private long workerId;
	private String workerName;
	private String workerPhoneNumber;
	private String workerCategory;
	private List<CustomerOrder> orders;

	public Worker(String workerName, String workerPhoneNumber, String workerCategory, List<CustomerOrder> orders) {
		super();
		this.workerName = workerName;
		this.workerPhoneNumber = workerPhoneNumber;
		this.workerCategory = workerCategory;
		this.orders = orders;
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerPhoneNumber() {
		return workerPhoneNumber;
	}

	public void setWorkerPhoneNumber(String workerPhoneNumber) {
		this.workerPhoneNumber = workerPhoneNumber;
	}

	public String getWorkerCategory() {
		return workerCategory;
	}

	public void setWorkerCategory(String workerCategory) {
		this.workerCategory = workerCategory;
	}

	public List<CustomerOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<CustomerOrder> orders) {
		this.orders = orders;
	}

	public String displayWorker() {
		return String.format("Worker ID: %d\nWorker Name: %s\nWorker Phone Number: %s\nWorker Category: %s", workerId,
				workerName, workerPhoneNumber, workerCategory);
	}

}
