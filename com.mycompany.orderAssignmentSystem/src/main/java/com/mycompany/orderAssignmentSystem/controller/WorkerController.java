/*
 * WorkerController: Controller class responsible for handling worker-related operations.
 */
package com.mycompany.orderassignmentsystem.controller;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.WorkerSearchOption;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.view.WorkerView;

/**
 * Controller class responsible for handling worker-related operations.
 */
public class WorkerController {

	private static final String ERROR_FINDING_WORKER = "Error finding worker: {}";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(WorkerController.class);

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The worker view. */
	private WorkerView workerView;

	/** The validation configurations. */
	private ValidationConfigurations validationConfigurations;

	/**
	 * Instantiates a new worker controller.
	 *
	 * @param workerRepository         the worker repository
	 * @param validationConfigurations the input validation configuration
	 * @param workerView               the worker view
	 */
	public WorkerController(WorkerRepository workerRepository, WorkerView workerView,
			ValidationConfigurations validationConfigurations) {
		this.workerRepository = workerRepository;
		this.workerView = workerView;
		this.validationConfigurations = validationConfigurations;
	}

	/**
	 * Retrieves all workers from the repository and displays them.
	 */
	public synchronized void getAllWorkers() {
		LOGGER.info("Retrieving all workers");

		workerView.showAllWorkers(workerRepository.findAll());
	}

	private synchronized void add(Worker worker) {
		if (worker.getWorkerId() != null) {
			throw new IllegalArgumentException("Unable to assign a worker ID during worker creation.");
		}
		Worker existingWorker = workerRepository.findByPhoneNumber(worker.getWorkerPhoneNumber());
		if (existingWorker != null) {
			throw new IllegalArgumentException(
					"Worker with phone number " + worker.getWorkerPhoneNumber() + " Already Exists");
		}
		worker = workerRepository.save(worker);
		workerView.workerAdded(worker);
		LOGGER.info("New worker created: {}", worker);
	}

	private synchronized void update(Worker worker) {
		Long id = validationConfigurations.validateStringNumber(worker.getWorkerId().toString());
		worker.setWorkerId(id);
		Worker existingWorker = workerRepository.findByPhoneNumber(worker.getWorkerPhoneNumber());
		if (existingWorker != null && existingWorker.getWorkerId() != worker.getWorkerId()) {
			throw new IllegalArgumentException(
					"Worker with phone number " + worker.getWorkerPhoneNumber() + " Already Exists");
		}

		Worker savedWorker = workerRepository.findById(worker.getWorkerId());
		if (savedWorker == null) {
			throw new NoSuchElementException("No Worker found with id: " + worker.getWorkerId());
		}
		if (!savedWorker.getOrders().isEmpty()) {
			throw new IllegalArgumentException(
					"Cannot update worker " + worker.getWorkerCategory() + " because of existing orders");
		}

		worker = workerRepository.save(worker);
		workerView.workerModified(worker);
		LOGGER.info("Worker Updated: {}", worker);
	}

	/**
	 * Creates a new worker and adds it to the repository.
	 *
	 * @param worker the worker to be added
	 */
	public synchronized void createOrUpdateWorker(Worker worker, OperationType operation) {
		try {
			Objects.requireNonNull(worker, "Worker is null");
			validateWorkerFields(worker);

			switch (operation) {
			case ADD:
				LOGGER.info("Creating a new order");
				add(worker);
				break;
			case UPDATE:
				LOGGER.info("Updating an existing order");
				update(worker);
				break;
			default:
				throw new IllegalArgumentException("This operation is not allowed");

			}

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while " + operation.toString() + " worker: " + e.getMessage());
			workerView.showError(e.getMessage(), worker);

		} catch (NoSuchElementException e) {
			LOGGER.error(ERROR_FINDING_WORKER, e.getMessage());
			workerView.showErrorNotFound(e.getMessage(), worker);
		}
	}

	/**
	 * Fetches a worker by its ID.
	 *
	 * @param worker the worker to be fetched
	 */
	public synchronized void fetchWorkerById(Worker worker) {
		LOGGER.info("Fetch a worker");

		try {
			validateWorkerAndWorkerId(worker);

			Worker savedWorker = workerRepository.findById(worker.getWorkerId());
			if (savedWorker == null) {
				throw new NoSuchElementException("Worker with id " + worker.getWorkerId() + " Not Found.");
			}
			workerView.showFetchedWorker(savedWorker);
			LOGGER.info("Worker Fetched: {}", savedWorker);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating worker: {}", e.getMessage());
			workerView.showError(e.getMessage(), worker);
		} catch (NoSuchElementException e) {
			LOGGER.error(ERROR_FINDING_WORKER, e.getMessage());
			workerView.showErrorNotFound(e.getMessage(), worker);
		}
	}

