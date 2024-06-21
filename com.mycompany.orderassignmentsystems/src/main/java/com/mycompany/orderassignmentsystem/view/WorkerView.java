/*
 * WorkerView: Interface for displaying and interacting with workers in the system.
 */
package com.mycompany.orderassignmentsystem.view;

import java.util.List;

import com.mycompany.orderassignmentsystem.model.Worker;

/**
 * Interface for displaying and interacting with workers in the system.
 */
public interface WorkerView {

	/**
	 * Displays all workers.
	 *
	 * @param worker the list of workers to be displayed
	 */
	void showAllWorkers(List<Worker> worker);

	/**
	 * Notifies when a new worker is added.
	 *
	 * @param worker the newly added worker
	 */
	void workerAdded(Worker worker);

	/**
	 * Notifies when a worker is modified.
	 *
	 * @param worker the modified worker
	 */
	void workerModified(Worker worker);

	/**
	 * Displays a fetched worker.
	 *
	 * @param worker the worker to be displayed
	 */
	void showFetchedWorker(Worker worker);

	/**
	 * Shows the search result for workers.
	 *
	 * @param worker the list of workers matching the search criteria
	 */
	void showSearchResultForWorker(List<Worker> worker);

	/**
	 * Notifies when a worker is removed.
	 *
	 * @param worker the removed worker
	 */
	void workerRemoved(Worker worker);

	/**
	 * Displays an error message related to a specific worker. while adding,
	 * updating or fetching
	 *
	 * @param message the error message to be displayed
	 * @param worker  the worker associated with the error
	 */
	void showError(String message, Worker worker);

	/**
	 * Displays an error message when a worker is not found.
	 *
	 * @param message the error message to be displayed
	 * @param worker  the worker associated with the error
	 */
	void showErrorNotFound(String message, Worker worker);

	/**
	 * Displays an error message related to a search operation.
	 *
	 * @param message    the error message to be displayed
	 * @param searchText the search text associated with the error
	 */
	void showSearchError(String message, String searchText);

}
