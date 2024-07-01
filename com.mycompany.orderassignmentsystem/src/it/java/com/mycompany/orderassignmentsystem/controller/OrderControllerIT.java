/*
 * Integration tests for the OrderController class.
 *
 * These tests verify the integration between the OrderController and its 
 * dependencies, including OrderRepository, OrderView, 
 * and ValidationConfigurations. The tests cover various scenarios such 
 * as creating, updating, fetching, deleting, and searching orders. The 
 * tests utilise Mockito for mocking dependencies and ensure the 
 * correctness of the OrderController implementation.
 *
 * Key features tested include:
 * - Retrieving all orders and workers.
 * - Creating or updating orders with various conditions.
 * - Searching for orders based on different criteria.
 *
 * The setup and teardown methods handle the initialisation and cleanup 
 * of resources, including database connections and mock objects.
 *
 * The databaseConfig variable is responsible for starting the Docker container.
 * If the test is run from Eclipse, it runs the Docker container using Testcontainers.
 * If the test is run using a Maven command, it starts a Docker container without test containers.
 *
 * @see OrderController
 * @see OrderRepository
 * @see WorkerRepository
 * @see OrderView
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 * @see DatabaseConfig
 * @see DBConfig
 * @see MavenContainerConfig
 * @see TestContainerConfig
 */

package com.mycompany.orderassignmentsystem.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderassignmentsystem.DatabaseConfig;
import com.mycompany.orderassignmentsystem.configurations.DBConfig;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderSearchOptions;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.OrderView;

/**
 * The Class OrderControllerIT.
 */
public class OrderControllerIT {

	/** The order repository. */
	@Mock
	private OrderRepository orderRepository;

	/** The order view. */
	@Mock
	private OrderView orderView;

	/** The worker repository. */
	@Mock
	private WorkerRepository workerRepository;

	/**
	 * The validation config
	 */
	@Mock
	private ValidationConfigurations validationConfig;

	/** The order controller. */
	@InjectMocks
	private OrderController orderController;

	/** The closeable. */
	private AutoCloseable closeable;

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/**
	 * This variable is responsible for starting the Docker container. If the test
	 * is run from Eclipse, it runs the Docker container using Testcontainers. If
	 * the test is run using a Maven command, it starts a Docker container without
	 * test containers.
	 */
	private static DBConfig databaseConfig;

	/** The worker name 1. */
	private String WORKER_NAME_1 = "Bob";

	/** The worker phone 1. */
	private String WORKER_PHONE_1 = "3401372678";

	/** The worker category 1. */
	private OrderCategory WORKER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The customer name 1. */
	private String CUSTOMER_NAME_1 = "Jhon";

	/** The customer phone 1. */
	private String CUSTOMER_PHONE_1 = "3401372671";

	/** The customer address 1. */
	private String CUSTOMER_ADDRESS_1 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 1. */
	private String ORDER_APPOINTMENT_DATE_1 = "12-12-2024";

	/** The order description 1. */
	private String ORDER_DESCRIPTION_1 = "Please be on time";

	/** The order category 1. */
	private OrderCategory ORDER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The order status 1. */
	private OrderStatus ORDER_STATUS_1 = OrderStatus.PENDING;

	/** The customer name 2. */
	private String CUSTOMER_NAME_2 = "Alic";

	/** The customer phone 2. */
	private String CUSTOMER_PHONE_2 = "3401372672";

	/** The customer address 2. */
	private String CUSTOMER_ADDRESS_2 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 2. */
	private String ORDER_APPOINTMENT_DATE_2 = "12-12-2024";

	/** The order description 2. */
	private String ORDER_DESCRIPTION_2 = "Please bring tape";

	/** The order category 2. */
	private OrderCategory ORDER_CATEGORY_2 = OrderCategory.PLUMBER;

	/** The order status 2. */
	private OrderStatus ORDER_STATUS_2 = OrderStatus.PENDING;

	/** The worker. */
	private Worker worker;

	/**
	 * Setup.
	 */
	@BeforeClass
	public static void setup() {
		databaseConfig = DatabaseConfig.getDatabaseConfig();
		databaseConfig.testAndStartDatabaseConnection();
	}

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		entityManagerFactory = databaseConfig.getEntityManagerFactory();
		orderRepository = new OrderDatabaseRepository(entityManagerFactory);
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		validationConfig = new ExtendedValidationConfigurations();
		orderController = new OrderController(orderRepository, orderView, workerRepository, validationConfig);

		worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		worker = workerRepository.save(worker);

	}

	/**
	 * Release mocks.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void releaseMocks() throws Exception {
		entityManagerFactory.close();
		closeable.close();
	}

	/**
	 * Test all orders.
	 */
	@Test
	public void testAllOrders() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		CustomerOrder savedOrder = orderRepository.save(order);

		// Exercise
		orderController.allOrders();

		// Verify
		verify(orderView).showAllOrder(asList(savedOrder));
	}

	/**
	 * Test all workers on order view.
	 */
	@Test
	public void testAllWorkersOnOrderView() {
		// Setup in @Before method

		// Exercise
		orderController.allWorkers();

		// Verify
		verify(orderView).showAllWorkers(asList(worker));
	}

	/**
	 * Test new order.
	 */
	@Test
	public void testNewOrder() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		order = orderRepository.findAll().get(0);

		// Verify
		verify(orderView).orderAdded(order);
	}

	/**
	 * Test new order with worker have pending order.
	 */
	@Test
	public void testNewOrderWithWorkerHavePendingOrder() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order);
	}

	/**
	 * Test update order.
	 */
	@Test
	public void testUpdateOrder() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);
		order.setOrderStatus(OrderStatus.COMPLETED);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		order = orderRepository.findById(order.getOrderId());

		// Verify
		verify(orderView).orderModified(order);
	}

	/**
	 * Test update order with worker have pending order.
	 */
	@Test
	public void testUpdateOrderWithWorkerHavePendingOrder() {
		// Setup
		Worker workerForOrder1 = new Worker("Jhon", "3401372672", OrderCategory.PLUMBER);
		workerForOrder1 = workerRepository.save(workerForOrder1);

		Worker workerForOrder2 = new Worker("Bob", "3401372673", OrderCategory.PLUMBER);
		workerForOrder2 = workerRepository.save(workerForOrder2);

		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, workerForOrder1);
		CustomerOrder order2 = new CustomerOrder(CUSTOMER_NAME_2, CUSTOMER_ADDRESS_2, CUSTOMER_PHONE_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, workerForOrder2);

		// Saving each order with its corresponding worker
		orderRepository.save(order1);
		order2 = orderRepository.save(order2);

		// Exercise
		order2.setWorker(workerForOrder1);
		orderController.createOrUpdateOrder(order2, OperationType.UPDATE);

		// Verify
		verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order2);
	}

	/**
	 * Test fetch order by id.
	 */
	@Test
	public void testFetchOrderById() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		orderController.fetchOrderById(order);

		// Verify
		verify(orderView).showFetchedOrder(order);

	}

	/**
	 * Test delete order.
	 */
	@Test
	public void testDeleteOrder() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		orderController.deleteOrder(order);

		// Verify
		verify(orderView).orderRemoved(order);
	}

	/**
	 * Test search order with order id.
	 */
	@Test
	public void testSearchOrderWithOrderId() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		orderController.searchOrder(order.getOrderId().toString(), OrderSearchOptions.ORDER_ID);

		// Verify
		verify(orderView).showSearchResultForOrder(asList(order));

	}

	/**
	 * Test search order with worker id.
	 */
	@Test
	public void testSearchOrderWithWorkerId() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		String searchText = order.getWorker().getWorkerId().toString();
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		verify(orderView).showSearchResultForOrder(asList(order));

	}

	/**
	 * Test search order with customer phone number.
	 */
	@Test
	public void testSearchOrderWithCustomerPhoneNumber() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		String searchText = CUSTOMER_PHONE_1;
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);

		// Verify
		verify(orderView).showSearchResultForOrder(asList(order));

	}

	/**
	 * Test search order with appointment date.
	 */
	@Test
	public void testSearchOrderWithAppointmentDate() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		String searchText = ORDER_APPOINTMENT_DATE_1;
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);

		// Verify
		verify(orderView).showSearchResultForOrder(asList(order));

	}

	/**
	 * Test search order with order status.
	 */
	@Test
	public void testSearchOrderWithOrderStatus() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		String searchText = ORDER_STATUS_1.name();
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);

		// Verify
		verify(orderView).showSearchResultForOrder(asList(order));

	}

	/**
	 * Test search order with order category.
	 */
	@Test
	public void testSearchOrderWithOrderCategory() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		String searchText = ORDER_CATEGORY_1.name();
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);

		// Verify
		verify(orderView).showSearchResultForOrder(asList(order));

	}

	/**
	 * Test search order with customer name.
	 */
	@Test
	public void testSearchOrderWithCustomerName() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		String searchText = CUSTOMER_NAME_1;
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);

		// Verify
		verify(orderView).showSearchResultForOrder(asList(order));

	}
}
