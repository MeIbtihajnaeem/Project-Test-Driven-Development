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
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderAssignmentSystem.view.WorkerView;

public class WorkerControllerIT {

	@Mock
	private WorkerRepository workerRepository;

	@Mock
	private WorkerView workerView;

	@Mock
	private ValidationConfigurations validationConfig;

	@InjectMocks
	private WorkerController workerController;

	private OrderRepository orderRepository;

	private AutoCloseable closeable;

//	private OrderRepository orderRepository;

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private static final String PERSISTENCE_UNIT_NAME = "test_myPersistenceUnit";

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

		entityManager = entityManagerFactory.createEntityManager();
		validationConfig = new ExtendedValidationConfigurations();
		workerRepository = new WorkerDatabaseRepository(entityManager);
		orderRepository = new OrderDatabaseRepository(entityManager);
		workerController = new WorkerController(workerRepository, workerView, validationConfig);
	}

	@After
	public void releaseMocks() throws Exception {
		entityManagerFactory.close();
		entityManager.close();
		closeable.close();
	}

	@Test
	public void testAllWorkers() {
		Worker worker = new Worker();
		worker.setWorkerName("John");
		worker.setWorkerPhoneNumber("3401372678");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker newWorker = workerRepository.save(worker);
		workerController.getAllWorkers();
		verify(workerView).showAllWorkers(asList(newWorker));
	}

	@Test
	public void testNewWorker() {
		Worker worker = new Worker();
		worker.setWorkerName("John");
		worker.setWorkerPhoneNumber("3401372678");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		Long workerId = workerRepository.findAll().get(0).getWorkerId();
		worker.setWorkerId(workerId);
		verify(workerView).workerAdded(worker);
	}

	@Test
	public void testUpdateWorker() {
		Worker worker = new Worker();
		worker.setWorkerName("John");
		worker.setWorkerPhoneNumber("3401372678");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker oldWorker = workerRepository.save(worker);
		Worker updatedWorker = new Worker(oldWorker.getWorkerId(), "John", "3401372678", OrderCategory.CARPAINTER);
		workerController.createOrUpdateWorker(updatedWorker, OperationType.UPDATE);
		verify(workerView).workerModified(updatedWorker);
	}

	@Test
	public void testFetchWorker() {
		Worker worker = new Worker();
		worker.setWorkerName("John");
		worker.setWorkerPhoneNumber("3401372678");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		workerController.fetchWorkerById(savedWorker);

		verify(workerView).showFetchedWorker(savedWorker);
	}

	@Test
	public void testDeleteWorker() {
		Worker worker = new Worker();
		worker.setWorkerName("John");
		worker.setWorkerPhoneNumber("3401372678");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		Worker savedWorker = workerRepository.save(worker);
		workerController.deleteWorker(savedWorker);
		verify(workerView).workerRemoved(savedWorker);

	}

	@Test
	public void testFetchOrdersByWorkerId() {
		Worker worker = new Worker();
		worker.setWorkerName("John");
		worker.setWorkerPhoneNumber("3401372678");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		CustomerOrder order = new CustomerOrder();
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		CustomerOrder savedOrder = orderRepository.save(order);
		worker.setOrders(asList(savedOrder));
		Worker savedWorker = workerRepository.save(worker);
		workerController.fetchOrdersByWorkerId(savedWorker);
		verify(workerView).showOrderByWorkerId(savedWorker.getOrders());
	}

	@Test
	public void testSearchWorkerWithWorkerId() {
		String workerName = "John";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		Worker worker = new Worker();
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		Worker savedWorker = workerRepository.save(worker);
		Long workerId = savedWorker.getWorkerId();
		workerController.searchWorker(workerId.toString(), WorkerSearchOption.WORKER_ID);
		workerView.showSearchResultForWorker(asList(savedWorker));
	}

	@Test
	public void testSearchWorkerWithWorkerPhone() {
		String workerName = "John";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		Worker worker = new Worker();
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		Worker savedWorker = workerRepository.save(worker);
		workerController.searchWorker(workerPhoneNumber, WorkerSearchOption.WORKER_PHONE);
		workerView.showSearchResultForWorker(asList(savedWorker));
	}

	@Test
	public void testSearchWorkerWithWorkerName() {
		String workerName = "John";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		Worker worker = new Worker();
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		Worker savedWorker = workerRepository.save(worker);
		workerController.searchWorker(workerName, WorkerSearchOption.WORKER_NAME);
		workerView.showSearchResultForWorker(asList(savedWorker));
	}

	@Test
	public void testSearchWorkerWithWorkerCategory() {
		String workerName = "John";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		Worker worker = new Worker();
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		Worker savedWorker = workerRepository.save(worker);
		workerController.searchWorker(plumber.toString(), WorkerSearchOption.WORKER_CATEGORY);
		workerView.showSearchResultForWorker(asList(savedWorker));
	}

}