//package com.mycompany.orderassignmentsystem.raceCondition;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.mycompany.orderAssignmentSystem.controller.WorkerController;
//import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
//import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
//import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
//import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
//import com.mycompany.orderAssignmentSystem.model.Worker;
//import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
//import com.mycompany.orderAssignmentSystem.repository.postgres.WorkerDatabaseRepository;
//import com.mycompany.orderAssignmentSystem.view.WorkerView;
//
//public class WorkerControllerRaceConditionAddWorkerIT {
//
//	@Mock
//	private WorkerView workerView;
//
//	private WorkerRepository workerRepository;
//
//	private ValidationConfigurations validationConfig;
//
//	private AutoCloseable closeable;
//
//	private EntityManagerFactory entityManagerFactory;
////	private EntityManager entityManager;
//	private static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";
//	private Worker worker = new Worker();
//
//	@Before
//	public void setUp() {
//		closeable = MockitoAnnotations.openMocks(this);
//
//		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
////		entityManager = entityManagerFactory.createEntityManager();
//
//		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
//		validationConfig = new ExtendedValidationConfigurations();
//
//		worker.setWorkerName("Alic");
//		worker.setWorkerCategory(OrderCategory.PLUMBER);
//		worker.setWorkerPhoneNumber("3401372678");
//	}
//
//	@After
//	public void releaseMocks() throws Exception {
////		entityManager.clear();
//		entityManagerFactory.close();
////		entityManager.close();
//		closeable.close();
//	}
//
//	@Test
//	public void testCreateWorkerConcurrent() {
//
//		List<Thread> threads = IntStream.range(0, 3).mapToObj(i -> new Thread(() -> {
//			try {
//				new WorkerController(workerRepository, workerView, validationConfig).createOrUpdateWorker(worker,
//						OperationType.ADD);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		})).peek(t -> t.start()).collect(Collectors.toList());
//		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
////		new WorkerController(workerRepository, workerView, validationConfig).createOrUpdateWorker(worker,
////				OperationType.ADD);
//		worker = workerRepository.findAll().get(0);
//		assertThat(workerRepository.findAll()).containsExactly(worker);
//	}
//
//}
