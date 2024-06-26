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
import com.mycompany.orderassignmentsystem.enumerations.OrderSearchOptions;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.view.OrderView;

public class OrderControllerTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderView orderView;

	@Mock
	private WorkerRepository workerRepository;

	@Mock
	private ValidationConfigurations validationConfigurations;

	@InjectMocks
	private OrderController orderController;

	private AutoCloseable closeable;

	private String CUSTOMER_NAME = "Muhammad Ibtihaj";
	private String CUSTOMER_PHONE = "3401372678";
	private String CUSTOMER_ADDRESS = "1234 Main Street , Apt 101, Springfield, USA 12345";
	private String ORDER_APPOINTMENT_DATE = "12-12-2024";
	private String ORDER_DESCRIPTION = "Please ensure all connection are leak-proof.Thanks";
	private OrderCategory ORDER_CATEGORY = OrderCategory.PLUMBER;
	private OrderStatus ORDER_STATUS = OrderStatus.PENDING;

	@Before
	public void Setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	// Tests for show all orders
	@Test
	public void testAllOrdersMethodWhenCustomer() {
		// Setup
		List<CustomerOrder> orders = asList(new CustomerOrder());

		// Mocks
		when(orderRepository.findAll()).thenReturn(orders);

		// Exercise
		orderController.allOrders();

		// Verify
		verify(orderView).showAllOrder(orders);
	}

	@Test
	public void testAllOrdersMethodWhenEmptyList() {
		// Setup
		List<CustomerOrder> emptyList = Collections.emptyList();

		// Mocks
		when(orderRepository.findAll()).thenReturn(emptyList);

		// Exercise
		orderController.allOrders();

		// Verify
		verify(orderView).showAllOrder(emptyList);
	}

	@Test
	public void testAllOrdersMethodWhenNullList() {
		// Setup, mock
		when(orderRepository.findAll()).thenReturn(null);

		// Exercise
		orderController.allOrders();

		// Verify
		verify(orderView).showAllOrder(null);
	}

	// Tests for show all workers
	@Test
	public void testAllWorkersMethodWhenWorker() {
		// Setup
		List<Worker> workers = asList(new Worker());

		// Mocks
		when(workerRepository.findAll()).thenReturn(workers);

		// Exercise
		orderController.allWorkers();

		// Verify
		verify(orderView).showAllWorkers(workers);
	}

	@Test
	public void testAllWorkersMethodWhenEmptyList() {
		// Setup
		List<Worker> emptyList = Collections.emptyList();

		// Mocks
		when(workerRepository.findAll()).thenReturn(emptyList);

		// Exercise
		orderController.allWorkers();

		// Verify
		verify(orderView).showAllWorkers(emptyList);
	}

	@Test
	public void testAllWorkersMethodWhenNullList() {
		// Setup, mock
		when(workerRepository.findAll()).thenReturn(null);

		// Exercise
		orderController.allWorkers();

		// Verify
		verify(orderView).showAllWorkers(null);
	}

	// Tests for create order method
	@Test
	public void testCreateOrUpdateOrderMethodWhenNullOperationType() {
		// Setup, Exercise
		orderController.createOrUpdateOrder(null, null);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Operation Type is null", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenNullCustomerOrderAndOperationTypeIsAdd() {
		// Setup, Exercise
		orderController.createOrUpdateOrder(null, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateNameThrowsNullPointerExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("");

		// Mocks
		doThrow(new NullPointerException("The name field cannot be empty.")).when(validationConfigurations)
				.validateName(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateNameThrowsIllegalArgumentExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad@Ibtihaj");

		// Mocks
		doThrow(new IllegalArgumentException("The name field cannot contain Special characters."))
				.when(validationConfigurations).validateName(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot contain Special characters.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidatePhoneNumberThrowsNullPointerExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String customerPhoneNumber = "";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerName(CUSTOMER_NAME);

		// Mocks
		doThrow(new NullPointerException("The phone number field cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidatePhoneNumberThrowsIllegalArgumentExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String customerPhoneNumber = "000000";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerName(CUSTOMER_NAME);

		// Mocks
		doThrow(new IllegalArgumentException(
				"The phone number must be 10 characters long. Please provide a valid phone number."))
				.when(validationConfigurations).validatePhoneNumber(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateAddressThrowsNullPointerExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String customerAddress = "";
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(customerAddress);

		// Mocks
		doThrow(new NullPointerException("The address field cannot be empty.")).when(validationConfigurations)
				.validateAddress(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateAddressThrowsIllegalArgumentExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String customerAddress = "";
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(customerAddress);

		// Mocks
		doThrow(new IllegalArgumentException(
				"The Address must be at least 10 characters long. Please provide a valid Address."))
				.when(validationConfigurations).validateAddress(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address must be at least 10 characters long. Please provide a valid Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateDateThrowsNullPointerExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String appointmentDate = "";
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(appointmentDate);

		// Mocks
		doThrow(new NullPointerException("Date field cannot be empty.")).when(validationConfigurations)
				.validateStringDate(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Date field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateDateThrowsIllegalArgumentExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String appointmentDate = "1212/2024";
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(appointmentDate);

		// Mocks
		doThrow(new IllegalArgumentException("Please provide a valid date.")).when(validationConfigurations)
				.validateStringDate(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateDescriptionThrowsNullPointerExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String description = "";
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(ORDER_APPOINTMENT_DATE);
		order.setOrderDescription(description);

		// Mocks
		doThrow(new NullPointerException("The description field cannot be empty.")).when(validationConfigurations)
				.validateDescription(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The description field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateDescriptionThrowsIllegalArgumentExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		String description = "no descr";
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(ORDER_APPOINTMENT_DATE);
		order.setOrderDescription(description);

		// Mocks
		doThrow(new IllegalArgumentException(
				"The description must be at least 10 characters long. Please provide a valid description."))
				.when(validationConfigurations).validateDescription(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The description must be at least 10 characters long. Please provide a valid description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateCategoryThrowsNullPointerExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(ORDER_APPOINTMENT_DATE);
		order.setOrderDescription(ORDER_DESCRIPTION);

		// Mocks
		doThrow(new NullPointerException("The category field cannot be empty.")).when(validationConfigurations)
				.validateCategory(any());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The category field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateCategoryThrowsIllegalArgumentExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(ORDER_APPOINTMENT_DATE);
		order.setOrderDescription(ORDER_DESCRIPTION);

		// Mocks
		doThrow(new IllegalArgumentException("The category field does not match.")).when(validationConfigurations)
				.validateCategory(any());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The category field does not match.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateStatusThrowsNullPointerExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(ORDER_APPOINTMENT_DATE);
		order.setOrderDescription(ORDER_DESCRIPTION);
		order.setOrderCategory(ORDER_CATEGORY);

		// Mocks
		doThrow(new NullPointerException("The status field cannot be empty.")).when(validationConfigurations)
				.validateStatus(any());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The status field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateStatusThrowsIllegalArgumentExceptionAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(ORDER_APPOINTMENT_DATE);
		order.setOrderDescription(ORDER_DESCRIPTION);
		order.setOrderCategory(ORDER_CATEGORY);

		// Mocks
		doThrow(new IllegalArgumentException("The status field does not match.")).when(validationConfigurations)
				.validateStatus(any());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The status field does not match.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenWorkerIsNullAndOperationTypeIsAdd() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(CUSTOMER_NAME);
		order.setCustomerPhoneNumber(CUSTOMER_PHONE);
		order.setCustomerAddress(CUSTOMER_ADDRESS);
		order.setAppointmentDate(ORDER_APPOINTMENT_DATE);
		order.setOrderDescription(ORDER_DESCRIPTION);
		order.setOrderCategory(ORDER_CATEGORY);
		order.setOrderStatus(ORDER_STATUS);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The worker field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateStringNumberThrowsNullPointerExceptionForWorkerIdAndOperationTypeIsAdd() {
		// Setup
		long workerId = 0l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateStringNumberThrowsIllegalArgumentExceptionForWorkerIdAndOperationTypeIsAdd() {
		// Setup
		long workerId = -1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenCustomerOrderIdIsNotNullAndOperationTypeIsAdd() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		long orderId = 1l;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Unable to assign an order ID during order creation.", order);

		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenOrderStatusIsNotPendingAndOperationTypeIsAdd() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		OrderStatus status = OrderStatus.COMPLETED;
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, status, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The order status should be initiated with 'pending' status.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenWorkerNotFoundAndOperationTypeIsAdd() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(null);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("Worker with this ID " + worker.getWorkerId() + " not found",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);

	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenWorkerAndOrderCategoryNotAlignedAndOperationTypeIsAdd() {
		// Setup
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		long workerId = 1l;
		Worker worker = new Worker(workerId, workerCategory);
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, orderCategory, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order and worker categories must align", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);

	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenWorkerOrdersAreNullAndOperationTypeIsAdd() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);

	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenWorkerOrdersAreEmptyAndOperationTypeIsAdd() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		worker.setOrders(Collections.emptyList());
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);

	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenWorkerOrdersHavePendingOrderAndOperationTypeIsAdd() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		CustomerOrder workerOrder = new CustomerOrder();
		workerOrder.setOrderStatus(OrderStatus.PENDING);
		worker.setOrders(asList(workerOrder));
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);

	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenWorkerOrdersHaveNoPendingOrdersAndOperationTypeIsAdd() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		CustomerOrder workerOrder = new CustomerOrder();
		workerOrder.setOrderStatus(OrderStatus.COMPLETED);
		worker.setOrders(asList(workerOrder));
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.ADD);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);

	}

	// Update

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateStringNumberThrowsNullPointerExceptionAndOperationTypeIsUpdate() {
		// Setup
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenValidateStringNumberThrowsIllegalArgumentExceptionAndOperationTypeIsUpdate() {
		// Setup
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);
		long orderId = 0l;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);
		verifyNoMoreInteractions(workerRepository);
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerNotFoundAndOperationTypeIsUpdate() {
		// Setup
		Worker worker = new Worker();
		long workerId = 1l;
		worker.setWorkerId(workerId);
		long orderId = 1l;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(null);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("Worker with this ID " + worker.getWorkerId() + " not found",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView);

	}

	@Test
	public void testUpdateOrderMethodWhenWorkerAndOrderCategoryNotAlignedAndOperationTypeIsUpdate() {
		// Setup
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		long workerId = 1l;
		Worker worker = new Worker(workerId, workerCategory);
		long orderId = 1l;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, orderCategory, ORDER_STATUS, worker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order and worker categories must align", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);

	}

	@Test
	public void testUpdateOrderWhenWorkerOrdersAreNullAndOperationTypeIsUpdate() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);

		long orderId = 1L;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		long savedWorkerId = 2L;
		Worker savedWorker = new Worker(savedWorkerId, ORDER_CATEGORY);

		CustomerOrder savedOrder = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, savedWorker);

		// Mocks
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.save(order)).thenReturn(order);
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersAreEmptyAndOperationTypeIsUpdate() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		worker.setOrders(Collections.emptyList());
		long orderId = 1L;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);
		long savedWorkerId = 2l;
		Worker savedWorker = new Worker(savedWorkerId, ORDER_CATEGORY);
		CustomerOrder savedOrder = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, savedWorker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.save(order)).thenReturn(order);
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersHavePendingOrderAndOperationTypeIsUpdate() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		CustomerOrder workerOrder = new CustomerOrder();
		workerOrder.setOrderStatus(OrderStatus.PENDING);
		worker.setOrders(asList(workerOrder));
		long orderId = 1L;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);
		long savedWorkerId = 2l;
		Worker savedWorker = new Worker(savedWorkerId, ORDER_CATEGORY);
		CustomerOrder savedOrder = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, savedWorker);

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.save(order)).thenReturn(order);
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersHaveNoPendingOrdersAndOperationTypeIsUpdate() {
		// Setup
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		long orderId = 1l;
		CustomerOrder order = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);
		worker.setOrders(asList(order));

		// Mocks
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(order);

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenOperationTypeNoneAndOperationTypeIsUpdate() {
		// Setup
		CustomerOrder order = new CustomerOrder();

		// Exercise
		orderController.createOrUpdateOrder(order, OperationType.NONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("This operation is not allowed here", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	// tests for fetch by order id method

	@Test
	public void testFetchOrderByIdMethodWhenOrderIsNull() {
		// Setup & Exercise
		orderController.fetchOrderById(null);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null.", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testFetchOrderByIdMethodWhenValidateStringNumberThrowsNullPointerException() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		long orderId = 0l;
		order.setOrderId(orderId);

		// Mocks
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.fetchOrderById(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testFetchOrderByIdMethodWhenValidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		long orderId = 0l;
		order.setOrderId(orderId);

		// Mocks
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());

		// Exercise
		orderController.fetchOrderById(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsValidButOrderNotFound() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);

		// Mocks
		when(orderRepository.findById(orderId)).thenReturn(null);

		// Exercise
		orderController.fetchOrderById(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("Order with ID " + order.getOrderId() + " not found.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsValid() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		long workerId = 1l;
		Worker worker = new Worker(workerId, ORDER_CATEGORY);
		CustomerOrder savedOrder = new CustomerOrder(orderId, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, worker);

		// Mocks
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);

		// Exercise
		orderController.fetchOrderById(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showFetchedOrder(savedOrder);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	// Tests for delete order

	@Test
	public void testDeleteOrderMethodWhenNullCustomerOrder() {
		// Setup & Exercise
		orderController.deleteOrder(null);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testDeleteOrderMethodWhenValidateStringNumberThrowsNullPointerException() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(0l);

		// Mocks
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(anyString());

		// Exercise
		orderController.deleteOrder(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testDeleteOrderMethodWhenValidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(0l);

		// Mocks
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());

		// Exercise
		orderController.deleteOrder(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testDeleteOrderMethodWhenCustomerOrderIdIsValidButNoOrderFoundWithId() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);

		// Mocks
		when(orderRepository.findById(orderId)).thenReturn(null);

		// Exercise
		orderController.deleteOrder(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("No order found with ID: " + order.getOrderId(), order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testDeleteOrderMethodWhenCustomerOrderIdIsValidAndOrderFound() {
		// Setup
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);

		// Mocks
		when(orderRepository.findById(orderId)).thenReturn(order);

		// Exercise
		orderController.deleteOrder(order);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).delete(order);
		inOrder.verify(orderView).orderRemoved(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	// tests for search options

	@Test
	public void testSearchOrderMethodWhenValidateSearchThrowsNullPointerException() {
		// Mocks
		doThrow(new NullPointerException("The search Text field cannot be empty.")).when(validationConfigurations)
				.validateSearchString(any());

		// Exercise
		orderController.searchOrder(null, null);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The search Text field cannot be empty.", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testSearchOrderMethodWhenValidateSearchThrowsIllegalArgumentException() {
		// Setup
		String searchText = "Muhammad\tIbtihaj";

		// Mocks
		doThrow(new IllegalArgumentException(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text."))
				.when(validationConfigurations).validateSearchString(anyString());

		// Exercise
		orderController.searchOrder(searchText, null);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void testSearchOrderMethodWhenSearchTextIsValidStringAndSearchOptionIsNull() {
		// Setup
		String searchText = "1";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		// Exercise
		orderController.searchOrder(searchText, null);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Search option cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderIdAndValidateStringNumberThrowsNullPointerException() {
		// Setup
		String searchText = "";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderIdAndValidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		String searchText = "a";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(anyString());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderIdAndvalidateStringNumberThrowsNullPointerException() {
		// Setup
		String searchText = "1";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("Id Field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderIdAndvalidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		String searchText = "0";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException("Id Field cannot be zero.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be zero.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderIdSearchTextIsValidNumberButOrderNotFound() {
		// Setup
		String searchText = "1";
		long orderId = 1l;

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(orderId);
		when(validationConfigurations.validateStringNumber(Long.toString(orderId))).thenReturn(orderId);
		when(orderRepository.findById(orderId)).thenReturn(null);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No result found with ID: " + orderId, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderIdSearchTextIsValidNumberAndOrderFound() {
		// Setup
		String searchText = "1";
		long orderId = 1l;
		CustomerOrder order = new CustomerOrder();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(orderId);
		when(validationConfigurations.validateStringNumber(Long.toString(orderId))).thenReturn(orderId);
		when(orderRepository.findById(orderId)).thenReturn(order);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdAndValidateStringNumberThrowsNullPointerException() {
		// Setup
		String searchText = "";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdAndValidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		String searchText = "a";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(anyString());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdAndvalidateStringNumberThrowsNullPointerException() {
		// Setup
		String searchText = "1";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("Id Field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdAndvalidateStringNumberThrowsIllegalArgumentException() {
		// Setup
		String searchText = "0";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException("Id Field cannot be zero.")).when(validationConfigurations)
				.validateStringNumber(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be zero.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdSearchTextIsValidNumberButWorkerNotFound() {
		// Setup
		String searchText = "1";
		long workerId = 1l;

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(Long.toString(workerId))).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(null);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No result found with ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdSearchTextIsValidNumberButOrdersAreNull() {
		// Setup
		String searchText = "1";
		long workerId = 1l;
		Worker worker = new Worker();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(Long.toString(workerId))).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with worker ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdSearchTextIsValidNumberButOrdersAreEmpty() {
		// Setup
		String searchText = "1";
		long workerId = 1l;
		Worker worker = new Worker();
		worker.setOrders(Collections.emptyList());

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(Long.toString(workerId))).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with worker ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void searchOrderWhenSearchOptionIsWorkerIdSearchTextIsValidNumberAndOrderFound() {
		// Setup
		String searchText = "1";
		long workerId = 1l;
		Worker worker = new Worker();
		CustomerOrder order = new CustomerOrder();
		worker.setOrders(asList(order));

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(Long.toString(workerId))).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(worker);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
		verifyNoMoreInteractions(orderView);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerPhoneNumberAndValidatePhoneNumberThrowsNullPointerException() {
		// Setup
		String searchText = "000000";

		// Mocks
		doThrow(new NullPointerException("Phone Number cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Phone Number cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerPhoneNumberAndValidatePhoneNumberThrowsIllegalArgumentException() {
		// Setup
		String searchText = "000000";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException("Phone Number cannot be less then 10 characters."))
				.when(validationConfigurations).validatePhoneNumber(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Phone Number cannot be less then 10 characters.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerPhoneNumberSearchTextIsValidPhoneNumberButOrdersAreNull() {
		// Setup
		String searchText = "3401372678";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(null);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with phone number: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerPhoneNumberSearchTextIsValidPhoneNumberButOrdersAreEmpty() {
		// Setup
		String searchText = "3401372678";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(Collections.emptyList());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with phone number: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerPhoneNumberSearchTextIsValidPhoneNumber() {
		// Setup
		String searchText = "3401372678";
		CustomerOrder order = new CustomerOrder();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(asList(order));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerNameAndValidateNameThrowsNullPointerException() {
		// Setup
		String searchText = "a";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("Name field cannot be empty.")).when(validationConfigurations)
				.validateName(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Name field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerNameAndValidateName_IllegalArgumentException() {
		// Setup
		String searchText = "a";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException(
				"The name must be at least 3 characters long. Please provide a valid name."))
				.when(validationConfigurations).validateName(any());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The name must be at least 3 characters long. Please provide a valid name.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerNameSearchTextIsValidNameButOrdersAreNull() {
		// Setup
		String searchText = "Muhammad";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerName(searchText)).thenReturn(null);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with customer name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerNameSearchTextIsValidNameButOrdersAreEmpty() {
		// Setup
		String searchText = "Muhammad";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerName(searchText)).thenReturn(Collections.emptyList());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with customer name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsCustomerNameSearchTextIsValidName_OrderFound() {
		// Setup
		String searchText = "Muhammad";
		CustomerOrder customerOrder = new CustomerOrder();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerName(searchText)).thenReturn(asList(customerOrder));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderStatusAndValidateEnumThrowsNullPointerException() {
		// Setup
		String searchText = " Pending ";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("Status field cannot be empty.")).when(validationConfigurations)
				.validateEnum(any(), eq(OrderStatus.class));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Status field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderStatusAndValidateEnumThrowsIllegalArgumentException() {
		// Setup
		String searchText = " Pending ";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException(
				"The status cannot contain whitespaces. Please remove any whitespaces from the status."))
				.when(validationConfigurations).validateEnum(anyString(), eq(OrderStatus.class));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The status cannot contain whitespaces. Please remove any whitespaces from the status.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderStatusSearchTextIsValidStatusButOrdersAreNull() {
		// Setup
		OrderStatus status = OrderStatus.PENDING;
		String searchText = status.toString();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderStatus.class)).thenReturn(status);
		when(orderRepository.findByOrderStatus(status)).thenReturn(null);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with status: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderStatusSearchTextIsValidStatusButOrdersAreEmpty() {
		// Setup
		OrderStatus status = OrderStatus.PENDING;
		String searchText = status.toString();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderStatus.class)).thenReturn(status);
		when(orderRepository.findByOrderStatus(status)).thenReturn(Collections.emptyList());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with status: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderStatusSearchTextIsValidStatusAndOrdersFound() {
		// Setup
		OrderStatus status = OrderStatus.PENDING;
		String searchText = status.toString();
		CustomerOrder customerOrder = new CustomerOrder();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderStatus.class)).thenReturn(status);
		when(orderRepository.findByOrderStatus(status)).thenReturn(asList(customerOrder));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderCategoryAndValidateEnumThrowsNullPointerException() {
		// Setup
		String searchText = " Plumber ";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("category field cannot be empty.")).when(validationConfigurations)
				.validateEnum(any(), eq(OrderCategory.class));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("category field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderCategoryAndValidateEnumThrowsIllegalArgumentException() {
		// Setup
		String searchText = " Plumber ";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category."))
				.when(validationConfigurations).validateEnum(any(), eq(OrderCategory.class));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderCategorySearchTextIsValidCategoryButOrdersAreNull() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);
		when(orderRepository.findByOrderCategory(category)).thenReturn(null);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with category: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderCategorySearchTextIsValidCategoryButOrdersAreEmpty() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);
		when(orderRepository.findByOrderCategory(category)).thenReturn(Collections.emptyList());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with category: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderCategorySearchTextIsValidCategoryAndOrdersFound() {
		// Setup
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		CustomerOrder customerOrder = new CustomerOrder();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);
		when(orderRepository.findByOrderCategory(category)).thenReturn(asList(customerOrder));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderDateAndValidateStringDateThrowsNullPoitnerException() {
		// Setup
		String searchText = "12/12/";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new NullPointerException("Date field cannot be empty.")).when(validationConfigurations)
				.validateStringDate(anyString());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Date field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderDateAndValidateStringDateThrowsIllegalArgumentException() {
		// Setup
		String searchText = "12/12/";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		doThrow(new IllegalArgumentException("Date must not be less then 10 characters."))
				.when(validationConfigurations).validateStringDate(anyString());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Date must not be less then 10 characters.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderDateSearchTextIsValidStringDateButOrdersAreNull() {
		// Setup
		String searchText = "30-12-2024";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringDate(searchText)).thenReturn(searchText);
		when(orderRepository.findByDate(searchText)).thenReturn(null);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with date: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderDateSearchTextIsValidStringDateButOrdersAreEmpty() {
		// Setup
		String searchText = "30-12-2024";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringDate(searchText)).thenReturn(searchText);
		when(orderRepository.findByDate(searchText)).thenReturn(Collections.emptyList());

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with date: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsOrderDateSearchTextIsValidStringDateAndOrdersFound() {
		// Setup
		String searchText = "30-12-2024";
		CustomerOrder order = new CustomerOrder();

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringDate(searchText)).thenReturn(searchText);
		when(orderRepository.findByDate(searchText)).thenReturn(asList(order));

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

	@Test
	public void searchOrderWhenSearchOptionIsNone() {
		// Setup
		String searchText = "30-12-2024";

		// Mocks
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		// Exercise
		orderController.searchOrder(searchText, OrderSearchOptions.NONE);

		// Verify
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("This operation is not allowed", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
		verifyNoMoreInteractions(orderView, workerRepository);
	}

}