package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderSearchOptions;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderAssignmentSystem.view.OrderView;

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
	private EntityManager entityManager;
	private static final String PERSISTENCE_UNIT_NAME = "test_myPersistenceUnit";

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();
		orderRepository = new OrderDatabaseRepository(entityManager);
		workerRepository = new WorkerDatabaseRepository(entityManager);
		validationConfig = new ExtendedValidationConfigurations();
		orderController = new OrderController(orderRepository, orderView, workerRepository, validationConfig);
	}

	@After
	public void releaseMocks() throws Exception {
		entityManagerFactory.close();
		entityManager.close();
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
		Long orderId = orderRepository.findAll().get(0).getOrderId();
		order.setOrderId(orderId);
		verify(orderView).orderAdded(order);
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
		CustomerOrder savedOrder = orderRepository.save(order);
		savedOrder.setOrderStatus(OrderStatus.COMPLETED);
		orderController.createOrUpdateOrder(savedOrder, OperationType.UPDATE);
		verify(orderView).orderModified(savedOrder);
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
		orderView.showFetchedOrder(savedOrder);

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
		orderView.showSearchResultForOrder(asList(savedOrder));
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
		orderView.showSearchResultForOrder(asList(savedOrder));
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
		orderView.showSearchResultForOrder(asList(savedOrder));
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
		orderView.showSearchResultForOrder(asList(savedOrder));
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
		orderView.showSearchResultForOrder(asList(savedOrder));
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
		orderView.showSearchResultForOrder(asList(savedOrder));
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
		orderView.showSearchResultForOrder(asList(savedOrder));
	}
}