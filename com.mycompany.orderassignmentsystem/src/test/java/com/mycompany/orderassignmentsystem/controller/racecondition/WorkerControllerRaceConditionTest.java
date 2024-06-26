/*
 * Unit tests for the WorkerController class focused on race conditions.
 *
 * These tests verify the functionality of the WorkerController in concurrent
 * environments, ensuring that the application handles race conditions properly
 * when multiple threads are accessing and modifying order data simultaneously.
 * The tests utilise Mockito for mocking dependencies and Awaitility for 
 * handling asynchronous operations.
 *
 * The methods tested include:
 * - createOrUpdateWorker() for concurrent creation of orders.
 * - deleteWorker() for concurrent deletion of orders.
 *
 * Each test follows a structured approach with three main phases:
 * 1. Setup: Created environment for the test.
 * 2. Mocks: Configuring the mock objects (Added separate comment just for better readability).
 * 3. Exercise: Calling an instance method.
 * 4. Verify: Verify that the outcome matches the expected behaviour.
 *
 * The setup and teardown methods handle the initialisation and cleanup of mock objects.
 *
 * @see WorkerController
 * @see WorkerRepository
 * @see WorkerView
 * @see ValidationConfigurations
 */
package com.mycompany.orderassignmentsystem.controller.racecondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderassignmentsystem.controller.WorkerController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.view.WorkerView;

/**
 * The Class WorkerControllerRaceConditionTest.
 */
public class WorkerControllerRaceConditionTest {

	/** The worker repository. */
	@Mock
	private WorkerRepository workerRepository;

	/** The worker view. */
	@Mock
	private WorkerView workerView;

	/** The validation configurations. */
	@Mock
	private ValidationConfigurations validationConfigurations;

	/** The worker controller. */
	@InjectMocks
	private WorkerController workerController;

	/** The closeable. */
	private AutoCloseable closeable;

	/** The worker id. */
	private long WORKER_ID = 1l;

	/** The worker name. */
	private String WORKER_NAME = "Muhammad Ibtihaj";

	/** The worker phone. */
	private String WORKER_PHONE = "3401372678";

	/** The worker category. */
	private OrderCategory WORKER_CATEGORY = OrderCategory.PLUMBER;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	/**
	 * Release mocks.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	/**
	 * Test new worker concurrent.
	 */
	@Test
	public void testNewWorkerConcurrent() {
		// Setup
		List<Worker> workers = new ArrayList<>();

		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mocks
		when(workerRepository.findByPhoneNumber(anyString()))
				.thenAnswer(invocation -> workers.stream().findFirst().orElse(null));

		doAnswer(invocation -> {
			workers.add(worker);
			return worker;
		}).when(workerRepository).save(any(Worker.class));

		// Exercise
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD)))
				.peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));

		// Verify
		assertThat(workers).containsExactly(worker);
	}

	/**
	 * Test delete worker concurrent.
	 */
	@Test
	public void testDeleteWorkerConcurrent() {
		// Setup
		List<Worker> workers = new ArrayList<>();

		Worker worker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mocks
		when(workerRepository.findById(anyLong())).thenAnswer(invocation -> workers.stream().findFirst().orElse(null));

		doAnswer(invocation -> {
			workers.remove(worker);
			return null;
		}).when(workerRepository).delete(any(Worker.class));

		// Exercise
		workers.add(worker);
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> workerController.deleteWorker(worker))).peek(t -> t.start())
				.collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));

		// Verify
		assertThat(workers).isEmpty();

	}
}
