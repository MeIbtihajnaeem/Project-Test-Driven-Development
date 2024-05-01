package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderSearchOptions;
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
		LOGGER.info("Retrieving all orders");
		orderView.showAllOrder(orderRepository.findAll());
	}

	public void createNewOrder(CustomerOrder order) {
		LOGGER.info("Creating a new order");

		try {
			validateNewOrder(order);
			Worker worker = getValidWorker(order);
			if (worker.getOrders() == null) {
				order = orderRepository.save(order);
				orderView.orderAdded(order);
				LOGGER.info("New order created: {}", order);
				return;
			}
			checkForPendingOrders(worker.getOrders());

			order = orderRepository.save(order);
			orderView.orderAdded(order);
			LOGGER.info("New order created: {}", order);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while creating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		} catch (NoSuchElementException e) {
			LOGGER.error("Error Finding: " + e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
			return;
		}
	}

	public void updateOrder(CustomerOrder order) {
		LOGGER.info("Updating a order");

		try {

			validateUpdateOrder(order);
			Worker worker = getValidWorker(order);
			CustomerOrder savedOrder = orderRepository.findById(order.getWorker().getWorkerId());
			if (savedOrder.getWorker().getWorkerId() == order.getWorker().getWorkerId()) {
				throw new IllegalArgumentException("Cannot update order because it is assigned to the same worker.");
			}
			if (worker.getOrders() == null) {
				order = orderRepository.modify(order);
				orderView.orderModified(order);
				LOGGER.info("Order Updated: {}", order);
				return;
			}
			checkForPendingOrders(worker.getOrders());

			order = orderRepository.modify(order);
			orderView.orderModified(order);
			LOGGER.info("Order Updated: {}", order);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		} catch (NoSuchElementException e) {
			LOGGER.error("Error Finding: " + e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
			return;
		}
	}

	public void fetchOrderById(CustomerOrder order) {
		LOGGER.info("Fetching order by id");

		try {
			Objects.requireNonNull(order, "Order is null.");
			order.setOrderId(validationConfigurations.validateId(order.getOrderId()));
			CustomerOrder savedOrder = orderRepository.findById(order.getOrderId());
			if (savedOrder == null) {
				throw new NoSuchElementException("Order with id " + order.getOrderId() + " Not Found.");
			}
			LOGGER.info("Order Fetched by order id: {}", order);
			orderView.showFetchedOrder(savedOrder);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		} catch (NoSuchElementException e) {
			LOGGER.error("Error Finding: " + e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
			return;
		}
	}

	public void deleteOrder(CustomerOrder order) {
		try {
			Objects.requireNonNull(order, "Order is null");
			order.setOrderId(validationConfigurations.validateId(order.getOrderId()));
			CustomerOrder existingOrder = orderRepository.findById(order.getOrderId());
			if (existingOrder == null) {
				throw new NoSuchElementException("No Order found with ID: " + order.getOrderId());
			}
			orderRepository.delete(order);
			orderView.orderRemoved(order);
		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		} catch (NoSuchElementException e) {
			LOGGER.error("Error Finding: " + e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
			return;
		}

	}

	public void searchOrder(String searchText, OrderSearchOptions searchOption) {
		LOGGER.info("Search orders by search Options");
		try {
			searchText = validationConfigurations.validateSearchString(searchText);
			if (searchOption == null) {
				throw new NullPointerException("Search Option cannot be empty.");
			}
			List<CustomerOrder> orders = Collections.emptyList();

			switch (searchOption) {
			case ORDER_ID:
				orders = asList(searchByOrderId(searchText));
				break;
			case WORKER_ID:
				orders = searchByWorkerId(searchText);
				break;
			case CUSTOMER_PHONE:
				orders = searchByCustomerPhoneNumber(searchText);
				break;
			case DATE:
				orders = searchByDate(searchText);
				break;
			case STATUS:
				orders = searchByStatus(searchText);
				break;
			case CATEGORY:
				orders = searchByCategory(searchText);
				break;
			default:
				orders = searchByCustomerName(searchText);
				break;
			}
			orderView.showSearchResultForOrder(orders);
			LOGGER.info("Order Searched: {}", orders);

		} catch (Exception e) {
			LOGGER.error("Error validating Search Text: " + e.getMessage());
			orderView.showSearchError(e.getMessage(), searchText);
			return;
		}

	}

	private List<CustomerOrder> searchByDate(String searchText) {
		LocalDate date;
		date = validationConfigurations.validateStringDate(searchText);

		List<CustomerOrder> orders = orderRepository.findByDate(date);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with date: " + searchText);
		}
		return orders;
	}

	private List<CustomerOrder> searchByCategory(String searchText) {
		OrderCategory category;
		category = validationConfigurations.validateEnum(searchText, OrderCategory.class);
		List<CustomerOrder> orders = orderRepository.findByOrderCategory(category);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with category: " + category);
		}
		return orders;
	}

	private List<CustomerOrder> searchByStatus(String searchText) {
		OrderStatus status;
		status = validationConfigurations.validateEnum(searchText, OrderStatus.class);
		List<CustomerOrder> orders = orderRepository.findByOrderStatus(status);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with status: " + status);
		}
		return orders;
	}

	private List<CustomerOrder> searchByCustomerName(String searchText) {
		String customerName;
		customerName = validationConfigurations.validateName(searchText);
		List<CustomerOrder> orders = orderRepository.findByCustomerName(customerName);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with Customer name: " + customerName);
		}
		return orders;
	}

	private List<CustomerOrder> searchByCustomerPhoneNumber(String searchText) {
		String customerPhoneNumber;
		customerPhoneNumber = validationConfigurations.validatePhoneNumber(searchText);
		List<CustomerOrder> orders = orderRepository.findByCustomerPhoneNumber(customerPhoneNumber);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with phone number: " + customerPhoneNumber);
		}
		return orders;
	}

	private List<CustomerOrder> searchByWorkerId(String searchText) {
		Long workerId = validateId(searchText);
		Worker worker = workerRepository.findById(workerId);
		if (worker == null) {
			throw new NoSuchElementException("No result found with id: " + workerId);
		}
		List<CustomerOrder> orders = worker.getOrders();
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with worker id: " + workerId);
		}
		return orders;
	}

	private CustomerOrder searchByOrderId(String searchText) {
		Long orderId = validateId(searchText);
		CustomerOrder order = orderRepository.findById(orderId);
		if (order == null) {
			throw new NoSuchElementException("No result found with id: " + orderId);
		}
		return order;
	}

	private Long validateId(String searchText) {
		Long id = validationConfigurations.validateStringNumber(searchText);
		id = Long.parseLong(searchText);
		id = validationConfigurations.validateId(id);
		return id;
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

}
