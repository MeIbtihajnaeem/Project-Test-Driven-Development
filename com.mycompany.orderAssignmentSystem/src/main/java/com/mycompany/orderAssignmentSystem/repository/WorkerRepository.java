/*
 * WorkerRepository: Interface for accessing and managing workers in the system.
 */
package com.mycompany.orderAssignmentSystem.repository;

import java.util.List;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.model.Worker;

/**
 * Interface for accessing and managing workers in the system.
 */
public interface WorkerRepository {

	/**
	 * Retrieves all workers in the system.
	 *
	 * @return the list of workers
	 */
	public List<Worker> findAll();

	/**
	 * Finds a worker by their ID.
	 *
	 * @param workerId the ID of the worker
	 * @return the found worker, or null if not found
	 */
	public Worker findById(String workerId);

	/**
	 * Finds workers by their name.
	 *
	 * @param workerName the name of the worker
	 * @return the list of workers with matching names or empty list if not found
	 */
	public List<Worker> findByName(String workerName);

	/**
	 * Finds workers by their assigned order category.
	 *
	 * @param category the order category assigned to workers
	 * @return the list of workers with matching order categories or empty list if
	 *         not found
	 */
	public List<Worker> findByOrderCategory(OrderCategory category);

	/**
	 * Finds a worker by their phone number.
	 *
	 * @param phoneNumber the phone number of the worker
	 * @return the found worker by matching phone number, or null if not found
	 */
	public Worker findByPhoneNumber(String phoneNumber);

	/**
	 * Saves a new worker or updates an existing one.
	 *
	 * @param worker the worker to be saved or updated
	 * @return the saved
	 */
	public Worker save(Worker worker);

	/**
	 * Deletes a worker from the system.
	 *
	 * @param worker the worker to be deleted
	 */
	public void delete(Worker worker);

}
