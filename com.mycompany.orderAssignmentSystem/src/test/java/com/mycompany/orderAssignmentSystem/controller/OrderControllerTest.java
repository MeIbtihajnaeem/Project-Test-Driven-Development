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
import com.mycompany.orderAssignmentSystem.enumerations.OrderSearchOptions;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.view.OrderView;

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

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	// Tests for show all orders
	@Test
	public void testAllOrdersMethodWhenCustomer() {
		List<CustomerOrder> orders = asList(new CustomerOrder());
		when(orderRepository.findAll()).thenReturn(orders);
		orderController.allOrders();
		verify(orderView).showAllOrder(orders);
	}

	@Test
	public void testAllOrdersMethodWhenEmptyList() {
		when(orderRepository.findAll()).thenReturn(Collections.emptyList());
		orderController.allOrders();
		verify(orderView).showAllOrder(Collections.emptyList());
	}

	@Test
	public void testAllOrdersMethodWhenNullList() {
		when(orderRepository.findAll()).thenReturn(null);
		orderController.allOrders();
		verify(orderView).showAllOrder(null);
	}

	// Tests for show all workers
	@Test
	public void testAllWorkersMethodWhenW() {
		List<Worker> workers = asList(new Worker());
		when(workerRepository.findAll()).thenReturn(workers);
		orderController.allWorkers();
		verify(orderView).showAllWorkers(workers);
	}

	@Test
	public void testAllWorkersMethodWhenEmptyList() {
		when(workerRepository.findAll()).thenReturn(Collections.emptyList());
		orderController.allWorkers();
		verify(orderView).showAllWorkers(Collections.emptyList());
	}

	@Test
	public void testAllWorkersMethodWhenNullList() {
		when(workerRepository.findAll()).thenReturn(null);
		orderController.allWorkers();
		verify(orderView).showAllWorkers(null);
	}

	// Tests for create order method
	@Test
	public void testCreateOrderMethodWhenNullOperationType() {
		orderController.createOrUpdateOrder(null, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Operation Type is null", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenNullCustomerOrder() {
		orderController.createOrUpdateOrder(null, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateNameThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("");
		doThrow(new NullPointerException("The name field cannot be empty.")).when(validationConfigurations)
				.validateName(anyString());
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateNameThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad@Ibtihaj");
		doThrow(new IllegalArgumentException("The name field cannot contain Special characters."))
				.when(validationConfigurations).validateName(anyString());
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot contain Special characters.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateNewOrderMethodWhenValidateNameReturnsValidName() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj Nae";
		order.setCustomerName(customerName);
		CustomerOrder spyOrder = spy(order);
		when(validationConfigurations.validateName(customerName)).thenReturn(customerName);
		orderController.createOrUpdateOrder(spyOrder, OperationType.ADD);
		assertThat(spyOrder.getCustomerName()).isEqualTo(customerName);
		verify(spyOrder).setCustomerName(customerName);
	}

	@Test
	public void testCreateOrderMethodWhenValidatePhoneNumberThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerName(customerName);
		doThrow(new NullPointerException("The phone number field cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(anyString());
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidatePhoneNumberThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "000000";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerName(customerName);
		doThrow(new IllegalArgumentException(
				"The phone number must be 10 characters long. Please provide a valid phone number."))
				.when(validationConfigurations).validatePhoneNumber(anyString());
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateNewOrderMethodWhenValidatePhoneNumberReturnsValidPhoneNumber() {
		CustomerOrder order = new CustomerOrder();

		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);

		CustomerOrder spyOrder = spy(order);
		when(validationConfigurations.validatePhoneNumber(customerPhoneNumber)).thenReturn(customerPhoneNumber);
		orderController.createOrUpdateOrder(spyOrder, OperationType.ADD);
		assertThat(spyOrder.getCustomerPhoneNumber()).isEqualTo(customerPhoneNumber);
		verify(spyOrder).setCustomerPhoneNumber(customerPhoneNumber);
	}

	@Test
	public void testCreateOrderMethodWhenValidateAddressThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		doThrow(new NullPointerException("The address field cannot be empty.")).when(validationConfigurations)
				.validateAddress(anyString());

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateAddressThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		doThrow(new IllegalArgumentException(
				"The Address must be at least 10 characters long. Please provide a valid Address."))
				.when(validationConfigurations).validateAddress(anyString());

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address must be at least 10 characters long. Please provide a valid Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateAddressReturnsValidAddress() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "1234 Main Street , Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);

		CustomerOrder spyOrder = spy(order);

		when(validationConfigurations.validateAddress(customerAddress)).thenReturn(customerAddress);

		orderController.createOrUpdateOrder(spyOrder, OperationType.ADD);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(customerAddress);
		verify(spyOrder).setCustomerAddress(customerAddress);
	}

	@Test
	public void testCreateOrderMethodWhenValidateDateThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		doThrow(new NullPointerException("Date field cannot be empty.")).when(validationConfigurations)
				.validateStringDate(anyString());

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Date field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateDateThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		doThrow(new IllegalArgumentException("Please provide a valid date that is not before today's date."))
				.when(validationConfigurations).validateStringDate(anyString());

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date that is not before today's date.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateDateReturnsValidDate() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(address);
		order.setAppointmentDate(appointmentDate);

		CustomerOrder spyOrder = spy(order);

		when(validationConfigurations.validateStringDate(appointmentDate)).thenReturn(appointmentDate);
		orderController.createOrUpdateOrder(spyOrder, OperationType.ADD);
		assertThat(spyOrder.getAppointmentDate()).isEqualTo(appointmentDate);
		verify(spyOrder).setAppointmentDate(appointmentDate);
	}

	@Test
	public void testCreateOrderMethodWhenValidateDescriptionThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String description = "";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);

		doThrow(new NullPointerException("The description field cannot be empty.")).when(validationConfigurations)
				.validateDescription(anyString());

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The description field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateDescriptionThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String description = "";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		doThrow(new IllegalArgumentException(
				"The description must be at least 10 characters long. Please provide a valid description."))
				.when(validationConfigurations).validateDescription(anyString());

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The description must be at least 10 characters long. Please provide a valid description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateDescriptionReturnsValidDescription() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String description = "Please ensure all connection are leak-proof.Thanks";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		CustomerOrder spyOrder = spy(order);

		when(validationConfigurations.validateDescription(description)).thenReturn(description);
		orderController.createOrUpdateOrder(spyOrder, OperationType.ADD);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(description);
		verify(spyOrder).setOrderDescription(description);
	}

	@Test
	public void testCreateOrderMethodWhenValidateCategoryThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String description = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		order.setOrderCategory(category);
		doThrow(new NullPointerException("The category field cannot be empty.")).when(validationConfigurations)
				.validateCategory(any(OrderCategory.class));

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The category field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateCategoryThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String description = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		order.setOrderCategory(category);
		doThrow(new IllegalArgumentException("The category field does not match.")).when(validationConfigurations)
				.validateCategory(any(OrderCategory.class));
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The category field does not match.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateCategoryReturnsValidCategory() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);

		CustomerOrder spyOrder = spy(order);
		when(validationConfigurations.validateCategory(category)).thenReturn(category);

		orderController.createOrUpdateOrder(spyOrder, OperationType.ADD);
		assertThat(spyOrder.getOrderCategory()).isEqualTo(category);
		verify(spyOrder).setOrderCategory(category);
	}

	@Test
	public void testCreateOrderMethodWhenValidateStatusThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String description = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		order.setOrderCategory(category);
		doThrow(new NullPointerException("The status field cannot be empty.")).when(validationConfigurations)
				.validateStatus(any());
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The status field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateStatusThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String description = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		order.setOrderCategory(category);
		order.setOrderStatus(OrderStatus.PENDING);
		doThrow(new IllegalArgumentException("The status field does not match.")).when(validationConfigurations)
				.validateStatus(any(OrderStatus.class));
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The status field does not match.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenValidateStatusReturnsValidStatus() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);

		CustomerOrder spyOrder = spy(order);

		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		orderController.createOrUpdateOrder(spyOrder, OperationType.ADD);
		assertThat(spyOrder.getOrderStatus()).isEqualTo(status);
		verify(spyOrder).setOrderStatus(status);
	}

	@Test
	public void testCreateOrderMethodWhenWorkerIsNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The worker field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenvalidateStringNumberThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenvalidateStringNumberThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenvalidateStringNumberReturnsValidWorkerId() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);

		Worker spyWorker = spy(worker);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(spyWorker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		when(validationConfigurations.validateCategory(category)).thenReturn(category);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		assertThat(spyWorker.getWorkerId()).isEqualTo(workerId);
		verify(spyWorker).setWorkerId(workerId);

	}

	@Test
	public void testCreateOrderMethodWhenCustomerOrderIdIsNotNullForOperationAdd() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		Long orderId = 1l;
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		order.setOrderId(orderId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Unable to assign an order ID during order creation.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenOrderStatusIsNotPending() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.COMPLETED;
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(category);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);

		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		when(validationConfigurations.validateCategory(category)).thenReturn(category);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		when(workerRepository.findById(workerId)).thenReturn(worker);

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The order status should be initiated with 'pending' status.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerNotFound() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);

		when(workerRepository.findById(workerId)).thenReturn(null);

		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		when(validationConfigurations.validateCategory(workerCategory)).thenReturn(workerCategory);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("Worker with this ID " + worker.getWorkerId() + " not found",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerAndOrderCategoryNotAligned() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		when(validationConfigurations.validateCategory(workerCategory)).thenReturn(workerCategory);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order and worker categories must align", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerOrdersAreNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		when(validationConfigurations.validateCategory(workerCategory)).thenReturn(workerCategory);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerOrdersAreEmpty() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		worker.setOrders(Collections.emptyList());
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(validationConfigurations.validateCategory(workerCategory)).thenReturn(workerCategory);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerOrdersHavePendingOrder() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		CustomerOrder workerOrder = new CustomerOrder();
		workerOrder.setOrderStatus(OrderStatus.PENDING);
		worker.setOrders(asList(workerOrder));
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		when(validationConfigurations.validateCategory(workerCategory)).thenReturn(workerCategory);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order);

		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerOrdersHaveNoPendingOrders() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		CustomerOrder workerOrder = new CustomerOrder();
		workerOrder.setOrderStatus(OrderStatus.COMPLETED);
		worker.setOrders(asList(workerOrder));
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);

		when(validationConfigurations.validateCategory(workerCategory)).thenReturn(workerCategory);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createOrUpdateOrder(order, OperationType.ADD);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	// Update

	@Test
	public void testCreateOrderMethodWhenOperationUpdateAndvalidateStringNumberThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenOperationUpdateAndvalidateStringNumberThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);
		order.setOrderId(0l);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenvalidateStringNumberReturnsValidIdForOperationUpdate() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		Long orderId = 1l;
		Long workerId = 2l;
		worker.setWorkerId(workerId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);
		order.setOrderId(orderId);
		CustomerOrder spyOrder = spy(order);

		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.createOrUpdateOrder(spyOrder, OperationType.UPDATE);
		assertThat(spyOrder.getOrderId()).isEqualTo(orderId);
		verify(spyOrder).setOrderId(orderId);
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerNotFound() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		Long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);

		when(workerRepository.findById(workerId)).thenReturn(null);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("Worker with this ID " + worker.getWorkerId() + " not found",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerAndOrderCategoryNotAligned() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		Long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order and worker categories must align", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerIsNotChanged() {
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connections are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Long workerId = 1l;
		Long orderId = 1l;
		Worker worker = new Worker(workerId, "Worker Name", "1234567890", workerCategory);
		CustomerOrder order = new CustomerOrder(orderId, customerName, address, customerPhoneNumber, appointmentDate,
				actualDescription, orderCategory, status, worker);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(validationConfigurations.validateCategory(orderCategory)).thenReturn(orderCategory);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);

		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Cannot update order because it is assigned to the same worker.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersAreNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		Long orderId = 1l;
		order.setOrderId(orderId);
		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);

		Worker savedWorker = new Worker();
		Long savedWorkerId = 2l;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.save(order)).thenReturn(order);
		when(validationConfigurations.validateCategory(orderCategory)).thenReturn(orderCategory);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersAreEmpty() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		Long orderId = 1l;
		order.setOrderId(orderId);
		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		worker.setOrders(Collections.emptyList());
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);

		Worker savedWorker = new Worker();
		Long savedWorkerId = 2l;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.save(order)).thenReturn(order);
		when(validationConfigurations.validateCategory(orderCategory)).thenReturn(orderCategory);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersHavePendingOrder() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		Long orderId = 1l;
		order.setOrderId(orderId);
		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		CustomerOrder workerOrder = new CustomerOrder();
		workerOrder.setOrderStatus(OrderStatus.PENDING);
		worker.setOrders(asList(workerOrder));

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(orderCategory);
		order.setOrderStatus(status);
		order.setWorker(worker);
		Worker savedWorker = new Worker();
		Long savedWorkerId = 2l;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.save(order)).thenReturn(order);
		when(validationConfigurations.validateCategory(orderCategory)).thenReturn(orderCategory);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order);

		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersHaveNoPendingOrders() {
		Long orderId = 1l;
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connections are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		Long workerId = 1l;

		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		CustomerOrder order = new CustomerOrder(orderId, customerName, address, customerPhoneNumber, appointmentDate,
				actualDescription, orderCategory, status, worker);

		Worker savedWorker = new Worker();
		Long savedWorkerId = 2l;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.save(order)).thenReturn(order);
		when(validationConfigurations.validateCategory(orderCategory)).thenReturn(orderCategory);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.createOrUpdateOrder(order, OperationType.UPDATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrUpdateOrderMethodWhenOperationTypeNone() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		Long orderId = 1l;
		Long workerId = 1l;
		worker.setWorkerId(workerId);
		order.setOrderId(orderId);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);
		orderController.createOrUpdateOrder(order, OperationType.NONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("This operation is not allowed here", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	// tests for fetch by order id method

	@Test
	public void testFetchOrderByIdMethodWhenOrderIsNull() {
		orderController.fetchOrderById(null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null.", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenvalidateStringNumberThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();

		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());
		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenvalidateStringNumberThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(0l);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenvalidateStringNumberReturnsValidId() {
		CustomerOrder order = new CustomerOrder();
		Long orderId = 1l;
		order.setOrderId(orderId);
		CustomerOrder spyOrder = spy(order);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.fetchOrderById(spyOrder);
		assertThat(spyOrder.getOrderId()).isEqualTo(orderId);
		verify(spyOrder).setOrderId(orderId);
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsGreaterThanZeroAndOrderNotFound() {
		CustomerOrder order = new CustomerOrder();
		Long orderId = 1l;
		order.setOrderId(orderId);
		when(orderRepository.findById(orderId)).thenReturn(null);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);

		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("Order with ID " + order.getOrderId() + " not found.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsGreaterThanZeroAndOrderFound() {
		CustomerOrder order = new CustomerOrder();
		Long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		String appointmentDate = "12-12-2024";

		String actualDescription = "Please ensure all connections are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		Long workerId = 1l;

		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, worker);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);

		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showFetchedOrder(savedOrder);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	// Tests for delete order

	@Test
	public void testDeleteOrderMethodWhenNullCustomerOrder() {

		orderController.deleteOrder(null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderMethodWhenvalidateStringNumberThrowsNullPointerException() {
		CustomerOrder order = new CustomerOrder();
		doThrow(new NullPointerException("The id field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderMethodWhenvalidateStringNumberThrowsIllegalArgumentException() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(0l);
		doThrow(new IllegalArgumentException("The id field cannot be less than 1. Please provide a valid id."))
				.when(validationConfigurations).validateStringNumber(anyString());
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderMethodWhenvalidateStringNumberReturnsValidId() {
		CustomerOrder order = new CustomerOrder();
		Long orderId = 1l;
		order.setOrderId(orderId);
		CustomerOrder spyOrder = spy(order);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		orderController.deleteOrder(spyOrder);
		assertThat(spyOrder.getOrderId()).isEqualTo(orderId);
		verify(spyOrder).setOrderId(orderId);
	}

	@Test
	public void testDeleteOrderMethodWhenCustomerOrderIdIsValidButNoOrderFoundWithId() {
		CustomerOrder order = new CustomerOrder();
		Long orderId = 1l;
		order.setOrderId(orderId);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);

		when(orderRepository.findById(orderId)).thenReturn(null);
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("No order found with ID: " + order.getOrderId(), order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderMethodWhenCustomerOrderIdIsValidAndOrderFound() {
		CustomerOrder order = new CustomerOrder();
		Long orderId = 1l;
		order.setOrderId(orderId);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		when(orderRepository.findById(orderId)).thenReturn(order);
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).delete(order);
		inOrder.verify(orderView).orderRemoved(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	// tests for search options

	@Test
	public void testSearchOrderMethodWhenValidateSearchThrowsNullPointerException() {
		doThrow(new NullPointerException("The search Text field cannot be empty.")).when(validationConfigurations)
				.validateSearchString(any());
		orderController.searchOrder(null, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The search Text field cannot be empty.", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testSearchOrderMethodWhenValidateSearchThrowsIllegalArgumentException() {
		String searchText = "Muhammad\tIbtihaj";
		doThrow(new IllegalArgumentException(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text."))
				.when(validationConfigurations).validateSearchString(anyString());
		orderController.searchOrder(searchText, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testSearchOrderMethodWhenSearchTextIsValidStringAndSearchOptionIsNull() {
		String searchText = "1";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Search option cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_AndValidateStringNumber_Throws_NullPointerException() {
		String searchText = "";
		doThrow(new NullPointerException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_AndValidateStringNumber_Throws_IllegalArgumentException() {
		String searchText = "a";
		doThrow(new IllegalArgumentException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_AndvalidateStringNumber_Throws_NullPointerException() {
		String searchText = "1";
		doThrow(new NullPointerException("Id Field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
//		when(validationConfigurations.validateStringNumber(searchText)).thenReturn("1");
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_AndvalidateStringNumber_Throws_IllegalArgumentException() {
		String searchText = "0";
		doThrow(new IllegalArgumentException("Id Field cannot be zero.")).when(validationConfigurations)
				.validateStringNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
//		when(validationConfigurations.validateStringNumber(searchText)).thenReturn("0");
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be zero.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_ValidNumberButOrderNotFound() {
		String searchText = "1";
		Long orderId = 1l;
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(orderId);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		when(orderRepository.findById(orderId)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No result found with ID: " + orderId, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_ValidNumberAndOrderFound() {
		String searchText = "1";
		Long orderId = 1l;
		CustomerOrder order = new CustomerOrder();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(orderId);
		when(validationConfigurations.validateStringNumber(orderId.toString())).thenReturn(orderId);
		when(orderRepository.findById(orderId)).thenReturn(order);
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_AndValidateStringNumber_Throws_NullPointerException() {
		String searchText = "";
		doThrow(new NullPointerException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_AndValidateStringNumber_Throws_IllegalArgumentException() {
		String searchText = "a";
		doThrow(new IllegalArgumentException("Please enter a valid number.")).when(validationConfigurations)
				.validateStringNumber(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_AndvalidateStringNumber_Throws_NullPointerException() {
		String searchText = "1";
		doThrow(new NullPointerException("Id Field cannot be empty.")).when(validationConfigurations)
				.validateStringNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
//		when(validationConfigurations.validateStringNumber(searchText)).thenReturn("1");
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_AndvalidateStringNumber_Throws_IllegalArgumentException() {
		String searchText = "0";
		doThrow(new IllegalArgumentException("Id Field cannot be zero.")).when(validationConfigurations)
				.validateStringNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
//		when(validationConfigurations.validateStringNumber(searchText)).thenReturn("0");
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Id Field cannot be zero.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButWorkerNotFound() {
		String searchText = "1";
		Long workerId = 1l;
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(null);

		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No result found with ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButOrdersAreNull() {
		String searchText = "1";
		Long workerId = 1l;
		Worker worker = new Worker();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with worker ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButOrdersAreEmpty() {
		String searchText = "1";
		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setOrders(Collections.emptyList());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with worker ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberAndOrderFound() {
		String searchText = "1";
		Long workerId = 1l;
		Worker worker = new Worker();
		CustomerOrder order = new CustomerOrder();
		worker.setOrders(asList(order));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringNumber(searchText)).thenReturn(workerId);
		when(validationConfigurations.validateStringNumber(workerId.toString())).thenReturn(workerId);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_And_ValidatePhoneNumber_Throws_NullPointerException() {

		String searchText = "000000";
		doThrow(new NullPointerException("Phone Number cannot be empty.")).when(validationConfigurations)
				.validatePhoneNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Phone Number cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_And_ValidatePhoneNumber_Throws_IllegalArgumentException() {

		String searchText = "000000";
		doThrow(new IllegalArgumentException("Phone Number cannot be less then 10 characters."))
				.when(validationConfigurations).validatePhoneNumber(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Phone Number cannot be less then 10 characters.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_ValidPhoneNumber_ButOrdersAreNull() {
		String searchText = "3401372678";

		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with phone number: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_ValidPhoneNumber_ButOrdersAreEmpty() {
		String searchText = "3401372678";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(Collections.emptyList());
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with phone number: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_ValidPhoneNumber() {
		String searchText = "3401372678";
		CustomerOrder order = new CustomerOrder();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validatePhoneNumber(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(asList(order));
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_And_ValidateName_ThrowsNullPointerException() {

		String searchText = "a";
		doThrow(new NullPointerException("Name field cannot be empty.")).when(validationConfigurations)
				.validateName(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Name field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_And_ValidateName_IllegalArgumentException() {

		String searchText = "a";
		doThrow(new IllegalArgumentException(
				"The name must be at least 3 characters long. Please provide a valid name."))
				.when(validationConfigurations).validateName(any());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The name must be at least 3 characters long. Please provide a valid name.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_SearchTextIs_ValidName_ButOrdersAreNull() {
		String searchText = "Muhammad";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);

		when(orderRepository.findByCustomerName(searchText)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with customer name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_SearchTextIs_ValidName_ButOrdersAreEmpty() {
		String searchText = "Muhammad";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerName(searchText)).thenReturn(Collections.emptyList());
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with customer name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_SearchTextIs_ValidName_OrderFound() {
		String searchText = "Muhammad";
		CustomerOrder customerOrder = new CustomerOrder();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateName(searchText)).thenReturn(searchText);
		when(orderRepository.findByCustomerName(searchText)).thenReturn(asList(customerOrder));
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_And_ValidateEnum_Throws_NullPointerException() {
		String searchText = " Pending ";
		doThrow(new NullPointerException("Status field cannot be empty.")).when(validationConfigurations)
				.validateEnum(any(), eq(OrderStatus.class));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Status field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_And_ValidateEnum_Throws_IllegalArgumentException() {
		String searchText = " Pending ";
		doThrow(new IllegalArgumentException(
				"The status cannot contain whitespaces. Please remove any whitespaces from the status."))
				.when(validationConfigurations).validateEnum(anyString(), eq(OrderStatus.class));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The status cannot contain whitespaces. Please remove any whitespaces from the status.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_SearchTextIs_ValidStatus_ButOrdersAreNull() {
		OrderStatus status = OrderStatus.PENDING;
		String searchText = status.toString();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderStatus.class)).thenReturn(status);

		when(orderRepository.findByOrderStatus(status)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with status: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_SearchTextIs_ValidStatus_ButOrdersAreEmpty() {
		OrderStatus status = OrderStatus.PENDING;
		String searchText = status.toString();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderStatus.class)).thenReturn(status);
		when(orderRepository.findByOrderStatus(status)).thenReturn(Collections.emptyList());
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with status: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_SearchTextIs_ValidStatus_AndOrdersFound() {
		OrderStatus status = OrderStatus.PENDING;
		String searchText = status.toString();
		CustomerOrder customerOrder = new CustomerOrder();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderStatus.class)).thenReturn(status);
		when(orderRepository.findByOrderStatus(status)).thenReturn(asList(customerOrder));
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_And_ValidateEnum_Throws_NullPointerException() {
		String searchText = " Plumber ";
		doThrow(new NullPointerException("category field cannot be empty.")).when(validationConfigurations)
				.validateEnum(any(), eq(OrderCategory.class));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("category field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_And_ValidateEnum_Throws_IllegalArgumentException() {
		String searchText = " Plumber ";
		doThrow(new IllegalArgumentException(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category."))
				.when(validationConfigurations).validateEnum(any(), eq(OrderCategory.class));
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_ValidCategory_ButOrdersAreNull() {
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		when(orderRepository.findByOrderCategory(category)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with category: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_ValidCategory_ButOrdersAreEmpty() {
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		when(orderRepository.findByOrderCategory(category)).thenReturn(Collections.emptyList());

		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with category: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_ValidCategory_AndOrdersFound() {
		OrderCategory category = OrderCategory.PLUMBER;
		String searchText = category.toString();
		CustomerOrder customerOrder = new CustomerOrder();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateEnum(searchText, OrderCategory.class)).thenReturn(category);

		when(orderRepository.findByOrderCategory(category)).thenReturn(asList(customerOrder));
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_And_ValidateStringDate_Throws_NullPoitnerException() {
		String searchText = "12/12/";
		doThrow(new NullPointerException("Date field cannot be empty.")).when(validationConfigurations)
				.validateStringDate(anyString());

		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);

		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Date field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_And_ValidateStringDate_Throws_IllegalArgumentException() {
		String searchText = "12/12/";
		doThrow(new IllegalArgumentException("Date must not be less then 10 characters."))
				.when(validationConfigurations).validateStringDate(anyString());
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Date must not be less then 10 characters.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_ValidStringDate_ButOrdersAreNull() {
		String searchText = "30-12-2024";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringDate(searchText)).thenReturn(searchText);

		when(orderRepository.findByDate(searchText)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with date: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_ValidStringDate_ButOrdersAreEmpty() {
		String searchText = "30-12-2024";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringDate(searchText)).thenReturn(searchText);
		when(orderRepository.findByDate(searchText)).thenReturn(Collections.emptyList());
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with date: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_ValidStringDate_AndOrdersFound() {
		String searchText = "30-12-2024";
		CustomerOrder order = new CustomerOrder();
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		when(validationConfigurations.validateStringDate(searchText)).thenReturn(searchText);
		when(orderRepository.findByDate(searchText)).thenReturn(asList(order));
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);

		inOrder.verify(orderView).showSearchResultForOrder(asList(order));

		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsNone() {
		String searchText = "30-12-2024";
		when(validationConfigurations.validateSearchString(searchText)).thenReturn(searchText);
		orderController.searchOrder(searchText, OrderSearchOptions.None);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("This operation is not allowed", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

}