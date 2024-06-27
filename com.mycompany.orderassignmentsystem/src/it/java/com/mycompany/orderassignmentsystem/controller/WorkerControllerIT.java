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

public class WorkerControllerIT {

	@Mock
	private WorkerRepository workerRepository;

	@Mock
	private WorkerView workerView;

	@Mock
	private ValidationConfigurations validationConfig;

	@InjectMocks
	private WorkerController workerController;

	private AutoCloseable closeable;

	private EntityManagerFactory entityManagerFactory;

	/**
	 * This variable is responsible for starting the Docker container. If the test
	 * is run from Eclipse, it runs the Docker container using Testcontainers. If
	 * the test is run using a Maven command, it starts a real Docker container.
	 */
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
		validationConfig = new ExtendedValidationConfigurations();
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		workerController = new WorkerController(workerRepository, workerView, validationConfig);
	}

	@After
	public void releaseMocks() throws Exception {
		entityManagerFactory.close();
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
		verify(workerView).showSearchResultForWorker(asList(savedWorker));

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
		verify(workerView).showSearchResultForWorker(asList(savedWorker));

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
		verify(workerView).showSearchResultForWorker(asList(savedWorker));

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
		verify(workerView).showSearchResultForWorker(asList(savedWorker));

	}

}
