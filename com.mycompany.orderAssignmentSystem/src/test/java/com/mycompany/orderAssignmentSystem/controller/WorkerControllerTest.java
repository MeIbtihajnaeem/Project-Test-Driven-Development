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

}
