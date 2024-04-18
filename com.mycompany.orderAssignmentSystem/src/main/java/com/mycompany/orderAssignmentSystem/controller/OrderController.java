package com.mycompany.orderAssignmentSystem.controller;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.OrderView;

public class OrderController {
	private static final Logger LOGGER = LogManager.getLogger(WorkerController.class);
	private OrderRepository orderRepository;

	private OrderView orderView;

	private WorkerRepository workerRepository;

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

		} catch (Exception e) {
			LOGGER.error("Error validating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		}
	}

}
