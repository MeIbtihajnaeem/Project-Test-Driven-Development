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

public class WorkerControllerRaceConditionTest {

	@Mock
	private WorkerRepository workerRepository;

	@Mock
	private WorkerView workerView;

	@Mock
	private ValidationConfigurations validationConfigurations;

	@InjectMocks
	private WorkerController workerController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testNewWorkerConcurrent() {
		List<Worker> workers = new ArrayList<>();
		String workerName = "Alic";
		String workerPhoneNumber = "3401372678";
		OrderCategory category = OrderCategory.PLUMBER;
		Worker worker = new Worker(workerName, workerPhoneNumber, category);
		when(validationConfigurations.validateName(anyString())).thenReturn(workerName);
		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(workerPhoneNumber);
		when(validationConfigurations.validateCategory(category)).thenReturn(category);
		when(workerRepository.findByPhoneNumber(anyString()))
				.thenAnswer(invocation -> workers.stream().findFirst().orElse(null));

		doAnswer(invocation -> {
			workers.add(worker);
			return worker;
		}).when(workerRepository).save(any(Worker.class));
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD)))
				.peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		assertThat(workers).containsExactly(worker);
	}

	@Test
	public void testDeleteWorkerConcurrent() {
		List<Worker> workers = new ArrayList<>();
		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		when(validationConfigurations.validateStringNumber(anyString())).thenReturn(workerId);
		when(workerRepository.findById(anyLong())).thenAnswer(invocation -> workers.stream().findFirst().orElse(null));

		doAnswer(invocation -> {
			workers.remove(worker);
			return null;
		}).when(workerRepository).delete(any(Worker.class));

		workers.add(worker);
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> workerController.deleteWorker(worker))).peek(t -> t.start())
				.collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		assertThat(workers).isEmpty();
		;
	}
}
