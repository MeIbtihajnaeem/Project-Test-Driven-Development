package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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

import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.WorkerView;

public class WorkerControllerTest {

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
		workerController.createOrUpdateWorker(null, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker is null", null);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateNameThrowsNullPointerException() {
		Worker worker = new Worker();
		doThrow(new NullPointerException("The name field cannot be empty.")).when(validationConfigurations)
				.validateName(any());
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateNameThrowsIllegalArgumentException() {
		Worker worker = new Worker();
		String workerName = "Muhammad1Ibtihaj";
		worker.setWorkerName(workerName);
		doThrow(new IllegalArgumentException(
				"The name cannot contain numbers. Please remove any number from the name."))
				.when(validationConfigurations).validateName(anyString());
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The name cannot contain numbers. Please remove any number from the name.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenValidateNameReturnsValidName() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj Nae";
		worker.setWorkerName(workerName);
		Worker spyWorker = spy(worker);
		when(validationConfigurations.validateName(workerName)).thenReturn(workerName);

		workerController.createOrUpdateWorker(spyWorker, OperationType.ADD);
		assertThat(spyWorker.getWorkerName()).isEqualTo(workerName);
		verify(spyWorker).setWorkerName(workerName);
	}

	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidatPhoneThrowsNullPointerException() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		worker.setWorkerName(workerName);
		doThrow(new NullPointerException("The phone number field cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(any());
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The phone number field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidatPhoneThrowsIllegalArgumentException() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372@78";
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		doThrow(new IllegalArgumentException(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number."))
				.when(validationConfigurations).validatePhoneNumber(anyString());

		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenValidatePhoneNumberReturnsValidPhoneNumber() {
		Worker worker = new Worker();

		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		Worker spyWorker = spy(worker);
		when(validationConfigurations.validatePhoneNumber(workerPhoneNumber)).thenReturn(workerPhoneNumber);
		workerController.createOrUpdateWorker(spyWorker, OperationType.ADD);
		assertThat(spyWorker.getWorkerPhoneNumber()).isEqualTo(workerPhoneNumber);
		verify(spyWorker).setWorkerPhoneNumber(workerPhoneNumber);
	}

	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateCategoryThrowsNullPointerException() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372@78";
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);

		doThrow(new NullPointerException("Category Cannot Be Null.")).when(validationConfigurations)
				.validateCategory(any());

		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Category Cannot Be Null.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewsWorkerMethodWhenValidationConfigurationsCallsValidateCategoryThrowsIllegalArgumentException() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372@78";
		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		doThrow(new IllegalArgumentException("Category cannot be found.")).when(validationConfigurations)
				.validateCategory(any(OrderCategory.class));

		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Category cannot be found.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenValidateCategoryReturnsValidCategory() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		Worker spyWorker = spy(worker);
		when(validationConfigurations.validateCategory(plumber)).thenReturn(plumber);

		workerController.createOrUpdateWorker(spyWorker, OperationType.ADD);
		assertThat(spyWorker.getWorkerCategory()).isEqualTo(plumber);
		verify(spyWorker).setWorkerCategory(plumber);

	}

	@Test
	public void testCreateNewWorkerWhenWorkerIdIsNotNull() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Unable to assign a worker ID during worker creation.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));

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
		when(validationConfigurations.validatePhoneNumber(workerPhoneNumber)).thenReturn(workerPhoneNumber);
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
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
		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(workerPhoneNumber);
		when(workerRepository.findByPhoneNumber(workerPhoneNumber)).thenReturn(null);
		when(workerRepository.save(worker)).thenReturn(worker);
		workerController.createOrUpdateWorker(worker, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).save(worker);
		inOrder.verify(workerView).workerAdded(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsNullPointerException() {
		Worker worker = new Worker();
		worker.setWorkerId(0l);
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(anyString());
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be empty.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsIllegalArgumentException() {
		Worker worker = new Worker();
		worker.setWorkerId(0l);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerIdIsValidIdReturnedByvalidateStringNumber() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		Worker spyWorker = spy(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.createOrUpdateWorker(spyWorker, OperationType.UPDATE);

		assertThat(spyWorker.getWorkerId()).isEqualTo(workerId);
		verify(spyWorker).setWorkerId(workerId);
	}

	@Test
	public void testUpdateWorkerMethodWithExistingPhoneNumberForWorker() {
		String phoneNumber = "3401372678";

		Long differentWorkerId = 2l;
		String differentWorkerName = "Ibtihaj";
		OrderCategory differentWorkerCategory = OrderCategory.PLUMBER;

		Worker differentWorker = new Worker(differentWorkerId, differentWorkerName, phoneNumber,
				differentWorkerCategory);

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		Long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);

		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(phoneNumber);
		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(differentWorker);
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError("Worker with phone number " + phoneNumber + " Already Exists", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerFoundButWithExistingOrders() {
		String phoneNumber = "3401372678";

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		Long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);
		when(validationConfigurations.validateStringNumber(anyString())).thenReturn(workerId);
		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(phoneNumber);
		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);
		worker.setOrders(asList(new CustomerOrder()));
		when(workerRepository.findById(workerId)).thenReturn(worker);
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError(
				"Cannot update worker " + worker.getWorkerCategory() + " because of existing orders", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerFoundButWithExistingOrdersNull() {
		String phoneNumber = "3401372678";

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		Long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);
		when(validationConfigurations.validateStringNumber(anyString())).thenReturn(workerId);
		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(phoneNumber);
		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(workerRepository.save(worker)).thenReturn(worker);
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).save(worker);
		inOrder.verify(workerView).workerModified(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerFoundButWithEmptyExistingOrders() {
		String phoneNumber = "3401372678";

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		Long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);
		when(validationConfigurations.validateStringNumber(anyString())).thenReturn(workerId);
		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(phoneNumber);
		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);
		worker.setOrders(Collections.emptyList());
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(workerRepository.save(worker)).thenReturn(worker);
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerRepository).save(worker);
		inOrder.verify(workerView).workerModified(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testUpdateWorkerMethodWhenWorkerNotFound() {
		String phoneNumber = "3401372678";

		String workerName = "Muhammad Ibtihaj";
		OrderCategory category = OrderCategory.PLUMBER;
		Long workerId = 1l;

		Worker worker = new Worker(workerId, workerName, phoneNumber, category);
		worker.setOrders(Collections.emptyList());
		when(validationConfigurations.validateStringNumber(anyString())).thenReturn(workerId);
		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(phoneNumber);
		when(workerRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);
		when(workerRepository.findById(workerId)).thenReturn(null);
		workerController.createOrUpdateWorker(worker, OperationType.UPDATE);

		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showErrorNotFound("No Worker found with id: " + worker.getWorkerId(), worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testCreateNewWorkerMethodWhenDefaultCase() {
		Worker worker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;

		worker.setWorkerName(workerName);
		worker.setWorkerPhoneNumber(workerPhoneNumber);
		worker.setWorkerCategory(plumber);
		when(validationConfigurations.validateCategory(plumber)).thenReturn(plumber);

		workerController.createOrUpdateWorker(worker, OperationType.NONE);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showError("This operation is not allowed", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));

	}

	// Tests for fetch worker method

	@Test
	public void testFetchWorkerMethodWhenNullWorker() {
		workerController.fetchWorkerById(null);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker is null", null);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsNullPointerException() {
		Worker worker = new Worker();

		doThrow(new NullPointerException("The id field cannot be null.")).when(validationConfigurations)
				.validateStringNumber(any());
		workerController.fetchWorkerById(worker);

		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be null.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenValidationConfigurationsCallsvalidateStringNumberThrowsIllegalArgumentException() {
		Worker worker = new Worker();

		Long workerId = 0l;
		worker.setWorkerId(workerId);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		workerController.fetchWorkerById(worker);

		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsValidReturnedByvalidateStringNumber() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		Worker spyWorker = spy(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.fetchWorkerById(spyWorker);
		assertThat(spyWorker.getWorkerId()).isEqualTo(workerId);
		verify(spyWorker).setWorkerId(workerId);
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsGreaterThanZeroAndWorkerNotFound() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(null);
		workerController.fetchWorkerById(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showErrorNotFound("Worker with id " + worker.getWorkerId() + " Not Found.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchWorkerMethodWhenWorkerIdIsGreaterThanZeroAndWorkerFound() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);

		Worker savedWorker = new Worker();
		String workerName = "Muhammad Ibtihaj";
		String workerPhoneNumber = "3401372678";
		OrderCategory plumber = OrderCategory.PLUMBER;
		savedWorker.setWorkerId(workerId);
		savedWorker.setWorkerName(workerName);
		savedWorker.setWorkerPhoneNumber(workerPhoneNumber);
		savedWorker.setWorkerCategory(plumber);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(savedWorker);
		workerController.fetchWorkerById(worker);
		InOrder inOrder = Mockito.inOrder(workerRepository, workerView);
		inOrder.verify(workerView).showFetchedWorker(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	// Tests for search worker method

	@Test
	public void testSearchWorkerMethodWhenValidationConfigurationsCallsValidateSearchStringThrowsNullPointerException() {
		String searchText = "";

		doThrow(new NullPointerException("The search Text field cannot be empty.")).when(validationConfigurations)
				.validateSearchString(anyString());
		workerController.searchWorker(searchText, null);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The search Text field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenValidationConfigurationsCallsValidateSearchStringThrowsIllegalArgumentException() {
		String searchText = "Muhammad\tIbtihaj";
		doThrow(new IllegalArgumentException(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text."))
				.when(validationConfigurations).validateSearchString(anyString());
		workerController.searchWorker(searchText, null);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsNull() {
		String searchText = "1";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		workerController.searchWorker(searchText, null);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("Search Option cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndvalidateStringNumberThrowsNullPointerExcetption() {
		String searchText = "";
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
//		when(validationConfigurations.validateStringNumber(anyString())).thenReturn("1");
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The id field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndvalidateStringNumberThrowsIllegalArgumentExcetption() {
		String searchText = "";
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
//		when(validationConfigurations.validateStringNumber(anyString())).thenReturn("1");

		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The id field cannot be less than 1. Please provide a valid id.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndSearchTextIsValidNumberButWorkerNotFound() {
		String searchText = "1";
		Long workerId = 1l;
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(null);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with id: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerIdAndSearchTextIsValidPositiveIntegarAndWorkerFound() {
		String searchText = "1";
		Long workerId = 1l;
		Worker worker = new Worker();
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndValidateNameThrowsNullPointerException() {
		String searchText = "";
		doThrow(new NullPointerException("The name field cannot be empty.")).when(validationConfigurations)
				.validateName(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The name field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndValidateNameThrowsIllegalArgumentException() {
		String searchText = "$";
		doThrow(new IllegalArgumentException(
				"The name cannot contain special characters. Please remove any special characters from the name."))
				.when(validationConfigurations).validateName(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The name cannot contain special characters. Please remove any special characters from the name.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndSearchTextIsValidNameButWorkersFoundAreNull() {
		String searchText = "Ibtihaj";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		when(workerRepository.findByName(searchText)).thenReturn(null);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with Worker Name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndSearchTextIsValidNameButWorkersFoundAreEmpty() {
		String searchText = "Ibtihaj";
		when(workerRepository.findByName(searchText)).thenReturn(Collections.emptyList());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with Worker Name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerNameAndSearchTextIsValidName() {
		String searchText = "Muhammad";
		Worker worker = new Worker();
		when(workerRepository.findByName(searchText)).thenReturn(asList(worker));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_NAME);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndValidatePhoneNumberThorwsNullPointerException() {

		String searchText = "";

		doThrow(new NullPointerException("The phone number field cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The phone number field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndValidatePhoneNumberThorwsIllegalArgumentException() {

		String searchText = "000000";

		doThrow(new IllegalArgumentException(
				"The phone number must be 10 characters long. Please provide a valid phone number."))
				.when(validationConfigurations).validatePhoneNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The phone number must be 10 characters long. Please provide a valid phone number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndSearchTextIsValidPhoneNumberButWorkerFoundIsNull() {
		String searchText = "3401372678";
		when(workerRepository.findByPhoneNumber(searchText)).thenReturn(null);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with phone number: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerPhoneNumberAndSearchTextIsValidPhoneNumberAndWorkerFound() {
		String searchText = "3401372678";
		Worker worker = new Worker();
		when(workerRepository.findByPhoneNumber(searchText)).thenReturn(worker);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_PHONE);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndValidateEnumThrowsNullPointerException() {
		String searchText = "";

		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		// eq(OrderCategory.class) ensures that the second argument must be exactly
		// OrderCategory.class.
		doThrow(new NullPointerException("The category cannot be empty.")).when(validationConfigurations)
				.validateEnum(anyString(), eq(OrderCategory.class));

		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("The category cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndValidateEnumThrowsIllegalArgumentException() {
		String searchText = " Plumber ";

		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		// eq(OrderCategory.class) ensures that the second argument must be exactly
		// OrderCategory.class.

		doThrow(new IllegalArgumentException(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category."))
				.when(validationConfigurations).validateEnum(anyString(), eq(OrderCategory.class));

		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndSearchTextIsValidCategoryButWorkersFoundAreNull() {
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		when(workerRepository.findByOrderCategory(category)).thenReturn(null);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with category: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndSearchTextIsValidCategoryButWorkersFoundAreEmptyList() {
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		when(workerRepository.findByOrderCategory(category)).thenReturn(Collections.emptyList());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("No result found with category: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsWorkerCategoryAndSearchTextIsValidCategoryAndWorkerFound() {
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		Worker worker = new Worker();
		when(workerRepository.findByOrderCategory(OrderCategory.PLUMBER)).thenReturn(asList(worker));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);
		workerController.searchWorker(searchText, WorkerSearchOption.WORKER_CATEGORY);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchResultForWorker(asList(worker));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testSearchWorkerMethodWhenSearchTextIsValidStringAndSearchOptionIsNone() {
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		when(workerRepository.findByOrderCategory(category)).thenReturn(null);
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		workerController.searchWorker(searchText, WorkerSearchOption.NONE);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchError("This operation is not allowed", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	// Tests for delete method

	public void testDeleteWorkerMethodWithNullWorker() {
		workerController.deleteWorker(null);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker is null", null);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsZeroAndvalidateStringNumberThrowsNullPointerException() {
		Worker worker = new Worker();
		doThrow(new NullPointerException("The id field cannot be null.")).when(validationConfigurations)
				.validateStringNumber(any());
		workerController.deleteWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be null.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsZeroAndvalidateStringNumberThrowsIllegalArgumentException() {
		Worker worker = new Worker();
		worker.setWorkerId(0l);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		workerController.deleteWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidAndReturnedByvalidateStringNumber() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		Worker spyWorker = spy(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		workerController.deleteWorker(spyWorker);
		assertThat(spyWorker.getWorkerId()).isEqualTo(workerId);
		verify(spyWorker).setWorkerId(workerId);
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButNoWorkerFound() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		when(workerRepository.findById(workerId)).thenReturn(null);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.deleteWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showErrorNotFound("No Worker found with ID: " + worker.getWorkerId(), worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButWorkerFoundButWithOrders() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setOrders(asList(new CustomerOrder()));
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.deleteWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError(
				"Cannot delete worker with orders this worker has " + worker.getOrders().size() + " Orders", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButWorkerFoundButWithNullOrders() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setOrders(null);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.deleteWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerRepository).delete(worker);
		inOrder.verify(workerView).workerRemoved(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsValidButWorkerFoundButWithEmptyOrders() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setOrders(Collections.emptyList());
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		workerController.deleteWorker(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerRepository).delete(worker);
		inOrder.verify(workerView).workerRemoved(worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	// Tests for search order by worker id

	public void testFetchOrdersByWorkerIdMethodWithNullWorker() {
		workerController.fetchOrdersByWorkerId(null);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showError("Worker is null", null);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchOrdersByWorkerIdMethodWhenWorkerIdIsZeroAndvalidateStringNumberThrowsNullPointerException() {
		Worker worker = new Worker();
		doThrow(new NullPointerException("The id field cannot be null.")).when(validationConfigurations)
				.validateStringNumber(any());
		workerController.fetchOrdersByWorkerId(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchOrderByWorkerIdError("The id field cannot be null.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchOrdersByWorkerIdMethodWhenWorkerIdIsZeroAndvalidateStringNumberThrowsIllegalArgumentException() {
		Worker worker = new Worker();
		worker.setWorkerId(0l);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		workerController.fetchOrdersByWorkerId(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showSearchOrderByWorkerIdError(
				"The id field cannot be less than 1. Please provide a valid id.", worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchOrdersByWorkerIdMethodWhenWorkerIdIsValidAndReturnedByvalidateStringNumber() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		Worker spyWorker = spy(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.fetchOrdersByWorkerId(spyWorker);
		assertThat(spyWorker.getWorkerId()).isEqualTo(workerId);
		verify(spyWorker).setWorkerId(workerId);
	}

	@Test
	public void testFetchOrdersByWorkerIdMethodWhenWorkerIdIsValidButNoWorkerFound() {
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		when(workerRepository.findById(workerId)).thenReturn(null);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		workerController.fetchOrdersByWorkerId(worker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showErrorNotFound("No Worker found with ID: " + worker.getWorkerId(), worker);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void testFetchOrdersByWorkerIdMethodWhenWorkerIdIsValidAndOrdersExists() {
		Worker newWorker = new Worker();
		Long workerId = 1l;
		newWorker.setWorkerId(workerId);
		Worker savedWorker = new Worker();
		savedWorker.setWorkerId(workerId);
		savedWorker.setOrders(Collections.emptyList());
		when(workerRepository.findById(workerId)).thenReturn(savedWorker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		workerController.fetchOrdersByWorkerId(newWorker);
		InOrder inOrder = Mockito.inOrder(workerView, workerRepository);
		inOrder.verify(workerView).showOrderByWorkerId(savedWorker.getOrders());
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

}
