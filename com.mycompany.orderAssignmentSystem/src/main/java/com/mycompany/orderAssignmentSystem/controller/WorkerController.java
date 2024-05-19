/*
 * WorkerController: Controller class responsible for handling worker-related operations.
 */
package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.WorkerView;

/**
 * Controller class responsible for handling worker-related operations.
 */
public class WorkerController {

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
	public void getAllWorkers() {
		LOGGER.info("Retrieving all workers");
		workerView.showAllWorkers(workerRepository.findAll());
	}

	private void add(Worker worker) {
		validateNewWorker(worker);
		worker = workerRepository.save(worker);
		workerView.workerAdded(worker);
		LOGGER.info("New worker created: {}", worker);
	}

	private void update(Worker worker) {
		validateUpdateWorker(worker);

		Worker savedWorker = workerRepository.findById(worker.getWorkerId());
		if (savedWorker == null) {
			throw new NoSuchElementException("No Worker found with id: " + worker.getWorkerId());
		}
		if (savedWorker.getOrders() != null && !savedWorker.getOrders().isEmpty()) {
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
	public void createOrUpdateWorker(Worker worker, OperationType operation) {
		try {
			switch (operation) {
			case ADD:
				LOGGER.info("Creating a new order");
				add(worker);
				break;
			case UPDATE:
				LOGGER.info("Updating an existing order");
				update(worker);
				break;
			}

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while " + operation.toString() + " worker: " + e.getMessage());
			workerView.showError(e.getMessage(), worker);
			return;
		}
	}

	/**
	 * Updates an existing worker in the repository.
	 *
	 * @param worker the worker to be updated
	 */
//	public void updateWorker(Worker worker) {
//		LOGGER.info("Updating a worker");
//
//		try {
//			validateUpdateWorker(worker);
//
//			Worker savedWorker = workerRepository.findById(worker.getWorkerId());
//			if (savedWorker == null) {
//				throw new NoSuchElementException("No Worker found with id: " + worker.getWorkerId());
//			}
//			if (savedWorker.getOrders() != null && !savedWorker.getOrders().isEmpty()) {
//				throw new IllegalArgumentException(
//						"Cannot update worker " + worker.getWorkerCategory() + " because of existing orders");
//			}
//
//			worker = workerRepository.modify(worker);
//			workerView.workerModified(worker);
//			LOGGER.info("Worker Updated: {}", worker);
//		} catch (NullPointerException | IllegalArgumentException e) {
//			LOGGER.error("Error validating while updating worker: {}", e.getMessage());
//			workerView.showError(e.getMessage(), worker);
//		} catch (NoSuchElementException e) {
//			LOGGER.error("Error finding worker: {}", e.getMessage());
//			workerView.showErrorNotFound(e.getMessage(), worker);
//		}
//	}

	/**
	 * Fetches a worker by its ID.
	 *
	 * @param worker the worker to be fetched
	 */
	public void fetchWorkerById(Worker worker) {
		LOGGER.info("Fetch a worker");

		try {
//			validateWorkerForFetch(worker);
			validateWorkerAndWorkerId(worker);

			Worker savedWorker = workerRepository.findById(worker.getWorkerId());
			if (savedWorker == null) {
				throw new NoSuchElementException("Worker with id " + worker.getWorkerId() + " Not Found.");
			}
			workerView.showFetchedWorker(worker);
			LOGGER.info("Worker Fetched: {}", worker);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating worker: {}", e.getMessage());
			workerView.showError(e.getMessage(), worker);
		} catch (NoSuchElementException e) {
			LOGGER.error("Error finding worker: {}", e.getMessage());
			workerView.showErrorNotFound(e.getMessage(), worker);
		}
	}

	/**
	 * Deletes a worker from the repository.
	 *
	 * @param worker the worker to be deleted
	 */
	public void deleteWorker(Worker worker) {
		LOGGER.info("Delete a worker");

		try {
			validateWorkerAndWorkerId(worker);
			Worker existingWorker = validateWorkerExistence(worker);
			if (existingWorker.getOrders() != null && !existingWorker.getOrders().isEmpty()) {
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
			LOGGER.error("Error finding worker: {}", e.getMessage());
			workerView.showErrorNotFound(e.getMessage(), worker);
		}
	}

	/**
	 * Fetches orders associated with a worker by their ID.
	 *
	 * @param worker the worker whose orders are to be fetched
	 */
	public void fetchOrdersByWorkerId(Worker worker) {
		LOGGER.info("Fetch a orders by worker Id");

		try {
			validateWorkerAndWorkerId(worker);
			Worker existingWorker = validateWorkerExistence(worker);
			workerView.showOrderByWorkerId(existingWorker.getOrders());
			LOGGER.info("Orders Fetched: {}", worker);
		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating worker: {}", e.getMessage());
			workerView.showSearchOrderByWorkerIdError(e.getMessage(), worker);
		} catch (NoSuchElementException e) {
			LOGGER.error("Error finding worker: {}", e.getMessage());
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
	public void searchWorker(String searchText, WorkerSearchOption searchOption) {
		LOGGER.info("Search workers by search Options");

		try {
			List<Worker> workers = Collections.emptyList();
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
			default:
				workers = searchByWorkerName(searchText);
				break;
			}
			workerView.showSearchResultForWorker(workers);
			LOGGER.info("Worker Searched: {}", workers);

		} catch (Exception e) {
			LOGGER.error("Error validating Search Text: " + e.getMessage());
			workerView.showSearchError(e.getMessage(), searchText);
			return;
		}
	}

	/**
	 * Searches for a worker by their phone number.
	 *
	 * @param searchText the phone number to search for
	 * @return the worker with the specified phone number
	 */
	private Worker searchByWorkerPhoneNumber(String searchText) {
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
	private List<Worker> searchByWorkerCategory(String searchText) {
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
	private List<Worker> searchByWorkerName(String searchText) {
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
	private Worker searchByWorkerId(String searchText) {
		Long workerId = validationConfigurations.validateStringNumber(searchText);
		workerId = validationConfigurations.validateId(workerId);
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
	private Worker validateWorkerExistence(Worker worker) {
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
		worker.setWorkerId(validationConfigurations.validateId(worker.getWorkerId()));
	}

	/**
	 * Validates the worker for fetching.
	 *
	 * @param worker the worker to validate
	 */
//	private void validateWorkerForFetch(Worker worker) {
//		validateWorkerAndWorkerId(worker);
//
//	}

	/**
	 * Validates a new worker.
	 *
	 * @param worker the worker to validate
	 */
	private void validateNewWorker(Worker worker) {
		Objects.requireNonNull(worker, "Worker is null");
		if (worker.getWorkerId() != null) {
			throw new IllegalArgumentException("Unable to assign a worker ID during worker creation.");
		}
		validateWorkerFields(worker);
		validateExistingWorker(worker);

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

	/**
	 * Validates an update to an existing worker.
	 *
	 * @param worker the worker to validate
	 */
	private void validateUpdateWorker(Worker worker) {
		validateWorkerAndWorkerId(worker);
		validateWorkerFields(worker);
		validateExistingWorker(worker);

	}

	/**
	 * Validates the existence of an existing worker.
	 *
	 * @param worker the worker to validate
	 */
	private void validateExistingWorker(Worker worker) {
		Worker existingWorker = workerRepository.findByPhoneNumber(worker.getWorkerPhoneNumber());
		if (existingWorker != null) {
			throw new IllegalArgumentException(
					"Worker with phone number " + worker.getWorkerPhoneNumber() + " Already Exists");
		}
	}
}
