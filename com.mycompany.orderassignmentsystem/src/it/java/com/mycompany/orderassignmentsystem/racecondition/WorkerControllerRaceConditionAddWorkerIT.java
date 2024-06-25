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
import com.mycompany.orderassignmentsystem.controller.WorkerController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.WorkerView;

public class WorkerControllerRaceConditionAddWorkerIT {

	@Mock
	private WorkerView workerView;

	private WorkerRepository workerRepository;

	private ValidationConfigurations validationConfig;

	private AutoCloseable closeable;

	private EntityManagerFactory entityManagerFactory;
	private Worker worker = new Worker();

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
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		validationConfig = new ExtendedValidationConfigurations();

		worker.setWorkerName("Alic");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		worker.setWorkerPhoneNumber("3401372678");
	}

	@After
	public void releaseMocks() throws Exception {
		entityManagerFactory.close();
		closeable.close();
	}

	@Test
	public void testCreateWorkerConcurrent() {

		List<Thread> threads = IntStream.range(0, 10).mapToObj(i -> new Thread(() -> {
			try {
				new WorkerController(workerRepository, workerView, validationConfig).createOrUpdateWorker(worker,
						OperationType.ADD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		})).peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		worker = workerRepository.findAll().get(0);
		assertThat(workerRepository.findAll()).containsExactly(worker);
	}

}
