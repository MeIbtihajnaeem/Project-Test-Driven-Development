package com.mycompany.orderAssignmentSystem.model;

import java.util.List;
import java.util.Objects;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;

public class Worker {
	private Long workerId;
	private String workerName;
	private String workerPhoneNumber;
	private OrderCategory workerCategory;
	private List<CustomerOrder> orders;

	public Worker() {
	};

	public Worker(Long workerId, String workerName, String workerPhoneNumber, OrderCategory workerCategory) {
		super();
		this.workerId = workerId;
		this.workerName = workerName;
		this.workerPhoneNumber = workerPhoneNumber;
		this.workerCategory = workerCategory;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
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

	public OrderCategory getWorkerCategory() {
		return workerCategory;
	}

	public void setWorkerCategory(OrderCategory workerCategory) {
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

	@Override
	public int hashCode() {
		return Objects.hash(orders, workerCategory, workerId, workerName, workerPhoneNumber);
	}

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
	
	

}
