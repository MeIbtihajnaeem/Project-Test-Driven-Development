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

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
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

	// Tests for show All workers
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

	// Tests for create worker method

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
		long workerId = 1l;
		worker.setWorkerId(workerId);
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
		String workerName = "";
		worker.setWorkerName(workerName);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsShortStringLessThanTwoCharachters() {
		Worker worker = new Worker();
		String workerName = "a";
		worker.setWorkerName(workerName);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The name must be at least 3 characters long. Please provide a valid name.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsShortStringEqualsToTwoCharachters() {
		Worker worker = new Worker();
		String workerName = "ab";
		worker.setWorkerName(workerName);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The name must be at least 3 characters long. Please provide a valid name.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsLargeStringGreaterThanTwentyCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj Naeem";
		worker.setWorkerName(workerName);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot exceed 20 characters. Please provide a shorter name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameIsLargeStringEqualsToTwentyCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj Nae";
		worker.setWorkerName(workerName);
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(workerName);
		verify(spyWorker).setWorkerName(workerName);
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithSpecialCharacters() {
		Worker worker = new Worker();
		String workerName = "Muhammad@Ibtihaj";
		worker.setWorkerName(workerName);
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
		String workerName = "Muhammad1Ibtihaj";
		worker.setWorkerName(workerName);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain numbers. Please remove any number from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithTabs() {
		Worker worker = new Worker();
		String workerName = "Muhammad\tIbtihaj";
		worker.setWorkerName(workerName);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain tabs. Please remove any tabs from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithOneLeadingWhiteSpace() {
		Worker worker = new Worker();
		String actualWorkerName = " MuhammadIbtihaj";
		String expectedWorkerName = "MuhammadIbtihaj";
		worker.setWorkerName(actualWorkerName);
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(expectedWorkerName);
		verify(spyWorker).setWorkerName(expectedWorkerName);

	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithTwoLeadingWhiteSpace() {
		Worker worker = new Worker();
		String actualWorkerName = "  MuhammadIbtihaj";
		String expectedWorkerName = "MuhammadIbtihaj";

		worker.setWorkerName(actualWorkerName);
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(expectedWorkerName);
		verify(spyWorker).setWorkerName(expectedWorkerName);

	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithOneMiddleWhiteSpace() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		worker.setWorkerName(workerName);
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(workerName);
		verify(spyWorker).setWorkerName(workerName);
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerNameWithOneEndingWhiteSpace() {
		Worker worker = new Worker();
		String actualWorkerName = "Muhammad Ibtihaj ";
		String expectedWorkerName = "Muhammad Ibtihaj";

		worker.setWorkerName(actualWorkerName);
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(expectedWorkerName);
		verify(spyWorker).setWorkerName(expectedWorkerName);
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberIsNull() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		worker.setWorkerName(workerName);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberIsEmptyString() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberIsShortStringLessThanTenCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "000000";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberIsLongStringGreaterThanTenCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "00000000000";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberIsLongStringEqualsTenCharachters() {
		Worker worker = new Worker();

		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerPhoneNumber()).isEqualTo(workerPhoneNumber);
		verify(spyWorker).setWorkerPhoneNumber(workerPhoneNumber);
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberWithSpecialCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372@78";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberWithAlphabetCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372a78";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberWithMiddleWhiteSpaceCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372 78";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberWithLeadingWhiteSpaceCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = " 340137278";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberWithEndingWhiteSpaceCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "340137278 ";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberWithTabsCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "\t340137278";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberWithLeadingNumberExceptThree() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "4401372078";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number must start with 3. Please provide a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerOrderCategoryIsNull() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The category field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerOrderCategoryEnumValue() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		Worker spyWorker = spy(worker);
		workerController.createNewWorker(spyWorker);
		assertThat(spyWorker.getWorkerCategory()).isEqualTo(plumber);
		verify(spyWorker).setWorkerCategory(plumber);

	}

	@Test
	public void testCreateNewWorkerMethodWhenWorkerPhoneNumberAlreadyExists() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		when(workerRepository.findByPhoneNumber(workerPhoneNumber)).thenReturn(worker);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError("Worker with phone number " + workerPhoneNumber + " Already Exists",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenAllFieldsAreValid() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		when(workerRepository.findByPhoneNumber(workerPhoneNumber)).thenReturn(null);
		when(workerRepository.save(worker)).thenReturn(worker);
		workerController.createNewWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).save(worker);
		inOrder.verify(workerView).workerAdded(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	// Tests for update worker method
	@Test
	public void testUpdateWorkerMethodWhenNullWorker() {
		try {
			workerController.updateWorker(null);
			fail("Expected an NullPointerException to be thrown ");
		} catch (NullPointerException e) {
			assertEquals("Worker is null", e.getMessage());
		}
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerIdIsNull() {
		Worker worker = new Worker();
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerIdIsZero() {
		Worker worker = new Worker();
		worker.setWorkerId(0l);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerIdIsLessThanZero() {
		Worker worker = new Worker();
		worker.setWorkerId(-1l);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerIdIsGreaterThanZero() {
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerId()).isEqualTo(workerId);
		verify(spyWorker).setWorkerId(workerId);
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameIsNull() {
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameIsEmpty() {
		Worker worker = new Worker();
		String workerName = "";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameIsShortStringLessThanTwoCharachters() {
		Worker worker = new Worker();
		String workerName = "a";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The name must be at least 3 characters long. Please provide a valid name.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameIsShortStringEqualsToTwoCharachters() {
		Worker worker = new Worker();
		String workerName = "ab";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The name must be at least 3 characters long. Please provide a valid name.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameIsLargeStringGreaterThanTwentyCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj Naeem";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot exceed 20 characters. Please provide a shorter name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameIsLargeStringEqualsToTwentyCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj Nae";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(workerName);
		verify(spyWorker).setWorkerName(workerName);
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameWithSpecialCharacters() {
		Worker worker = new Worker();
		String workerName = "Muhammad@Ibtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The name cannot contain special characters. Please remove any special characters from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameWithNumbers() {
		Worker worker = new Worker();
		String workerName = "Muhammad1Ibtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain numbers. Please remove any number from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameWithTabs() {
		Worker worker = new Worker();
		String workerName = "Muhammad\tIbtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain tabs. Please remove any tabs from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameWithOneLeadingWhiteSpace() {
		Worker worker = new Worker();
		String actualWorkerName = " MuhammadIbtihaj";
		String expectedWorkerName = "MuhammadIbtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(actualWorkerName);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(expectedWorkerName);
		verify(spyWorker).setWorkerName(expectedWorkerName);

	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameWithTwoLeadingWhiteSpace() {
		Worker worker = new Worker();
		String actualWorkerName = "  MuhammadIbtihaj";
		String expectedWorkerName = "MuhammadIbtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(actualWorkerName);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(expectedWorkerName);
		verify(spyWorker).setWorkerName(expectedWorkerName);

	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameWithOneMiddleWhiteSpace() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(workerName);
		verify(spyWorker).setWorkerName(workerName);
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNameWithOneEndingWhiteSpace() {
		Worker worker = new Worker();
		String actualWorkerName = "Muhammad Ibtihaj ";
		String expectedWorkerName = "Muhammad Ibtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(actualWorkerName);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerName()).isEqualTo(expectedWorkerName);
		verify(spyWorker).setWorkerName(expectedWorkerName);
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberIsNull() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberIsEmptyString() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberIsShortStringLessThanTenCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "000000";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberIsLongStringGreaterThanTenCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "00000000000";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberIsLongStringEqualsTenCharachters() {
		Worker worker = new Worker();

		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerPhoneNumber()).isEqualTo(workerPhoneNumber);
		verify(spyWorker).setWorkerPhoneNumber(workerPhoneNumber);
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberWithSpecialCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372@78";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberWithAlphabetCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372a78";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberWithMiddleWhiteSpaceCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372 78";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberWithLeadingWhiteSpaceCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = " 340137278";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberWithEndingWhiteSpaceCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "340137278 ";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberWithTabsCharachters() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "\t340137278";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerPhoneNumberWithLeadingNumberExceptThree() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "4401372078";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number must start with 3. Please provide a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerOrderCategoryIsNull() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The category field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerOrderCategoryEnumValue() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		Worker spyWorker = spy(worker);
		workerController.updateWorker(spyWorker);
		assertThat(spyWorker.getWorkerCategory()).isEqualTo(plumber);
		verify(spyWorker).setWorkerCategory(plumber);
	}

	@Test
	public void testUpdateWorkerMethodWithExistingPhoneNumberForWorker() {
		String phoneNumber = "3401372678";

		long differentWorkerId = 2l;
		String differentWorkerName = "Ibtihaj";
		OrderCategory differentWorkerCategory = OrderCategory.PLUMBER;

		Worker differentWorker = new Worker(differentWorkerId, differentWorkerName, phoneNumber,
				differentWorkerCategory);

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);

		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(differentWorker);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError("A Worker with this phone number " + phoneNumber + " Already Exists",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerFoundButWithExistingOrders() {
		String phoneNumber = "3401372678";

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);

		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);
		worker.setOrders(asList(new CustomerOrder()));
		when(workerRepository.findById(workerId)).thenReturn(worker);
		workerController.updateWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError(
				"Cannot update worker " + worker.getWorkerCategory() + " Because of existing orders", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerFoundButWithExistingOrdersNull() {
		String phoneNumber = "3401372678";

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);

		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(workerRepository.modify(worker)).thenReturn(worker);
		workerController.updateWorker(worker);

		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).modify(worker);
		inOrder.verify(workerView).workerModified(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerFoundButWithEmptyExistingOrders() {
		String phoneNumber = "3401372678";

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);

		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);
		worker.setOrders(Collections.emptyList());
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(workerRepository.modify(worker)).thenReturn(worker);
		workerController.updateWorker(worker);

		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).modify(worker);
		inOrder.verify(workerView).workerModified(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	// Tests for fetch worker method

	@Test
	public void testFetchWorkerMethodWhenNullWorker() {
		try {
			workerController.fetchWorkerById(null);
			fail("Expected an NullPointerException to be thrown ");
		} catch (NullPointerException e) {
			assertEquals("Worker is null", e.getMessage());
		}
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsNull() {
		Worker worker = new Worker();
		workerController.fetchWorkerById(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsZero() {
		Worker worker = new Worker();

		long workerId = 0l;
		worker.setWorkerId(workerId);
		workerController.fetchWorkerById(worker);

		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsLessThanZero() {
		Worker worker = new Worker();

		long workerId = -1l;
		worker.setWorkerId(workerId);
		workerController.fetchWorkerById(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsGreaterThanZero() {
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);
		Worker spyWorker = spy(worker);
		workerController.fetchWorkerById(spyWorker);
		assertThat(spyWorker.getWorkerId()).isEqualTo(workerId);
		verify(spyWorker).setWorkerId(workerId);
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsGreaterThanZeroAndWorkerNotFound() {
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);
		when(workerRepository.findById(workerId)).thenReturn(null);
		workerController.fetchWorkerById(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker with id " + worker.getWorkerId() + " Not Found.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsGreaterThanZeroAndWorkerFound() {
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);

		Worker savedWorker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		savedWorker.setWorkerId(workerId);
		savedWorker.setWorkerName(workerName);
		savedWorker.setWorkerPhoneNumber(workerPhoneNumber);
		savedWorker.setWorkerCategory(plumber);

		when(workerRepository.findById(workerId)).thenReturn(savedWorker);
		workerController.fetchWorkerById(worker);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showFetchedWorker(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

}
