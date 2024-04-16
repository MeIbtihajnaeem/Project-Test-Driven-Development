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
			String workerName = validationConfigurations.validateName(worker.getWorkerName());
			worker.setWorkerName(workerName);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			workerView.showError(e.getMessage(), worker);
			return;
		}

	}

}
