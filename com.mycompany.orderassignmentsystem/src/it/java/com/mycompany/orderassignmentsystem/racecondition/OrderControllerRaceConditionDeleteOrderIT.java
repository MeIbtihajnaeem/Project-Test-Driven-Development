package com.mycompany.orderassignmentsystem.racecondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderassignmentsystem.Config;
import com.mycompany.orderassignmentsystem.configurations.DBConfig;
import com.mycompany.orderassignmentsystem.controller.OrderController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.OrderView;

public class OrderControllerRaceConditionDeleteOrderIT {

	private OrderRepository orderRepository;

	@Mock
	private OrderView orderView;

	private WorkerRepository workerRepository;

	private ValidationConfigurations validationConfig;

	private AutoCloseable closeable;

	private EntityManagerFactory entityManagerFactory;
	private Worker worker = new Worker();
	private CustomerOrder savedOrder = new CustomerOrder();
	private static DBConfig databaseConfig;

	@BeforeClass
	public static void setup() {
		databaseConfig = Config.getDatabaseConfig();

		databaseConfig.testAndStartDatabaseConnection();
	}

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		entityManagerFactory = databaseConfig.getEntityManagerFactory();
		orderRepository = new OrderDatabaseRepository(entityManagerFactory);
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		validationConfig = new ExtendedValidationConfigurations();
		worker.setWorkerName("Jhon");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);
		CustomerOrder order = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		savedOrder = orderRepository.save(order);
	}

	@After
	public void releaseMocks() throws Exception {
		entityManagerFactory.close();
		closeable.close();
	}

	@Test
	public void deleteOrderConcurrent() {

		List<Thread> threads = IntStream.range(0, 10).mapToObj(i -> new Thread(() -> {
			try {
				new OrderController(orderRepository, orderView, workerRepository, validationConfig)
						.deleteOrder(savedOrder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		})).peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));

		assertThat(orderRepository.findAll()).isEmpty();
		;
	}
}
