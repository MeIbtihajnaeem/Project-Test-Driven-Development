package com.mycompany.orderAssignmentSystem.raceCondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderAssignmentSystem.controller.OrderController;
import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderAssignmentSystem.view.OrderView;

public class OrderControllerRaceConditionAddOrderIT {

	private OrderRepository orderRepository;

	@Mock
	private OrderView orderView;

	private WorkerRepository workerRepository;

	private ValidationConfigurations validationConfig;

	private AutoCloseable closeable;

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private static final String PERSISTENCE_UNIT_NAME = "test_myPersistenceUnit";
	private Worker worker = new Worker();
	private CustomerOrder order = new CustomerOrder();

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();

		orderRepository = new OrderDatabaseRepository(entityManager);
		workerRepository = new WorkerDatabaseRepository(entityManager);
		validationConfig = new ExtendedValidationConfigurations();
//		orderController = new OrderController(orderRepository, orderView, workerRepository, validationConfig);
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);

		order.setCustomerName("Jhon");
		order.setCustomerAddress("Piazza Luigi Dalla");
		order.setCustomerPhoneNumber("3401372678");
		order.setAppointmentDate("12-12-2024");
		order.setOrderDescription("No description");
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(worker);

	}

	@After
	public void releaseMocks() throws Exception {
		entityManager.clear();

		entityManagerFactory.close();
		entityManager.close();

		closeable.close();
	}

	@Test
	public void testNewOrderConcurrent() {

		List<Thread> threads = IntStream.range(0, 3).mapToObj(i -> new Thread(() -> {
			try {
				new OrderController(orderRepository, orderView, workerRepository, validationConfig)
						.createOrUpdateOrder(order, OperationType.ADD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		})).peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));

		order = orderRepository.findAll().get(0);
		assertThat(orderRepository.findAll()).containsExactly(order);
	}

}
