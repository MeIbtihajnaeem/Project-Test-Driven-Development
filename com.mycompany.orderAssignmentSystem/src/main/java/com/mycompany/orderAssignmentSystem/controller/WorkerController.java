package com.mycompany.orderAssignmentSystem.controller;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
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

}
