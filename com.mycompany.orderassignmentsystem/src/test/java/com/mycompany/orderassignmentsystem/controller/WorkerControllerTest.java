/*
 * Unit tests for the WorkerController class.
 *
 * These tests verify the functionality of the WorkerController, including
 * fetching all workers, creating or updating workers under 
 * various conditions, and searching for worker using different criteria.
 * The tests utilise Mockito for mocking dependencies and follow Test Driven 
 * Development (TDD) principles to ensure correctness of the WorkerController 
 * implementation.
 *
 * Methods tested include:
 * - getAllWorkers()
 * - createOrUpdateWorker()
 * - fetchWorkerById()
 * - deleteWorker()
 * - searchWorker()
 *
 * The setup and teardown methods handle the initialisation and cleanup 
 * of mock objects.
 *
 * Each test follows a structured approach with three main phases:
 * 1. Setup: Created environment for the test.
 * 2. Mocks: Configuring the mock objects (Added separate comment just for better readability).
 * 3. Exercise: Calling an instance method.
 * 4. Verify: Verify that the outcome matches the expected behavior.
 *
 * @see WorkerRepository
 * @see WorkerView
 * @see ValidationConfigurations
 */

package com.mycompany.orderassignmentsystem.controller;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.ignoreStubs;
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

import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.WorkerSearchOption;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.view.WorkerView;

/**
 * The Class WorkerControllerTest.
 */
public class WorkerControllerTest {

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
	 * Test all worker method when worker.
	 */
	@Test
	public void testAllWorkerMethodWhenWorker() {
		// Setup
		List<Worker> worker = asList(new Worker());

		// Mock
		when(workerRepository.findAll()).thenReturn(worker);

		// Exercise
		workerController.getAllWorkers();

		// Verify
		verify(workerView).showAllWorkers(worker);
	}

	/**
	 * Test all worker method when empty list.
	 */
	@Test
	public void testAllWorkerMethodWhenEmptyList() {
		// Setup
		List<Worker> emptyList = Collections.emptyList();

		// Mock
		when(workerRepository.findAll()).thenReturn(emptyList);

		// Exercise
		workerController.getAllWorkers();

		// Verify
		verify(workerView).showAllWorkers(emptyList);
	}

	/**
	 * Test all worker method when null list.
	 */
	@Test
	public void testAllWorkerMethodWhenNullList() {
		// Setup and Mock
		when(workerRepository.findAll()).thenReturn(null);

		// Exercise
		workerController.getAllWorkers();

		// Verify
		verify(workerView).showAllWorkers(null);
	}

	/**
	 * Test create or update worker method when null worker and operation type add.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenNullWorkerAndOperationTypeAdd() {
		// Setup & Exercise
		workerController.createOrUpdateWorker(null, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker is null", null);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create news worker method when validation configurations calls validate
	 * name throws null pointer exception and operation type add.
	 */
	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateNameThrowsNullPointerExceptionAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker();

		// Mock
		doThrow(new NullPointerException("The name field cannot be empty.")).when(validationConfigurations)
				.validateName(any());

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create news worker method when validation configurations calls validate
	 * name throws illegal argument exception and operation type add.
	 */
	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateNameThrowsIllegalArgumentExceptionAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerName(WORKER_NAME);

		// Mock
		doThrow(new IllegalArgumentException(
				"The name cannot contain numbers. Please remove any number from the name."))
				.when(validationConfigurations).validateName(anyString());

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain numbers. Please remove any number from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create news worker method when validation configurations calls validat
	 * phone throws null pointer exception and operation type add.
	 */
	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidatPhoneThrowsNullPointerExceptionAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerName(WORKER_NAME);

