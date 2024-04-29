package com.mycompany.orderAssignmentSystem.view;

import java.util.List;

import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;

public interface WorkerView {
	void showAllWorkers(List<Worker> worker);

	void workerAdded(Worker worker);

	void workerModified(Worker worker);

	void showFetchedWorker(Worker worker);

	void showSearchResultForWorker(List<Worker> worker);

	void showOrderByWorkerId(List<CustomerOrder> orders);

	void workerRemoved(Worker worker);

	void showError(String message, Worker worker);

	void showErrorNotFound(String message, Worker worker);

	void showSearchError(String message, String searchText);

//	void showErrorWorkerNotFound(String message, Worker worker);

}
