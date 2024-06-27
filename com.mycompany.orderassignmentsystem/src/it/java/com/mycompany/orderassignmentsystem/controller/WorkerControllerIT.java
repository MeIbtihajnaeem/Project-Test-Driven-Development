/*
 * Integration tests for the WorkerController class.
 *
 * These tests verify the integration between the WorkerController and its 
 * dependencies, including WorkerRepository, WorkerView, 
 * and ValidationConfigurations. The tests cover various scenarios such 
 * as creating, updating, fetching, deleting, and searching workers. The 
 * tests utilise Mockito for mocking dependencies and ensure the 
 * correctness of the OrderController implementation.
 *
 * Key features tested include:
 * - Retrieving all workers.
 * - Creating or updating workers with various conditions.
 * - Searching for workers based on different criteria.
 *
 * The setup and teardown methods handle the initialisation and cleanup 
 * of resources, including database connections and mock objects.
 *
 * The databaseConfig variable is responsible for starting the Docker container.
 * If the test is run from Eclipse, it runs the Docker container using Testcontainers.
 * If the test is run using a Maven command, it starts a real Docker container.
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
import com.mycompany.orderassignmentsystem.enumerations.WorkerSearchOption;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.WorkerView;

/**
 * The Class WorkerControllerIT.
 */
public class WorkerControllerIT {

	/** The worker repository. */
	@Mock
	private WorkerRepository workerRepository;

	/** The worker view. */
	@Mock
	private WorkerView workerView;

	/** The validation config. */
	@Mock
	private ValidationConfigurations validationConfig;

	/** The worker controller. */
	@InjectMocks
	private WorkerController workerController;

	/** The closeable. */
	private AutoCloseable closeable;

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/**
	 * This variable is responsible for starting the Docker container. If the test
	 * is run from Eclipse, it runs the Docker container using Testcontainers. If
	 * the test is run using a Maven command, it starts a real Docker container.
	 */
	private static DBConfig databaseConfig;

	/** The worker name 1. */
	private String WORKER_NAME = "Bob";

	/** The worker phone 1. */
	private String WORKER_PHONE = "3401372678";

	/** The worker category 1. */
	private OrderCategory WORKER_CATEGORY = OrderCategory.PLUMBER;

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
		validationConfig = new ExtendedValidationConfigurations();
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		workerController = new WorkerController(workerRepository, workerView, validationConfig);
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
	 * Test all workers.
	 */
	@Test
	public void testAllWorkers() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		workerController.getAllWorkers();

		// Verify
		verify(workerView).showAllWorkers(asList(worker));
	}

	/**
	 * Test new worker.
	 */
	@Test
	public void testNewWorker() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		worker = workerRepository.findAll().get(0);
		verify(workerView).workerAdded(worker);
	}

	/**
	 * Test update worker.
	 */
	@Test
	public void testUpdateWorker() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		Worker updatedWorker = new Worker(worker.getWorkerId(), "John", "3401372678", OrderCategory.CARPAINTER);
		workerController.createOrUpdateWorker(updatedWorker, OperationType.UPDATE);

		// Verify
		verify(workerView).workerModified(updatedWorker);
	}

	/**
	 * Test fetch worker.
	 */
	@Test
	public void testFetchWorker() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		workerController.fetchWorkerById(worker);

		// Verify
		verify(workerView).showFetchedWorker(worker);
	}

	/**
	 * Test delete worker.
	 */
	@Test
	public void testDeleteWorker() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		workerController.deleteWorker(worker);

		// Verify
		verify(workerView).workerRemoved(worker);

	}

	/**
	 * Test search worker with worker id.
	 */
	@Test
	public void testSearchWorkerWithWorkerId() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		String searchText = worker.getWorkerId().toString();
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);

		// Verify
		verify(workerView).showSearchResultForWorker(asList(worker));

	}

	/**
	 * Test search worker with worker phone.
	 */
	@Test
	public void testSearchWorkerWithWorkerPhone() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		String searchText = WORKER_PHONE;
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);

		// Verify
		verify(workerView).showSearchResultForWorker(asList(worker));

	}

	/**
	 * Test search worker with worker name.
	 */
	@Test
	public void testSearchWorkerWithWorkerName() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		String searchText = WORKER_NAME;
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);

		// Verify
		verify(workerView).showSearchResultForWorker(asList(worker));

	}

	/**
	 * Test search worker with worker category.
	 */
	@Test
	public void testSearchWorkerWithWorkerCategory() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker = workerRepository.save(worker);

		// Exercise
		String searchText = WORKER_CATEGORY.name();
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);

		// Verify
		verify(workerView).showSearchResultForWorker(asList(worker));

	}

}
