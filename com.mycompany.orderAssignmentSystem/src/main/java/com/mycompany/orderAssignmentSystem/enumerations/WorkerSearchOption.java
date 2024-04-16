package com.mycompany.orderAssignmentSystem.enumerations;

public enum WorkerSearchOption {
	WORKER_ID("Worker ID"), WORKER_NAME("Worker Name"), WORKER_PHONE("Worker Phone"),
	WORKER_CATEGORY("Worker Category");

	private final String displayName;

	WorkerSearchOption(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