	/**
	 * Deletes a worker from the repository.
	 *
	 * @param worker the worker to be deleted
	 */
	public synchronized void deleteWorker(Worker worker) {
		LOGGER.info("Delete a worker");

		try {
			validateWorkerAndWorkerId(worker);
			worker = validateWorkerExistence(worker);
			if (worker.getOrders() != null && !worker.getOrders().isEmpty()) {
				throw new IllegalArgumentException(
						"Cannot delete worker with orders this worker has " + worker.getOrders().size() + " Orders");
			}
			workerRepository.delete(worker);
			workerView.workerRemoved(worker);
			LOGGER.info("Worker Deleted: {}", worker);
		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating worker: {}", e.getMessage());
			workerView.showError(e.getMessage(), worker);
		} catch (NoSuchElementException e) {
			LOGGER.error(ERROR_FINDING_WORKER, e.getMessage());
			workerView.showErrorNotFound(e.getMessage(), worker);
		}
	}

	/**
	 * Searches for workers based on the specified search option and text.
	 *
	 * @param searchText   the text to search for
	 * @param searchOption the option to search by. Available options: WORKER_ID,
	 *                     WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY. If null, an
	 *                     exception is thrown.
	 */
	public synchronized void searchWorker(String searchText, WorkerSearchOption searchOption) {
		LOGGER.info("Search workers by search Options");

		try {
			List<Worker> workers;
			searchText = validationConfigurations.validateSearchString(searchText);

			if (searchOption == null) {
				throw new NullPointerException("Search Option cannot be empty.");
			}

			switch (searchOption) {
			case WORKER_ID:
				workers = asList(searchByWorkerId(searchText));
				break;
			case WORKER_PHONE:
				workers = asList(searchByWorkerPhoneNumber(searchText));
				break;
			case WORKER_CATEGORY:
				workers = searchByWorkerCategory(searchText);
				break;
			case WORKER_NAME:
				workers = searchByWorkerName(searchText);
				break;
			default:
				throw new IllegalArgumentException("This operation is not allowed");
			}
			workerView.showSearchResultForWorker(workers);
			LOGGER.info("Worker Searched: {}", workers);

		} catch (Exception e) {
			LOGGER.error("Error validating Search Text: " + e.getMessage());
			workerView.showSearchError(e.getMessage(), searchText);

		}
	}

	/**
	 * Searches for a worker by their phone number.
	 *
	 * @param searchText the phone number to search for
	 * @return the worker with the specified phone number
	 */
	private synchronized Worker searchByWorkerPhoneNumber(String searchText) {
		String workerPhoneNumber;
		workerPhoneNumber = validationConfigurations.validatePhoneNumber(searchText);
		Worker worker = workerRepository.findByPhoneNumber(workerPhoneNumber);
		if (worker == null) {
			throw new NoSuchElementException("No result found with phone number: " + searchText);
		}
		return worker;
	}

	/**
	 * Searches for workers by their category.
	 *
	 * @param searchText the category to search for
	 * @return the list of workers with the specified category
	 */
	private synchronized List<Worker> searchByWorkerCategory(String searchText) {
		OrderCategory workerCategory;
		workerCategory = validationConfigurations.validateEnum(searchText, OrderCategory.class);
		List<Worker> workers = workerRepository.findByOrderCategory(workerCategory);
		if (workers == null || workers.isEmpty()) {
			throw new NoSuchElementException("No result found with category: " + searchText);
		}
		return workers;
	}

	/**
	 * Searches for workers by their name.
	 *
	 * @param searchText the name to search for
	 * @return the list of workers with the specified name
	 */
	private synchronized List<Worker> searchByWorkerName(String searchText) {
		String workerName;
		workerName = validationConfigurations.validateName(searchText);
		List<Worker> workers = workerRepository.findByName(workerName);
		if (workers == null || workers.isEmpty()) {
			throw new NoSuchElementException("No result found with Worker Name: " + searchText);
		}
		return workers;
	}

	/**
	 * Searches for a worker by their ID.
	 *
	 * @param searchText the ID to search for
	 * @return the worker with the specified ID
	 */
	private synchronized Worker searchByWorkerId(String searchText) {
		Long workerId = validationConfigurations.validateStringNumber(searchText);
		Worker worker = workerRepository.findById(workerId);
		if (worker == null) {
			throw new NoSuchElementException("No result found with id: " + workerId);
		}
		return worker;
	}

	/**
	 * Validates the existence of a worker.
	 *
	 * @param worker the worker to validate
	 * @return the worker if found
	 */
	private synchronized Worker validateWorkerExistence(Worker worker) {
		Worker existingWorker = workerRepository.findById(worker.getWorkerId());
		if (existingWorker == null) {
			throw new NoSuchElementException("No Worker found with ID: " + worker.getWorkerId());
		}

		return existingWorker;
	}

	/**
	 * Validates the worker and its ID.
	 *
	 * @param worker the worker to validate
	 */
	private void validateWorkerAndWorkerId(Worker worker) {
		Objects.requireNonNull(worker, "Worker is null");
		worker.setWorkerId(validationConfigurations.validateStringNumber(worker.getWorkerId().toString()));
	}

	/**
	 * Validates the fields of a worker.
	 *
	 * @param worker the worker to validate
	 */
	private void validateWorkerFields(Worker worker) {
		worker.setWorkerName(validationConfigurations.validateName(worker.getWorkerName()));
		worker.setWorkerPhoneNumber(validationConfigurations.validatePhoneNumber(worker.getWorkerPhoneNumber()));
		worker.setWorkerCategory(validationConfigurations.validateCategory(worker.getWorkerCategory()));
	}

}
