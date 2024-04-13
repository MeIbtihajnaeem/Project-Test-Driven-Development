package com.mycompany.orderAssignmentSystem.model;

public class Worker {
	final long workerId;
	final String workerName;
	final String workerPhoneNumber;
	final String workerCategory;

	public Worker(long workerId, String workerName, String workerPhoneNumber, String workerCategory) {
		super();
		this.workerId = workerId;
		this.workerName = workerName;
		this.workerPhoneNumber = workerPhoneNumber;
		this.workerCategory = workerCategory;
	}
}
