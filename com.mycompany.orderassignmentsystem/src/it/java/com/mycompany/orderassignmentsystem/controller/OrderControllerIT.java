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

public class OrderControllerIT {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderView orderView;

	@Mock
	private WorkerRepository workerRepository;

	@Mock
	private ValidationConfigurations validationConfig;

	@InjectMocks
	private OrderController orderController;

	private AutoCloseable closeable;
	private EntityManagerFactory entityManagerFactory;

	private static DBConfig databaseConfig;

	@BeforeClass
	public static void setup() {
		databaseConfig = DatabaseConfig.getDatabaseConfig();
		databaseConfig.testAndStartDatabaseConnection();
	}

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		entityManagerFactory = databaseConfig.getEntityManagerFactory();
		orderRepository = new OrderDatabaseRepository(entityManagerFactory);
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		validationConfig = new ExtendedValidationConfigurations();
		orderController = new OrderController(orderRepository, orderView, workerRepository, validationConfig);
	}

	@After
	public void releaseMocks() throws Exception {
		entityManagerFactory.close();
		closeable.close();
	}

	@Test
	public void testAllOrders() {
		CustomerOrder order = new CustomerOrder();
		order.setAppointmentDate("12/12/2024");
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		order.setWorker(savedWorker);
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		CustomerOrder savedOrder = orderRepository.save(order);
		orderController.allOrders();
		verify(orderView).showAllOrder(asList(savedOrder));
	}

	@Test
	public void testAllWorkersOnOrderView() {
		Worker worker = new Worker();
		worker.setWorkerName("John");
		worker.setWorkerPhoneNumber("3401372678");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker newWorker = workerRepository.save(worker);
		orderController.allWorkers();
		verify(orderView).showAllWorkers(asList(newWorker));
	}

	@Test
	public void testNewOrder() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		CustomerOrder savedOrder = orderRepository.findAll().get(0);
		verify(orderView).orderAdded(savedOrder);
	}

	@Test
	public void testNewOrderWithWorkerHavePendingOrder() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order);
	}

	@Test
	public void testUpdateOrder() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		order = orderRepository.save(order);
		order.setOrderStatus(OrderStatus.COMPLETED);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		CustomerOrder savedOrder = orderRepository.findById(order.getOrderId());

		verify(orderView).orderModified(savedOrder);
	}

	@Test
	public void testUpdateOrderWorkerHavePendingOrder() {
		Worker worker1 = new Worker();
		worker1.setWorkerName("Jhon");
		worker1.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker1 = workerRepository.save(worker1);

		Worker worker2 = new Worker();
		worker2.setWorkerName("Bob");
		worker2.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker2 = workerRepository.save(worker2);

		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, savedWorker1);
		CustomerOrder order2 = new CustomerOrder("Alic", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, savedWorker2);

		orderRepository.save(order1);
		CustomerOrder savedOrder2 = orderRepository.save(order2);
		savedOrder2.setWorker(savedWorker1);
		orderController.createOrUpdateOrder(savedOrder2, OperationType.UPDATE);
		verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", savedOrder2);
	}

	@Test
	public void testFetchOrderById() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		CustomerOrder savedOrder = orderRepository.save(order);
		orderController.fetchOrderById(savedOrder);
		verify(orderView).showFetchedOrder(savedOrder);

	}

	@Test
	public void testDeleteOrder() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		CustomerOrder savedOrder = orderRepository.save(order);
		orderController.deleteOrder(savedOrder);
		verify(orderView).orderRemoved(savedOrder);
	}

	@Test
	public void testSearchOrderWithOrderId() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		orderController.searchOrder(savedOrder.getOrderId().toString(), OrderSearchOptions.ORDER_ID);
		verify(orderView).showSearchResultForOrder(asList(savedOrder));

	}

	@Test
	public void testSearchOrderWithWorkerId() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		orderController.searchOrder(savedWorker.getWorkerId().toString(), OrderSearchOptions.WORKER_ID);
		verify(orderView).showSearchResultForOrder(asList(savedOrder));

	}

	@Test
	public void testSearchOrderWithCustomerPhoneNumber() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		String searchText = "3401372678";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		verify(orderView).showSearchResultForOrder(asList(savedOrder));

	}

	@Test
	public void testSearchOrderWithAppointmentDate() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		String searchText = "12-12-2024";
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		verify(orderView).showSearchResultForOrder(asList(savedOrder));

	}

	@Test
	public void testSearchOrderWithOrderStatus() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		String searchText = "PENDING";
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		verify(orderView).showSearchResultForOrder(asList(savedOrder));

	}

	@Test
	public void testSearchOrderWithOrderCategory() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		String searchText = "PLUMBER";
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		verify(orderView).showSearchResultForOrder(asList(savedOrder));

	}

	@Test
	public void testSearchOrderWithCustomerName() {
		Worker worker = new Worker();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		String searchText = "Jhon";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		verify(orderView).showSearchResultForOrder(asList(savedOrder));

	}
}
