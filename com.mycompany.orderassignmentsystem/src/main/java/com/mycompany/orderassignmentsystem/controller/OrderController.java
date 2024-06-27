/*
 * The OrderController class is responsible for managing customer orders within the system.
 * 
 * This class provides methods to:
 * - Retrieve all orders and workers.
 * - Create or update orders with validation.
 * - Fetch orders by ID.
 * - Delete orders.
 * - Search for orders based on various criteria.
 * 
 * 
 * Key functionalities include:
 * - Adding new orders while ensuring the initial status is 'pending' and no order ID is pre-assigned.
 * - Updating existing orders with validation and checking for category alignment between orders and workers.
 * - Deleting orders and handling non-existing order scenarios.
 * - Searching for orders by ID, worker ID, customer phone number, date, category, status, and customer name.
 * - Validating order details such as customer name, phone number, address, date, description, category, and status.
 * 
 * The class ensures proper logging for operations and handles exceptions by displaying appropriate error messages in the view.
 * 
 * @see OrderRepository
 * @see WorkerRepository
 * @see OrderView
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 */
package com.mycompany.orderassignmentsystem.controller;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderSearchOptions;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.view.OrderView;

/**
 * The Class OrderController.
 */
public class OrderController {

	/** The Constant ERROR_FINDING. */
	private static final String ERROR_FINDING = "Error Finding: {}";

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
	 * @param workerRepository         the worker repository
	 * @param validationConfigurations the input validation configuration
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
	public synchronized void allOrders() {
		LOGGER.info("Retrieving all orders");
		orderView.showAllOrder(orderRepository.findAll());
	}

	/**
	 * Retrieves all workers.
	 */
	public synchronized void allWorkers() {
		LOGGER.info("Retrieving all workers");
		orderView.showAllWorkers(workerRepository.findAll());
	}

