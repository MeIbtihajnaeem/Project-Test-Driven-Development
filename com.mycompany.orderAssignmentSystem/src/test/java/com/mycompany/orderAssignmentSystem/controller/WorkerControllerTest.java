package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.WorkerView;

public class WorkerControllerTest {

	@Mock
	private WorkerRepository workerRepository;

	@Mock
	private WorkerView workerView;

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
	public void testAllWorkerMethodWhenWorker() {
		List<Worker> worker = asList(new Worker());
		when(workerRepository.findAll()).thenReturn(worker);
		workerController.getAllWorkers();
		verify(workerView).showAllWorkers(worker);
	}

	@Test
	public void testAllWorkerMethodWhenEmptyList() {
		when(workerRepository.findAll()).thenReturn(Collections.emptyList());
		workerController.getAllWorkers();
		verify(workerView).showAllWorkers(Collections.emptyList());
	}

	@Test
	public void testAllWorkerMethodWhenNullList() {
		when(workerRepository.findAll()).thenReturn(null);
		workerController.getAllWorkers();
		verify(workerView).showAllWorkers(null);
	}

	@Test
	public void testCreateNewWorkerMethodWhenNullWorker() {
		try {
			workerController.createNewWorker(null);
			fail("Expected an NullPointerException to be thrown ");
		} catch (NullPointerException e) {
			assertEquals("Worker is null", e.getMessage());
		}
	}

	@Test
	public void testCreateNewWorkerWhenWorkerIdIsNotNull() {
		Worker worker = new Worker();
		worker.setWorkerId(1l);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Unable to assign a worker ID during worker creation.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));

	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsNull() {
		Worker worker = new Worker();
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsEmpty() {
		Worker worker = new Worker();
		worker.setWorkerName("");
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsShortStringLessThanTwoCharachters() {
		Worker worker = new Worker();
		worker.setWorkerName("a");
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The name must be at least 3 characters long. Please provide a valid name.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsShortStringEqualsToTwoCharachters() {
		Worker worker = new Worker();
		worker.setWorkerName("ab");
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The name must be at least 3 characters long. Please provide a valid name.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsLargeStringGreaterThanTwentyCharachters() {
		Worker worker = new Worker();
		worker.setWorkerName("Muhammad Ibtihaj Naeem");
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot exceed 20 characters. Please provide a shorter name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsLargeStringEqualsToTwentyCharachters() {
		Worker worker = new Worker();
		worker.setWorkerName("Muhammad Ibtihaj Nae");
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo("Muhammad Ibtihaj Nae");
		verify(spyWorker).setWorkerName("Muhammad Ibtihaj Nae");
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithSpecialCharacters() {
		Worker worker = new Worker();
		worker.setWorkerName("Muhammad@Ibtihaj");
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The name cannot contain special characters. Please remove any special characters from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithNumbers() {
		Worker worker = new Worker();
		worker.setWorkerName("Muhammad1Ibtihaj");
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain numbers. Please remove any number from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithTabs() {
		Worker worker = new Worker();
		worker.setWorkerName("Muhammad\tIbtihaj");
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain tabs. Please remove any tabs from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithOneLeadingWhiteSpace() {
		Worker worker = new Worker();
		worker.setWorkerName(" MuhammadIbtihaj");
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo("MuhammadIbtihaj");
		verify(spyWorker).setWorkerName("MuhammadIbtihaj");

	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithTwoLeadingWhiteSpace() {
		Worker worker = new Worker();
		worker.setWorkerName("  MuhammadIbtihaj");
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo("MuhammadIbtihaj");
		verify(spyWorker).setWorkerName("MuhammadIbtihaj");

	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithOneMiddleWhiteSpace() {
		Worker worker = new Worker();
		worker.setWorkerName("Muhammad Ibtihaj");
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo("Muhammad Ibtihaj");
		verify(spyWorker).setWorkerName("Muhammad Ibtihaj");
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithOneEndingWhiteSpace() {
		Worker worker = new Worker();
		worker.setWorkerName("Muhammad Ibtihaj ");
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo("Muhammad Ibtihaj");
		verify(spyWorker).setWorkerName("Muhammad Ibtihaj");
	}

}
