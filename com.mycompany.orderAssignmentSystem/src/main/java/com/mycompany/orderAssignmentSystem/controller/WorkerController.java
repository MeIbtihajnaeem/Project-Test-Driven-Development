package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.WorkerView;

public class WorkerController {

	private static final Logger LOGGER = LogManager.getLogger(WorkerController.class);
	private WorkerRepository workerRepository;
	private WorkerView workerView;
	private ValidationConfigurations validationConfigurations = new ValidationConfigurations();

	public WorkerController(WorkerRepository workerRepository, WorkerView workerView) {
		this.workerRepository = workerRepository;
		this.workerView = workerView;
	}

	public void getAllWorkers() {
		LOGGER.info("Retrieving all workers");
		workerView.showAllWorkers(workerRepository.findAll());
	}

	public void createNewWorker(Worker worker) {
		LOGGER.info("Creating a new worker");

		try {
			validateNewWorker(worker);
			worker = workerRepository.save(worker);
			workerView.workerAdded(worker);
			LOGGER.info("New worker created: {}", worker);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while creating worker: " + e.getMessage());
			workerView.showError(e.getMessage(), worker);
			return;
		}
	}

	public void updateWorker(Worker worker) {
		LOGGER.info("Updating a worker");

		try {
			validateUpdateWorker(worker);

			Worker savedWorker = workerRepository.findById(worker.getWorkerId());
			if (savedWorker != null && savedWorker.getOrders() != null && !savedWorker.getOrders().isEmpty()) {
				throw new IllegalArgumentException(
						"Cannot update worker " + worker.getWorkerCategory() + " because of existing orders");
			}

			worker = workerRepository.modify(worker);
			workerView.workerModified(worker);
			LOGGER.info("Worker Updated: {}", worker);
		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating worker: {}", e.getMessage());
			workerView.showError(e.getMessage(), worker);
		}
	}

	public void fetchWorkerById(Worker worker) {
		LOGGER.info("Fetch a worker");

		try {
			validateWorkerForFetch(worker);
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

	public void fetchOrdersByWorkerId(Worker worker) {
		LOGGER.info("Fetch a orders by worker Id");

		try {
			validateWorkerAndWorkerId(worker);
			Worker existingWorker = validateWorkerExistence(worker);
			workerView.showOrderByWorkerId(existingWorker.getOrders());
			LOGGER.info("Orders Fetched: {}", worker);
		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating worker: {}", e.getMessage());
			workerView.showError(e.getMessage(), worker);
		} catch (NoSuchElementException e) {
			LOGGER.error("Error finding worker: {}", e.getMessage());
			workerView.showErrorNotFound(e.getMessage(), worker);
		}

	}

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
			case WORKER_NAME:
				workers = searchByWorkerName(searchText);
				break;
			case WORKER_CATEGORY:
				workers = searchByWorkerCategory(searchText);
				break;
			default:
				workers = asList(searchByWorkerPhoneNumber(searchText));
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

	private Worker searchByWorkerPhoneNumber(String searchText) {
		String workerPhoneNumber;
		workerPhoneNumber = validationConfigurations.validatePhoneNumber(searchText);
		Worker worker = workerRepository.findByPhoneNumber(workerPhoneNumber);
		return worker;
	}

	private List<Worker> searchByWorkerCategory(String searchText) {
		OrderCategory workerCategory;
		workerCategory = validationConfigurations.validateEnum(searchText, OrderCategory.class);
		List<Worker> workers = workerRepository.findByOrderCategory(workerCategory);
		return workers;
	}

	private List<Worker> searchByWorkerName(String searchText) {
		String workerName;
		workerName = validationConfigurations.validateName(searchText);
		List<Worker> workers = workerRepository.findByName(workerName);
		return workers;
	}

	private Worker searchByWorkerId(String searchText) {
		if (validationConfigurations.validateStringNumber(searchText)) {
			Long workerId;
			workerId = Long.parseLong(searchText);
			workerId = validationConfigurations.validateId(workerId);
			Worker worker = workerRepository.findById(workerId);
			return worker;

		} else {
			throw new IllegalArgumentException("Please enter a valid number.");
		}
	}

	private Worker validateWorkerExistence(Worker worker) {
		Worker existingWorker = workerRepository.findById(worker.getWorkerId());
		if (existingWorker == null) {
			throw new NoSuchElementException("No Worker found with ID: " + worker.getWorkerId());
		}
		return existingWorker;
	}

	private void validateWorkerAndWorkerId(Worker worker) {
		Objects.requireNonNull(worker, "Worker is null");
		worker.setWorkerId(validationConfigurations.validateId(worker.getWorkerId()));
	}

	private void validateWorkerForFetch(Worker worker) {
		validateWorkerAndWorkerId(worker);

	}

	private void validateNewWorker(Worker worker) {
		Objects.requireNonNull(worker, "Worker is null");
		if (worker.getWorkerId() != null) {
			throw new IllegalArgumentException("Unable to assign a worker ID during worker creation.");
		}
		validateWorkerFields(worker);
		validateExistingWorker(worker);

	}

	private void validateWorkerFields(Worker worker) {
		worker.setWorkerName(validationConfigurations.validateName(worker.getWorkerName()));
		worker.setWorkerPhoneNumber(validationConfigurations.validatePhoneNumber(worker.getWorkerPhoneNumber()));
		worker.setWorkerCategory(validationConfigurations.validateCategory(worker.getWorkerCategory()));
	}

	private void validateUpdateWorker(Worker worker) {
		validateWorkerAndWorkerId(worker);
		validateWorkerFields(worker);
		validateExistingWorker(worker);

	}

	private void validateExistingWorker(Worker worker) {
		Worker existingWorker = workerRepository.findByPhoneNumber(worker.getWorkerPhoneNumber());
		if (existingWorker != null) {
			throw new IllegalArgumentException(
					"Worker with phone number " + worker.getWorkerPhoneNumber() + " Already Exists");
		}
	}
}