	/**
	 * Creates a new order.
	 *
	 * @param order     the order
	 * @param operation the operation
	 */
	public synchronized void createOrUpdateOrder(CustomerOrder order, OperationType operation) {
		try {
			Objects.requireNonNull(operation, "Operation Type is null");
			Objects.requireNonNull(order, "Order is null");

			switch (operation) {

			case ADD:
				validateOrder(order);
				LOGGER.info("Creating a new order");
				add(order);
				break;
			case UPDATE:
				validateOrder(order);
				LOGGER.info("Updating an existing order");
				update(order);
				break;
			default:
				LOGGER.info("This operation is not allowed.");
				throw new IllegalArgumentException("This operation is not allowed here");
			}

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while creating or updating Order: {}", e.getMessage());
			orderView.showError(e.getMessage(), order);
		} catch (NoSuchElementException e) {
			LOGGER.error(ERROR_FINDING, e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
		}
	}

	/**
	 * Fetches an order by its ID.
	 *
	 * @param order the order
	 */
	public synchronized void fetchOrderById(CustomerOrder order) {
		LOGGER.info("Fetching order by ID");

		try {
			Objects.requireNonNull(order, "Order is null.");
			validationConfigurations.validateStringNumber(order.getOrderId().toString());
			CustomerOrder savedOrder = orderRepository.findById(order.getOrderId());
			if (savedOrder == null) {
				throw new NoSuchElementException("Order with ID " + order.getOrderId() + " not found.");
			}
			LOGGER.info("Order fetched by order ID: {}", order);
			orderView.showFetchedOrder(savedOrder);

		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating Order: {}", e.getMessage());
			orderView.showError(e.getMessage(), order);
		} catch (NoSuchElementException e) {
			LOGGER.error(ERROR_FINDING, e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
		}
	}

	/**
	 * Deletes an order.
	 *
	 * @param order the order
	 */
	public synchronized void deleteOrder(CustomerOrder order) {
		try {
			Objects.requireNonNull(order, "Order is null");
			validationConfigurations.validateStringNumber(order.getOrderId().toString());
			CustomerOrder existingOrder = orderRepository.findById(order.getOrderId());
			if (existingOrder == null) {
				throw new NoSuchElementException("No order found with ID: " + order.getOrderId());
			}
			orderRepository.delete(order);
			orderView.orderRemoved(order);
		} catch (NullPointerException | IllegalArgumentException e) {
			LOGGER.error("Error validating while updating Order: {}", e.getMessage());
			orderView.showError(e.getMessage(), order);
		} catch (NoSuchElementException e) {
			LOGGER.error(ERROR_FINDING, e.getMessage());
			orderView.showErrorNotFound(e.getMessage(), order);
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
	public synchronized void searchOrder(String searchText, OrderSearchOptions searchOption) {
		LOGGER.info("Searching orders by search options");
		try {
			searchText = validationConfigurations.validateSearchString(searchText);
			if (searchOption == null) {
				throw new NullPointerException("Search option cannot be empty.");
			}

			List<CustomerOrder> orders;

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
			LOGGER.error("Error validating search text: {}", e.getMessage());
			orderView.showSearchError(e.getMessage(), searchText);
		}

	}

	/**
	 * Adds the.
	 *
	 * @param order the customer order
	 */
	private synchronized void add(CustomerOrder order) {
		if (order.getOrderId() != null) {
			throw new IllegalArgumentException("Unable to assign an order ID during order creation.");
		}
		if (order.getOrderStatus() != OrderStatus.PENDING) {
			throw new IllegalArgumentException("The order status should be initiated with 'pending' status.");
		}
		Worker worker = getValidWorker(order);
		checkForPendingOrders(worker.getOrders());
		order = orderRepository.save(order);
		orderView.orderAdded(order);
		LOGGER.info("New order created: {}", order);
	}

	/**
	 * Update.
	 *
	 * @param order the customer order
	 */
	private synchronized void update(CustomerOrder order) {
		validationConfigurations.validateStringNumber(order.getOrderId().toString());
		Worker worker = getValidWorker(order);
		CustomerOrder savedOrder = orderRepository.findById(order.getOrderId());
		if (!Objects.equals(worker.getWorkerId(), savedOrder.getWorker().getWorkerId())) {
			checkForPendingOrders(worker.getOrders());
		}
		order = orderRepository.save(order);

		orderView.orderModified(order);
		LOGGER.info("Order Updated: {}", order);
	}

	/**
	 * Searches for orders by date.
	 *
	 * @param searchText the search text
	 * @return the list of orders
	 */
	private synchronized List<CustomerOrder> searchByDate(String searchText) {
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
	private synchronized List<CustomerOrder> searchByCategory(String searchText) {
		OrderCategory category = validationConfigurations.validateEnum(searchText, OrderCategory.class);
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
	private synchronized List<CustomerOrder> searchByStatus(String searchText) {
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
	private synchronized List<CustomerOrder> searchByCustomerName(String searchText) {
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
	private synchronized List<CustomerOrder> searchByCustomerPhoneNumber(String searchText) {
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
	private synchronized List<CustomerOrder> searchByWorkerId(String searchText) {
		Long workerId = validationConfigurations.validateStringNumber(searchText);
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
	private synchronized CustomerOrder searchByOrderId(String searchText) {
		Long orderId = validationConfigurations.validateStringNumber(searchText);
		CustomerOrder order = orderRepository.findById(orderId);
		if (order == null) {
			throw new NoSuchElementException("No result found with ID: " + orderId);
		}
		return order;
	}

	/**
	 * Validates an order.
	 *
	 * @param order the order
	 */
	private void validateOrder(CustomerOrder order) {
		validationConfigurations.validateName(order.getCustomerName());
		validationConfigurations.validatePhoneNumber(order.getCustomerPhoneNumber());
		validationConfigurations.validateAddress(order.getCustomerAddress());
		validationConfigurations.validateStringDate(order.getAppointmentDate());
		validationConfigurations.validateDescription(order.getOrderDescription());
		validationConfigurations.validateCategory(order.getOrderCategory());
		validationConfigurations.validateStatus(order.getOrderStatus());
		Objects.requireNonNull(order.getWorker(), "The worker field cannot be empty.");
		validationConfigurations.validateStringNumber(order.getWorker().getWorkerId().toString());
	}

	/**
	 * Retrieves a valid worker for the order.
	 *
	 * @param order the order
	 * @return the valid worker
	 */
	private synchronized Worker getValidWorker(CustomerOrder order) {
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
