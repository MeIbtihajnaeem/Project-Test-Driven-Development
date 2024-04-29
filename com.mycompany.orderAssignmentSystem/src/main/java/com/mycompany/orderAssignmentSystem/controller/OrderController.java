package com.mycompany.orderAssignmentSystem.controller;

import java.util.List;
import java.util.NoSuchElementException;
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

		try {

			validateNewOrder(order);
			Worker worker = getValidWorker(order);
			if (worker.getOrders() == null) {
				order = orderRepository.save(order);
				orderView.orderAdded(order);
				return;
			}
			checkForPendingOrders(worker.getOrders());

			order = orderRepository.save(order);
			orderView.orderAdded(order);
		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		} catch (NoSuchElementException e) {
			LOGGER.error("Error Finding: " + e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
			return;
		}
	}

	public void updateOrder(CustomerOrder order) {

		try {

			validateUpdateOrder(order);
			Worker worker = getValidWorker(order);
			validateWorkerUnchangedForOrderUpdate(order);
			if (worker.getOrders() == null) {
				order = orderRepository.modify(order);
				orderView.orderModified(order);
				return;
			}
			checkForPendingOrders(worker.getOrders());

			order = orderRepository.modify(order);
			orderView.orderModified(order);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		} catch (NoSuchElementException e) {
			LOGGER.error("Error Finding: " + e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
			return;
		}
	}

	private void validateNewOrder(CustomerOrder order) {
		Objects.requireNonNull(order, "Order is null");
		if (order.getOrderId() != null) {
			throw new IllegalArgumentException("Unable to assign a Order ID during order creation.");
		}
		validateOrder(order);
		if (order.getOrderStatus() != OrderStatus.PENDING) {
			throw new IllegalArgumentException("The order status should be initiated with 'pending' status.");
		}
	}

	private void validateOrder(CustomerOrder order) {
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

	private Worker getValidWorker(CustomerOrder order) {
		Worker worker = workerRepository.findById(order.getWorker().getWorkerId());
		if (worker == null) {
			throw new NoSuchElementException("Worker with this id " + order.getWorker().getWorkerId() + " not found");
		}
		if (worker.getWorkerCategory() != order.getOrderCategory()) {
			throw new IllegalArgumentException("Order and worker categories must align");
		}
		return worker;
	}

	private void checkForPendingOrders(List<CustomerOrder> orders) {
		if (orders.stream().anyMatch(ord -> ord.getOrderStatus() == OrderStatus.PENDING)) {
			throw new IllegalArgumentException(
					"Cannot assign a new order to this worker because they already have a pending order.");
		}
	}

	private void validateUpdateOrder(CustomerOrder order) {
		Objects.requireNonNull(order, "Order is null");
		order.setOrderId(validationConfigurations.validateId(order.getOrderId()));
		validateOrder(order);
	}

	private CustomerOrder validateWorkerUnchangedForOrderUpdate(CustomerOrder order) {
		CustomerOrder savedOrder = orderRepository.findById(order.getWorker().getWorkerId());
		if (savedOrder.getWorker().getWorkerId() == order.getWorker().getWorkerId()) {
			throw new IllegalArgumentException("Cannot update order because it is assigned to the same worker.");
		}
		return savedOrder;
	}

}
