/*
 * Integration tests for the WorkerController class focused on race conditions.
 *
 * These tests verify the functionality of the WorkerController in concurrent
 * environments, ensuring that the application handles race conditions properly
 * when multiple threads are accessing and deleting worker data simultaneously.
 * The tests utilise Awaitility for handling asynchronous operations.
 *
 * The methods tested include:
 * - deleteWorker() for concurrent deletion of workers.
 * 
 * The setup and teardown methods handle the initialisation and cleanup of mock objects.
 *
 * The databaseConfig variable is responsible for starting the Docker container.
 * If the test is run from Eclipse, it runs the Docker container using Testcontainers.
 * If the test is run using a Maven command, it starts a Docker container without test containers.
 *
 * @see WorkerController
 * @see WorkerRepository
 * @see WorkerView
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
import com.mycompany.orderassignmentsystem.controller.WorkerController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.WorkerView;

/**
 * The Class WorkerControllerRaceConditionDeleteWorkerIT.
 */
public class WorkerControllerRaceConditionDeleteWorkerIT {

	/** The worker view. */
	@Mock
	private WorkerView workerView;

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The validation config. */
	private ValidationConfigurations validationConfig;

	/** The closeable. */
	private AutoCloseable closeable;

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

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
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		validationConfig = new ExtendedValidationConfigurations();

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
	 * Delete worker concurrent.
	 */
	@Test
	public void deleteWorkerConcurrent() {
		Worker worker = new Worker("John", "3401372678", OrderCategory.PLUMBER);

		Worker savedWorker = workerRepository.save(worker);
		List<Thread> threads = IntStream.range(0, 10).mapToObj(i -> new Thread(() -> {
			try {

				new WorkerController(workerRepository, workerView, validationConfig).deleteWorker(savedWorker);
			} catch (Exception e) {
				e.printStackTrace();
			}
		})).peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));

		assertThat(workerRepository.findAll()).isEmpty();

	}
}
