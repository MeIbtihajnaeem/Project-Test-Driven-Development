package com.mycompany.orderAssignmentSystem.controller;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.OrderView;

public class OrderController {
	private static final Logger LOGGER = LogManager.getLogger(WorkerController.class);
	private OrderRepository orderRepository;

	private OrderView orderView;

	private WorkerRepository workerRepository;
	private ValidationConfigurations validationConfigurations = new ValidationConfigurations();

	public OrderController(OrderRepository orderRepository, OrderView orderView, WorkerRepository workerRepository) {
		this.orderRepository = orderRepository;
		this.orderView = orderView;
		this.workerRepository = workerRepository;
	}

	public void allOrders() {
		LOGGER.info("Retrieving all workers");
		orderView.showAllOrder(orderRepository.findAll());
	}

	public void createNewOrder(CustomerOrder order) {
		Objects.requireNonNull(order, "Order is null");
		if (order.getOrderId() != null) {
			orderView.showError("Unable to assign a Order ID during order creation.", order);
			return;
		}
		try {
			order.setCustomerName(validationConfigurations.validateName(order.getCustomerName()));
			order.setCustomerPhoneNumber(validationConfigurations.validatePhoneNumber(order.getCustomerPhoneNumber()));
			order.setCustomerAddress(validationConfigurations.validateAddress(order.getCustomerAddress()));
			order.setAppointmentDate(validationConfigurations.validateDate(order.getAppointmentDate()));
			order.setOrderDescription(validationConfigurations.validateDescription(order.getOrderDescription()));
			order.setOrderCategory(validationConfigurations.validateCategory(order.getOrderCategory()));
			order.setOrderStatus(validationConfigurations.validateStatus(order.getOrderStatus()));
			if (order.getWorker() == null) {
				orderView.showError("The worker field cannot be empty.", order);
				return;
			}
			order.getWorker().setWorkerId(validationConfigurations.validateId(order.getWorker().getWorkerId()));
			if (order.getOrderStatus() != OrderStatus.PENDING) {
				orderView.showError("The order status should be initiated with 'pending' status.", order);
				return;
			}
			Worker worker = workerRepository.findById(order.getWorker().getWorkerId());
			if (worker == null) {
				orderView.showError("Worker with this id " + order.getWorker().getWorkerId() + " not found", order);
				return;
			}
			if (worker.getWorkerCategory() != order.getOrderCategory()) {
				orderView.showError("Order and worker categories must align", order);
				return;
			}
			if (worker.getOrders() == null) {
				order = orderRepository.save(order);
				orderView.orderAdded(order);
				return;
			}
			if (_workerContainsPendingOrders(worker.getOrders())) {
				orderView.showError(
						"Cannot assign a new order to this worker because they already have a pending order.", order);
				return;
			}

			order = orderRepository.save(order);
			orderView.orderAdded(order);
			return;
		} catch (Exception e) {
			LOGGER.error("Error validating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		}
	}

	private boolean _workerContainsPendingOrders(List<CustomerOrder> orders) {
		for (CustomerOrder ord : orders) {
			if (ord.getOrderStatus() == OrderStatus.PENDING) {
				return true;
			}

		}
		return false;
	}

}
