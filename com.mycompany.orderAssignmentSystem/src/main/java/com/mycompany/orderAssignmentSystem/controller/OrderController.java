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
	private static final Logger LOGGER = LogManager.getLogger(OrderController.class);
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

		try {
			if (order.getOrderId() != null) {
				throw new IllegalArgumentException("Unable to assign a Order ID during order creation.");
			}
			_validateOrder(order);

			if (order.getOrderStatus() != OrderStatus.PENDING) {
				throw new IllegalArgumentException("The order status should be initiated with 'pending' status.");
			}
			Worker worker = workerRepository.findById(order.getWorker().getWorkerId());
			_validateWorker(order, worker);
			if (worker.getOrders() == null) {
				order = orderRepository.save(order);
				orderView.orderAdded(order);
				return;
			}
			if (_workerContainsPendingOrders(worker.getOrders())) {
				throw new IllegalArgumentException(
						"Cannot assign a new order to this worker because they already have a pending order.");
			}

			order = orderRepository.save(order);
			orderView.orderAdded(order);
		} catch (Exception e) {
			LOGGER.error("Error validating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		}
	}

	public void updateOrder(CustomerOrder order) {
		Objects.requireNonNull(order, "Order is null");

		try {

			order.setOrderId(validationConfigurations.validateId(order.getOrderId()));
			_validateOrder(order);
			Worker worker = workerRepository.findById(order.getWorker().getWorkerId());
			_validateWorker(order, worker);
			CustomerOrder savedOrder = orderRepository.findById(order.getWorker().getWorkerId());
			if (savedOrder.getWorker().getWorkerId() == order.getWorker().getWorkerId()) {
				throw new IllegalArgumentException("Cannot update order because it is assigned to the same worker.");
			}
			if (worker.getOrders() == null) {
				order = orderRepository.modify(order);
				orderView.orderModified(order);
				return;
			}
			if (_workerContainsPendingOrders(worker.getOrders())) {
				throw new IllegalArgumentException(
						"Cannot assign a new order to this worker because they already have a pending order.");
			}

			order = orderRepository.modify(order);
			orderView.orderModified(order);

		} catch (Exception e) {
			LOGGER.error("Error validating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		}
	}

	private void _validateOrder(CustomerOrder order) {
		order.setCustomerName(validationConfigurations.validateName(order.getCustomerName()));
		order.setCustomerPhoneNumber(validationConfigurations.validatePhoneNumber(order.getCustomerPhoneNumber()));
		order.setCustomerAddress(validationConfigurations.validateAddress(order.getCustomerAddress()));
		order.setAppointmentDate(validationConfigurations.validateDate(order.getAppointmentDate()));
		order.setOrderDescription(validationConfigurations.validateDescription(order.getOrderDescription()));
		order.setOrderCategory(validationConfigurations.validateCategory(order.getOrderCategory()));
		order.setOrderStatus(validationConfigurations.validateStatus(order.getOrderStatus()));
		Objects.requireNonNull(order.getWorker(), "The worker field cannot be empty.");
		order.getWorker().setWorkerId(validationConfigurations.validateId(order.getWorker().getWorkerId()));
	}

	private void _validateWorker(CustomerOrder order, Worker worker) {
		Objects.requireNonNull(worker, "Worker with this id " + order.getWorker().getWorkerId() + " not found");
		if (worker.getWorkerCategory() != order.getOrderCategory()) {
			throw new IllegalArgumentException("Order and worker categories must align");
		}
	}

	private boolean _workerContainsPendingOrders(List<CustomerOrder> orders) {
		return orders.stream().anyMatch(ord -> ord.getOrderStatus() == OrderStatus.PENDING);
	}

}
