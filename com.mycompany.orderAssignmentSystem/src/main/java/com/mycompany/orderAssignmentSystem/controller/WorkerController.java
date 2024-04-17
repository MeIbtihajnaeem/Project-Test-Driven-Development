package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		Objects.requireNonNull(worker, "Worker is null");
		if (worker.getWorkerId() != null) {
			workerView.showError("Unable to assign a worker ID during worker creation.", worker);
			return;
		}
		try {
			worker.setWorkerName(validationConfigurations.validateName(worker.getWorkerName()));
			worker.setWorkerPhoneNumber(validationConfigurations.validatePhoneNumber(worker.getWorkerPhoneNumber()));
			worker.setWorkerCategory(validationConfigurations.validateCategory(worker.getWorkerCategory()));

		} catch (Exception e) {
			LOGGER.error("Error validating Worker: " + e.getMessage());
			workerView.showError(e.getMessage(), worker);
			return;
		}

		Worker existingWorker = workerRepository.findByPhoneNumber(worker.getWorkerPhoneNumber());
		if (existingWorker != null) {
			LOGGER.error("Error validating worker phone number: Worker with phone number "
					+ worker.getWorkerPhoneNumber() + " Already Exists");
			workerView.showError("Worker with phone number " + worker.getWorkerPhoneNumber() + " Already Exists",
					existingWorker);
			return;
		}
		worker = workerRepository.save(worker);
		workerView.workerAdded(worker);
		LOGGER.info("New worker created: {}", worker);
	}

	public void updateWorker(Worker worker) {
		LOGGER.info("Updating a worker");
		Objects.requireNonNull(worker, "Worker is null");
		try {
			worker.setWorkerId(validationConfigurations.validateId(worker.getWorkerId()));
			worker.setWorkerName(validationConfigurations.validateName(worker.getWorkerName()));
			worker.setWorkerPhoneNumber(validationConfigurations.validatePhoneNumber(worker.getWorkerPhoneNumber()));
			worker.setWorkerCategory(validationConfigurations.validateCategory(worker.getWorkerCategory()));

		} catch (Exception e) {
			LOGGER.error("Error validating Worker: " + e.getMessage());
			workerView.showError(e.getMessage(), worker);
			return;
		}

		Worker existingWorker = workerRepository.findByPhoneNumber(worker.getWorkerPhoneNumber());
		if (existingWorker != null) {
			LOGGER.error("A Worker with this phone number " + worker.getWorkerPhoneNumber() + " Already Exists");
			workerView.showError("A Worker with this phone number " + worker.getWorkerPhoneNumber() + " Already Exists",
					worker);
			return;

		}
		Worker savedWorker = workerRepository.findById(worker.getWorkerId());
		if (savedWorker != null) {
			if (savedWorker.getOrders() != null) {
				if (!savedWorker.getOrders().isEmpty()) {
					LOGGER.error("Cannot update worker " + worker.getWorkerCategory() + " Because of existing orders");
					workerView.showError(
							"Cannot update worker " + worker.getWorkerCategory() + " Because of existing orders",
							worker);
					return;
				}
			}
		}
		worker = workerRepository.modify(worker);
		workerView.workerModified(worker);
		LOGGER.info("Worker Updated: {}", worker);

	}

	public void fetchWorkerById(Worker worker) {
		LOGGER.info("Fetch a worker");
		Objects.requireNonNull(worker, "Worker is null");

		try {
			worker.setWorkerId(validationConfigurations.validateId(worker.getWorkerId()));
		} catch (Exception e) {
			LOGGER.error("Error validating Worker: " + e.getMessage());
			workerView.showError(e.getMessage(), worker);
			return;
		}
		Worker savedWorker = workerRepository.findById(worker.getWorkerId());
		if (savedWorker == null) {
			LOGGER.error("Worker with id " + worker.getWorkerId() + " Not Found.");
			workerView.showError("Worker with id " + worker.getWorkerId() + " Not Found.", worker);
			return;
		}

		workerView.showFetchedWorker(worker);
	}

	public void searchWorker(String searchText, WorkerSearchOption searchOption) {
		try {
			searchText = validationConfigurations.validateSearchString(searchText);
		} catch (Exception e) {
			LOGGER.error("Error validating Search Text: " + e.getMessage());
			workerView.showSearchError(e.getMessage(), searchText);
			return;
		}
		if (searchOption == null) {
			LOGGER.error("Worker Search Option is Null");
			workerView.showSearchError("Search Option cannot be empty.", searchText);
			return;
		}
		if (searchOption == WorkerSearchOption.WORKER_ID) {
			Long workerId;
			if (!_containsOnlyNumbers(searchText)) {
				LOGGER.error("Error Worker id Invalid: Worker id is not number");
				workerView.showSearchError("Invalid worker id, it must be a number.", searchText);
				return;
			}
			workerId = Long.parseLong(searchText);
			try {
				workerId = validationConfigurations.validateId(workerId);
			} catch (Exception e) {
				LOGGER.error("Error validating Search Text Id: " + e.getMessage());
				workerView.showSearchError(e.getMessage(), searchText);
				return;
			}
			Worker worker = workerRepository.findById(workerId);
			workerView.showSearchResultForWorker(asList(worker));
			return;
		} else if (searchOption == WorkerSearchOption.WORKER_NAME) {
			String workerName;
			try {
				workerName = validationConfigurations.validateName(searchText);
			} catch (Exception e) {
				LOGGER.error("Error validating Search Text Name: " + e.getMessage());
				workerView.showSearchError(e.getMessage(), searchText);
				return;
			}
			List<Worker> workers = workerRepository.findByName(workerName);
			workerView.showSearchResultForWorker(workers);
			return;
		} else if (searchOption == WorkerSearchOption.WORKER_CATEGORY) {
			OrderCategory workerCategory;
			try {
				workerCategory = validationConfigurations.validateEnum(searchText, OrderCategory.class);
			} catch (Exception e) {
				LOGGER.error("Error validating Search Text worker Category: " + e.getMessage());
				workerView.showSearchError(e.getMessage(), searchText);
				return;
			}
			List<Worker> workers = workerRepository.findByOrderCategory(workerCategory);
			workerView.showSearchResultForWorker(workers);
			return;
		} else {
			String workerPhoneNumber;
			try {
				workerPhoneNumber = validationConfigurations.validatePhoneNumber(searchText);
			} catch (Exception e) {
				LOGGER.error("Error validating Search Text Phone Number: " + e.getMessage());
				workerView.showSearchError(e.getMessage(), searchText);
				return;
			}
			Worker worker = workerRepository.findByPhoneNumber(workerPhoneNumber);
			workerView.showSearchResultForWorker(asList(worker));
			return;
		}
	}

	public void deleteWorker(Worker worker) {
		Objects.requireNonNull(worker, "Worker is null");
		try {
			worker.setWorkerId(validationConfigurations.validateId(worker.getWorkerId()));
		} catch (Exception e) {
			LOGGER.error("Error validating Worker Id: " + e.getMessage());
			workerView.showError(e.getMessage(), worker);
			return;
		}
		Worker existingWorker = workerRepository.findById(worker.getWorkerId());
		if (existingWorker == null) {
			LOGGER.error("No Worker found with ID: " + worker.getWorkerId());
			workerView.showError("No Worker found with ID: " + worker.getWorkerId(), worker);
			return;
		}
		if (existingWorker.getOrders() != null) {
			if (!existingWorker.getOrders().isEmpty()) {
				LOGGER.error(
						"Cannot delete worker with orders this worker has " + worker.getOrders().size() + " Orders");
				workerView.showError(
						"Cannot delete worker with orders this worker has " + worker.getOrders().size() + " Orders",
						worker);
				return;
			}
		}
		worker = workerRepository.delete(worker);
		workerView.workerRemoved(worker);
	}

	private boolean _containsOnlyNumbers(String str) {
		Pattern pattern = Pattern.compile("^-?[0-9]+$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

}