		// Mock
		doThrow(new NullPointerException("The phone number field cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(any());

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create news worker method when validation configurations calls validat
	 * phone throws illegal argument exception and operation type add.
	 */
	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidatPhoneThrowsIllegalArgumentExceptionAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker();
		String workerPhoneNumber = "3401372@78";
		worker.setWorkerName(WORKER_NAME);
		worker.setWorkerPhoneNumber(workerPhoneNumber);

		// Mock
		doThrow(new IllegalArgumentException(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number."))
				.when(validationConfigurations).validatePhoneNumber(anyString());

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create news worker method when validation configurations calls validate
	 * category throws null pointer exception and operation type add.
	 */
	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateCategoryThrowsNullPointerExceptionAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerName(WORKER_NAME);
		worker.setWorkerPhoneNumber(WORKER_PHONE);

		// Mock
		doThrow(new NullPointerException("Category Cannot Be Null.")).when(validationConfigurations)
				.validateCategory(any());

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Category Cannot Be Null.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create news worker method when validation configurations calls validate
	 * category throws illegal argument exception and operation type add.
	 */
	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateCategoryThrowsIllegalArgumentExceptionAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mock
		doThrow(new IllegalArgumentException("Category cannot be found.")).when(validationConfigurations)
				.validateCategory(any(OrderCategory.class));

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Category cannot be found.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create new worker when worker id is not null and operation type add.
	 */
	@Test
	public void testCreateNewWorkerWhenWorkerIdIsNotNullAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(WORKER_ID);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Unable to assign a worker ID during worker creation.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));

	}

	/**
	 * Test create or update worker method when worker phone number already exists
	 * and operation type add.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenWorkerPhoneNumberAlreadyExistsAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mock
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(worker);
		when(validationConfigurations.validatePhoneNumber(WORKER_PHONE)).thenReturn(WORKER_PHONE);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError("Worker with phone number " + WORKER_PHONE + " Already Exists", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when all fields are valid and operation
	 * type add.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenAllFieldsAreValidAndOperationTypeAdd() {
		// Setup
		Worker worker = new Worker(WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mock
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(null);
		when(workerRepository.save(worker)).thenReturn(worker);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).save(worker);
		inOrder.verify(workerView).workerAdded(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when validation configurations
	 * callsvalidate string number throws null pointer exception and operation type
	 * update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsNullPointerExceptionAndOperationTypeUpdate() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(0l);

		// Mock
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when validation configurations
	 * callsvalidate string number throws illegal argument exception and operation
	 * type update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsIllegalArgumentExceptionAndOperationTypeUpdate() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(0l);

		// Mock
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method with existing phone number for worker and
	 * operation type update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWithExistingPhoneNumberForWorkerAndOperationTypeUpdate() {
		// Setup
		long differentWorkerId = 2l;
		Worker differentWorker = new Worker(differentWorkerId, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		Worker worker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mock
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(differentWorker);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError("Worker with phone number " + WORKER_PHONE + " Already Exists", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method with existing phone number same worker id
	 * and operation type update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWithExistingPhoneNumberSameWorkerIdAndOperationTypeUpdate() {
		// Setup
		Worker worker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker.setOrders(asList(new CustomerOrder()));

		// Mocks
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(worker);
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError(
				"Cannot update worker " + worker.getWorkerCategory() + " because of existing orders", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when worker found but with existing
	 * orders and operation type update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenWorkerFoundButWithExistingOrdersAndOperationTypeUpdate() {
		// Setup
		Worker worker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker.setOrders(asList(new CustomerOrder()));

		// Mocks
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(null);
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError(
				"Cannot update worker " + worker.getWorkerCategory() + " because of existing orders", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when worker found but with existing
	 * orders null and operation type update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenWorkerFoundButWithExistingOrdersNullAndOperationTypeUpdate() {
		// Setup
		Worker worker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mocks
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(null);
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);
		when(workerRepository.save(worker)).thenReturn(worker);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).save(worker);
		inOrder.verify(workerView).workerModified(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when worker found but with empty existing
	 * orders and operation type update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenWorkerFoundButWithEmptyExistingOrdersAndOperationTypeUpdate() {

		// Setup
		Worker worker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker.setOrders(Collections.emptyList());

		// Mocks
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(null);
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);
		when(workerRepository.save(worker)).thenReturn(worker);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).save(worker);
		inOrder.verify(workerView).workerModified(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when worker not found and operation type
	 * update.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenWorkerNotFoundAndOperationTypeUpdate() {

		// Setup
		Worker worker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);
		worker.setOrders(Collections.emptyList());

		// Mocks
		when(workerRepository.findByPhoneNumber(WORKER_PHONE)).thenReturn(null);
		when(workerRepository.findById(WORKER_ID)).thenReturn(null);

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showErrorNotFound("No Worker found with id: " + worker.getWorkerId(), worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test create or update worker method when default case and operation type
	 * none.
	 */
	@Test
	public void testCreateOrUpdateWorkerMethodWhenDefaultCaseAndOperationTypeNone() {
		// Setup
		Worker worker = new Worker();

		// Exercise
		workerController.createOrUpdateWorker(worker, OperationType.NONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError("This operation is not allowed", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));

	}

	/**
	 * Test fetch worker method when null worker.
	 */
	@Test
	public void testFetchWorkerMethodWhenNullWorker() {
		// Setup & Exercise
		workerController.fetchWorkerById(null);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker is null", null);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test fetch worker method when validation configurations callsvalidate string
	 * number throws null pointer exception.
	 */
	@Test
	public void testFetchWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsNullPointerException() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(0l);

		// Mocks
		doThrow(new NullPointerException("The id field cannot be null.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		workerController.fetchWorkerById(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be null.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test fetch worker method when validation configurations callsvalidate string
	 * number throws illegal argument exception.
	 */
	@Test
	public void testFetchWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(0l);

		// Mocks
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());

		// Exercise
		workerController.fetchWorkerById(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test fetch worker method when worker id is greater than zero and worker not
	 * found.
	 */
	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsGreaterThanZeroAndWorkerNotFound() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(WORKER_ID);

		// Mocks
		when(validationConfigurations.validateStringNumber(Long.toString(WORKER_ID))).thenReturn(WORKER_ID);
		when(workerRepository.findById(WORKER_ID)).thenReturn(null);

		// Exercise
		workerController.fetchWorkerById(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showErrorNotFound("Worker with id " + worker.getWorkerId() + " Not Found.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test fetch worker method when worker id is greater than zero and worker
	 * found.
	 */
	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsGreaterThanZeroAndWorkerFound() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(WORKER_ID);
		Worker savedWorker = new Worker(WORKER_ID, WORKER_NAME, WORKER_PHONE, WORKER_CATEGORY);

		// Mocks
		when(workerRepository.findById(WORKER_ID)).thenReturn(savedWorker);

		// Exercise
		workerController.fetchWorkerById(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showFetchedWorker(savedWorker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when validation configurations calls validate
	 * search string throws null pointer exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenValidationConfigurationsCallsValidateSearchStringThrowsNullPointerException() {
		// Setup
		String searchText = "";

		// Mocks
		doThrow(new NullPointerException("The search Text field cannot be empty.")).when(validationConfigurations)
				.validateSearchString(anyString());

		// Exercise
		workerController.searchWorker(searchText, null);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The search Text field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when validation configurations calls validate
	 * search string throws illegal argument exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenValidationConfigurationsCallsValidateSearchStringThrowsIllegalArgumentException() {
		// Setup
		String searchText = "Muhammad\tIbtihaj";

		// Mocks
		doThrow(new IllegalArgumentException(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text."))
				.when(validationConfigurations).validateSearchString(anyString());

		// Exercise
		workerController.searchWorker(searchText, null);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is null.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsNull() {
		// Setup
		String searchText = "1";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		// Exercise
		workerController.searchWorker(searchText, null);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("Search Option cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker id andvalidate string number throws null pointer excetption.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndvalidateStringNumberThrowsNullPointerExcetption() {
		// Setup
		String searchText = "";

		// Mocks
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The id field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker id andvalidate string number throws illegal argument excetption.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndvalidateStringNumberThrowsIllegalArgumentExcetption() {
		// Setup
		String searchText = "";

		// Mocks
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The id field cannot be less than 1. Please provide a valid id.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker id and search text is valid number but worker not found.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndSearchTextIsValidNumberButWorkerNotFound() {
		// Setup
		String searchText = "1";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(Long.toString(WORKER_ID))).thenReturn(WORKER_ID);
		when(workerRepository.findById(WORKER_ID)).thenReturn(null);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with id: " + WORKER_ID, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker id and search text is valid positive integar and worker found.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndSearchTextIsValidPositiveIntegarAndWorkerFound() {
		// Setup
		String searchText = "1";
		Worker worker = new Worker();

		// Mocks
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(Long.toString(WORKER_ID))).thenReturn(WORKER_ID);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker name and validate name throws null pointer exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndValidateNameThrowsNullPointerException() {
		// Setup
		String searchText = "";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("The name field cannot be empty.")).when(validationConfigurations)
				.validateName(anyString());

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The name field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker name and validate name throws illegal argument exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndValidateNameThrowsIllegalArgumentException() {
		// Setup
		String searchText = "$";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException(
				"The name cannot contain special characters. Please remove any special characters from the name."))
				.when(validationConfigurations).validateName(anyString());

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The name cannot contain special characters. Please remove any special characters from the name.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker name and search text is valid name but workers found are null.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndSearchTextIsValidNameButWorkersFoundAreNull() {
		// Setup
		String searchText = "Ibtihaj";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		when(workerRepository.findByName(searchText)).thenReturn(null);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with Worker Name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker name and search text is valid name but workers found are empty.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndSearchTextIsValidNameButWorkersFoundAreEmpty() {
		// Setup
		String searchText = "Ibtihaj";

		// Mocks
		when(workerRepository.findByName(searchText)).thenReturn(Collections.emptyList());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with Worker Name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker name and search text is valid name.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndSearchTextIsValidName() {
		// Setup
		String searchText = "Muhammad";
		Worker worker = new Worker();

		// Mocks
		when(workerRepository.findByName(searchText)).thenReturn(asList(worker));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker phone number and validate phone number throws null pointer
	 * exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndValidatePhoneNumberThrowsNullPointerException() {
		// Setup
		String searchText = "";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("The phone number field cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(anyString());

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The phone number field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker phone number and validate phone number throws illegal argument
	 * exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndValidatePhoneNumberThrowsIllegalArgumentException() {
		// Setup
		String searchText = "000000";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException(
				"The phone number must be 10 characters long. Please provide a valid phone number."))
				.when(validationConfigurations).validatePhoneNumber(anyString());

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The phone number must be 10 characters long. Please provide a valid phone number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker phone number and search text is valid phone number but worker found
	 * is null.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndSearchTextIsValidPhoneNumberButWorkerFoundIsNull() {
		// Setup
		String searchText = "3401372678";

		// Mocks
		when(workerRepository.findByPhoneNumber(searchText)).thenReturn(null);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with phone number: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker phone number and search text is valid phone number and worker
	 * found.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndSearchTextIsValidPhoneNumberAndWorkerFound() {
		// Setup
		String searchText = "3401372678";
		Worker worker = new Worker();

		// Mocks
		when(workerRepository.findByPhoneNumber(searchText)).thenReturn(worker);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker category and validate enum throws null pointer exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndValidateEnumThrowsNullPointerException() {
		// Setup
		String searchText = "";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		/**
		 * eq(OrderCategory.class) ensures that the second argument must be exactly
		 * OrderCategory.class. This is used to verify that the correct enum type is
		 * being validated.
		 */
		doThrow(new NullPointerException("The category cannot be empty.")).when(validationConfigurations)
				.validateEnum(anyString(), eq(OrderCategory.class));

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The category cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker category and validate enum throws illegal argument exception.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndValidateEnumThrowsIllegalArgumentException() {
		// Setup
		String searchText = " Plumber ";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		/**
		 * eq(OrderCategory.class) ensures that the second argument must be exactly
		 * OrderCategory.class. This is used to verify that the correct enum type is
		 * being validated.
		 */
		doThrow(new IllegalArgumentException(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category."))
				.when(validationConfigurations).validateEnum(anyString(), eq(OrderCategory.class));

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker category and search text is valid category but workers found are
	 * null.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndSearchTextIsValidCategoryButWorkersFoundAreNull() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();

		// Mocks
		when(workerRepository.findByOrderCategory(category)).thenReturn(null);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with category: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker category and search text is valid category but workers found are
	 * empty list.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndSearchTextIsValidCategoryButWorkersFoundAreEmptyList() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();

		// Mocks
		when(workerRepository.findByOrderCategory(category)).thenReturn(Collections.emptyList());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with category: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is worker category and search text is valid category and worker found.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndSearchTextIsValidCategoryAndWorkerFound() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		Worker worker = new Worker();

		// Mocks
		when(workerRepository.findByOrderCategory(OrderCategory.PLUMBER)).thenReturn(asList(worker));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test search worker method when search text is valid string and search option
	 * is none.
	 */
	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsNone() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();

		// Mocks
		when(workerRepository.findByOrderCategory(category)).thenReturn(null);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		// Exercise
		workerController.searchWorker(searchText, WorkerSearchOption.NONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("This operation is not allowed", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test delete worker method with null worker.
	 */
	public void testDeleteWorkerMethodWithNullWorker() {
		// Setup & Exercise
		workerController.deleteWorker(null);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker is null", null);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test delete worker method when worker id is zero andvalidate string number
	 * throws null pointer exception.
	 */
	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsZeroAndvalidateStringNumberThrowsNullPointerException() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(0l);

		// Mocks
		doThrow(new NullPointerException("The id field cannot be null.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		workerController.deleteWorker(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be null.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test delete worker method when worker id is zero andvalidate string number
	 * throws illegal argument exception.
	 */
	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsZeroAndvalidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(0l);

		// Mocks
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());

		// Exercise
		workerController.deleteWorker(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test delete worker method when worker id is valid but no worker found.
	 */
	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButNoWorkerFound() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(WORKER_ID);

		// Mocks
		when(workerRepository.findById(WORKER_ID)).thenReturn(null);
		when(validationConfigurations.validateStringNumber(Long.toString(WORKER_ID))).thenReturn(WORKER_ID);

		// Exercise
		workerController.deleteWorker(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showErrorNotFound("No Worker found with ID: " + worker.getWorkerId(), worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test delete worker method when worker id is valid but worker found but with
	 * orders.
	 */
	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButWorkerFoundButWithOrders() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(WORKER_ID);
		worker.setOrders(asList(new CustomerOrder()));

		// Mocks
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(Long.toString(WORKER_ID))).thenReturn(WORKER_ID);

		// Exercise
		workerController.deleteWorker(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"Cannot delete worker with orders this worker has " + worker.getOrders().size() + " Orders", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test delete worker method when worker id is valid but worker found but with
	 * null orders.
	 */
	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButWorkerFoundButWithNullOrders() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(WORKER_ID);
		worker.setOrders(null);

		// Mocks
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(Long.toString(WORKER_ID))).thenReturn(WORKER_ID);

		// Exercise
		workerController.deleteWorker(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerRepository).delete(worker);
		inOrder.verify(workerView).workerRemoved(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	/**
	 * Test delete worker method when worker id is valid but worker found but with
	 * empty orders.
	 */
	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButWorkerFoundButWithEmptyOrders() {
		// Setup
		Worker worker = new Worker();
		worker.setWorkerId(WORKER_ID);
		worker.setOrders(Collections.emptyList());

		// Mocks
		when(workerRepository.findById(WORKER_ID)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(Long.toString(WORKER_ID))).thenReturn(WORKER_ID);

		// Exercise
		workerController.deleteWorker(worker);

		// Verify
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerRepository).delete(worker);
		inOrder.verify(workerView).workerRemoved(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

}
