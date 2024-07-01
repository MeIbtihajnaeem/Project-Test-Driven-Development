/*
 * Integration tests for the OrderController class focused on race conditions.
 *
 * These tests verify the functionality of the OrderController in concurrent
 * environments, ensuring that the application handles race conditions properly
 * when multiple threads are accessing and deleting order data simultaneously.
 * The tests utilise Awaitility for handling asynchronous operations.
 *
 * The methods tested include:
 * - deleteOrder() for concurrent deletion of orders.
 * 
 * The setup and teardown methods handle the initialisation and cleanup of mock objects.
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

import com.mycompany.orderassignmentsystem.DatabaseConfig;
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

/**
 * The Class OrderControllerRaceConditionDeleteOrderIT.
 */
public class OrderControllerRaceConditionDeleteOrderIT {

	/** The order repository. */
	private OrderRepository orderRepository;

	/** The order view. */
	@Mock
	private OrderView orderView;

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The validation config. */
	private ValidationConfigurations validationConfig;

	/** The closeable. */
	private AutoCloseable closeable;

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/** The worker. */
	private Worker worker = new Worker("John", "3401372678", OrderCategory.PLUMBER);

	/** The saved order. */
	private CustomerOrder savedOrder = new CustomerOrder();

	/**
	 * This variable is responsible for starting the Docker container. If the test
	 * is run from Eclipse, it runs the Docker container using Testcontainers. If
	 * the test is run using a Maven command, it starts a Docker container without
	 * test containers
	 */
	private static DBConfig databaseConfig;

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
		worker = workerRepository.save(worker);
		savedOrder = orderRepository.save(new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker));
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
	 * Delete order concurrent.
	 */
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

	}
}
