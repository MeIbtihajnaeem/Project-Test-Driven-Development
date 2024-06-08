/*
 * This class represents the controller responsible for managing orders within the system.
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
import com.mycompany.orderAssignmentSystem.enumerations.OrderSearchOptions;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.OrderView;

/**
 * The Class OrderController.
 */
public class OrderController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(OrderController.class);

	/** The order repository. */
	private OrderRepository orderRepository;

	/** The order view. */
	private OrderView orderView;

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The validation configurations. */
	private ValidationConfigurations validationConfigurations;

	/**
	 * Instantiates a new order controller.
	 *
	 * @param orderRepository          the order repository
	 * @param orderView                the order view
	 * @param validationConfigurations the input validation configuration
	 * @param workerRepository         the worker repository
	 */
	public OrderController(OrderRepository orderRepository, OrderView orderView, WorkerRepository workerRepository,
			ValidationConfigurations validationConfigurations) {
		this.orderRepository = orderRepository;
		this.orderView = orderView;
		this.workerRepository = workerRepository;
		this.validationConfigurations = validationConfigurations;
	}

	/**
	 * Retrieves all orders.
	 */
	public void allOrders() {
		LOGGER.info("Retrieving all orders");
		orderView.showAllOrder(orderRepository.findAll());
	}

	public void allWorkers() {
		LOGGER.info("Retrieving all workers");
		orderView.showAllWorkers(workerRepository.findAll());
	}

	private void add(CustomerOrder order) {
		if (order.getOrderId() != null) {
			throw new IllegalArgumentException("Unable to assign an order ID during order creation.");
		}
		if (order.getOrderStatus() != OrderStatus.PENDING) {
			throw new IllegalArgumentException("The order status should be initiated with 'pending' status.");
		}
		Worker worker = getValidWorker(order);
		if (worker.getOrders().isEmpty()==false) {
//			order = orderRepository.save(order);
//			orderView.orderAdded(order);
//			LOGGER.info("New order created: {}", order);
//			return;
			checkForPendingOrders(worker.getOrders());

		}

		order = orderRepository.save(order);
		orderView.orderAdded(order);
		LOGGER.info("New order created: {}", order);
	}

	private void update(CustomerOrder order) {
		Long id = validationConfigurations.validateStringNumber(order.getOrderId().toString());
		order.setOrderId(id);
		Worker worker = getValidWorker(order);
		if (worker.getOrders().isEmpty()==false) {
//			order = orderRepository.save(order);
//			orderView.orderModified(order);
//			LOGGER.info("Order Updated: {}", order);
//			return;
			checkForPendingOrders(worker.getOrders());
		}
		order = orderRepository.save(order);
		orderView.orderModified(order);
		LOGGER.info("Order Updated: {}", order);
	}

	/**
	 * Creates a new order.
	 *
	 * @param order the order
	 */
	public void createOrUpdateOrder(CustomerOrder order, OperationType operation) {
		try {
			Objects.requireNonNull(operation, "Operation Type is null");
			Objects.requireNonNull(order, "Order is null");
			validateOrder(order);

			switch (operation) {

			case ADD:
				LOGGER.info("Creating a new order");
				add(order);
				break;
			case UPDATE:
				LOGGER.info("Updating an existing order");
				update(order);
				break;
			default:
				LOGGER.info("This operation is not allowed.");
				throw new IllegalArgumentException("This operation is not allowed here");
			}

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while creating or updating Order: " + e.getMessage());
			orderView.showError(e.getMessage(), order);
			return;
		} catch (NoSuchElementException e) {
			LOGGER.error("Error Finding: " + e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
			return;
		}
	}

	/**
	 * Fetches an order by its ID.
	 *
	 * @param order the order
	 */
	public void fetchOrderById(CustomerOrder order) {
		LOGGER.info("Fetching order by ID");

		try {
			Objects.requireNonNull(order, "Order is null.");
			Long id = validationConfigurations.validateStringNumber(order.getOrderId().toString());
			order.setOrderId(id);
			CustomerOrder savedOrder = orderRepository.findById(order.getOrderId());
			if (savedOrder == null) {
				throw new NoSuchElementException("Order with ID " + order.getOrderId() + " not found.");
			}
			LOGGER.info("Order fetched by order ID: {}", order);
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

	/**
	 * Deletes an order.
	 *
	 * @param order the order
	 */
	public void deleteOrder(CustomerOrder order) {
		try {
			Objects.requireNonNull(order, "Order is null");
			Long id = validationConfigurations.validateStringNumber(order.getOrderId().toString());
			order.setOrderId(id);
			CustomerOrder existingOrder = orderRepository.findById(order.getOrderId());
			if (existingOrder == null) {
				throw new NoSuchElementException("No order found with ID: " + order.getOrderId());
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

	/**
	 * Searches for orders based on the specified search text and option.
	 *
	 * @param searchText   the search text
	 * @param searchOption the search option. Available options: ORDER_ID,
	 *                     CUSTOMER_NAME, CUSTOMER_PHONE, DATE, CATEGORY, STATUS,
	 *                     WORKER_ID. If null, an exception is thrown.
	 */
	public void searchOrder(String searchText, OrderSearchOptions searchOption) {
		LOGGER.info("Searching orders by search options");
		try {
			searchText = validationConfigurations.validateSearchString(searchText);
			if (searchOption == null) {
				throw new NullPointerException("Search option cannot be empty.");
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
			case CUSTOMER_NAME:
				orders = searchByCustomerName(searchText);
				break;
			default:
				throw new IllegalArgumentException("This operation is not allowed");
			}
			orderView.showSearchResultForOrder(orders);
			LOGGER.info("Order searched: {}", orders);

		} catch (Exception e) {
			LOGGER.error("Error validating search text: " + e.getMessage());
			orderView.showSearchError(e.getMessage(), searchText);
			return;
		}

	}

	/**
	 * Searches for orders by date.
	 *
	 * @param searchText the search text
	 * @return the list of orders
	 */
	private List<CustomerOrder> searchByDate(String searchText) {
		String date;
		date = validationConfigurations.validateStringDate(searchText);

		List<CustomerOrder> orders = orderRepository.findByDate(date);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with date: " + searchText);
		}
		return orders;
	}

	/**
	 * Searches for orders by category.
	 *
	 * @param searchText the search text
	 * @return the list of orders
	 */
	private List<CustomerOrder> searchByCategory(String searchText) {
		OrderCategory category;
		category = validationConfigurations.validateEnum(searchText, OrderCategory.class);
		List<CustomerOrder> orders = orderRepository.findByOrderCategory(category);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with category: " + category);
		}
		return orders;
	}

	/**
	 * Searches for orders by status.
	 *
	 * @param searchText the search text
	 * @return the list of orders
	 */
	private List<CustomerOrder> searchByStatus(String searchText) {
		OrderStatus status;
		status = validationConfigurations.validateEnum(searchText, OrderStatus.class);
		List<CustomerOrder> orders = orderRepository.findByOrderStatus(status);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with status: " + status);
		}
		return orders;
	}

	/**
	 * Searches for orders by customer name.
	 *
	 * @param searchText the search text
	 * @return the list of orders
	 */
	private List<CustomerOrder> searchByCustomerName(String searchText) {
		String customerName;
		customerName = validationConfigurations.validateName(searchText);
		List<CustomerOrder> orders = orderRepository.findByCustomerName(customerName);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with customer name: " + customerName);
		}
		return orders;
	}

	/**
	 * Searches for orders by customer phone number.
	 *
	 * @param searchText the search text
	 * @return the list of orders
	 */
	private List<CustomerOrder> searchByCustomerPhoneNumber(String searchText) {
		String customerPhoneNumber;
		customerPhoneNumber = validationConfigurations.validatePhoneNumber(searchText);
		List<CustomerOrder> orders = orderRepository.findByCustomerPhoneNumber(customerPhoneNumber);
		if (orders == null || orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with phone number: " + customerPhoneNumber);
		}
		return orders;
	}

	/**
	 * Searches for orders by worker ID.
	 *
	 * @param searchText the search text
	 * @return the list of orders
	 */
	private List<CustomerOrder> searchByWorkerId(String searchText) {
		Long workerId = validateId(searchText);
		Worker worker = workerRepository.findById(workerId);
		if (worker == null) {
			throw new NoSuchElementException("No result found with ID: " + workerId);
		}
		List<CustomerOrder> orders = worker.getOrders();
		if (orders.isEmpty()) {
			throw new NoSuchElementException("No orders found with worker ID: " + workerId);
		}
		return orders;
	}

	/**
	 * Searches for an order by its ID.
	 *
	 * @param searchText the search text
	 * @return the customer order
	 */
	private CustomerOrder searchByOrderId(String searchText) {
		Long orderId = validateId(searchText);
		CustomerOrder order = orderRepository.findById(orderId);
		if (order == null) {
			throw new NoSuchElementException("No result found with ID: " + orderId);
		}
		return order;
	}

	/**
	 * Validates an ID.
	 *
	 * @param searchText the search text
	 * @return the ID as a Long
	 */
	private Long validateId(String searchText) {
		Long id = validationConfigurations.validateStringNumber(searchText);
		return id;
	}

	/**
	 * Validates an order.
	 *
	 * @param order the order
	 */
	private void validateOrder(CustomerOrder order) {
		order.setCustomerName(validationConfigurations.validateName(order.getCustomerName()));
		order.setCustomerPhoneNumber(validationConfigurations.validatePhoneNumber(order.getCustomerPhoneNumber()));
		order.setCustomerAddress(validationConfigurations.validateAddress(order.getCustomerAddress()));
		order.setAppointmentDate(validationConfigurations.validateStringDate(order.getAppointmentDate()));
		order.setOrderDescription(validationConfigurations.validateDescription(order.getOrderDescription()));
		order.setOrderCategory(validationConfigurations.validateCategory(order.getOrderCategory()));
		order.setOrderStatus(validationConfigurations.validateStatus(order.getOrderStatus()));
		Objects.requireNonNull(order.getWorker(), "The worker field cannot be empty.");
		order.getWorker()
				.setWorkerId(validationConfigurations.validateStringNumber(order.getWorker().getWorkerId().toString()));
	}

	/**
	 * Retrieves a valid worker for the order.
	 *
	 * @param order the order
	 * @return the valid worker
	 */
	private Worker getValidWorker(CustomerOrder order) {
		Worker worker = workerRepository.findById(order.getWorker().getWorkerId());
		if (worker == null) {
			throw new NoSuchElementException("Worker with this ID " + order.getWorker().getWorkerId() + " not found");
		}
		if (worker.getWorkerCategory() != order.getOrderCategory()) {
			throw new IllegalArgumentException("Order and worker categories must align");
		}
		return worker;
	}

	/**
	 * Checks for pending orders.
	 *
	 * @param orders the orders
	 */
	private void checkForPendingOrders(List<CustomerOrder> orders) {
		if (orders.stream().anyMatch(ord -> ord.getOrderStatus() == OrderStatus.PENDING)) {
			throw new IllegalArgumentException(
					"Cannot assign a new order to this worker because they already have a pending order.");
		}
	}

}
