package com.mycompany.orderAssignmentSystem.controller;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

// Tests for create order method
	@Test
	public void testCreateOrderMethodWhenNullCustomerOrder() {

		orderController.createNewOrder(null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerOrderIdIsNotNull() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(1l);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Unable to assign an order ID during order creation.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameIsNull() {
		CustomerOrder order = new CustomerOrder();
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerOrderIsShortStringLessThanTwoCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("a");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name must be at least 3 characters long. Please provide a valid name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameIsShortStringEqualsToTwoCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("ab");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name must be at least 3 characters long. Please provide a valid name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameIsLargeStringEqualsToTwentyCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj Naeem");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot exceed 20 characters. Please provide a shorter name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameIsLargeStringGreaterThanTwentyCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj Naeem");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot exceed 20 characters. Please provide a shorter name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateNewOrderMethodWhenCustomerNameIsLargeStringEqualsToTwentyCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj Nae";
		order.setCustomerName(customerName);
		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(customerName);
		verify(spyOrder).setCustomerName(customerName);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameWithSpecialCharacters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad@Ibtihaj");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The name cannot contain special characters. Please remove any special characters from the name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameWithNumbers() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad1Ibtihaj");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot contain numbers. Please remove any number from the name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameWithTabs() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad\tIbtihaj");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot contain tabs. Please remove any tabs from the name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameWithOneLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String actualCustomerName = " MuhammadIbtihaj";
		String expectedCustomerName = "MuhammadIbtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameWithTwoLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String actualCustomerName = "  MuhammadIbtihaj";
		String expectedCustomerName = "MuhammadIbtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameWithOneMiddleWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String actualCustomerName = "Muhammad Ibtihaj";
		String expectedCustomerName = "Muhammad Ibtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerNameWithOneEndingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String actualCustomerName = "Muhammad Ibtihaj ";
		String expectedCustomerName = "Muhammad Ibtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsNull() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsShortStringLessThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "000000";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsLongStringGreaterThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "00000000000";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateNewOrderMethodWhenCustomerPhoneNumberIsLongStringEqualsTenCharachters() {
		CustomerOrder order = new CustomerOrder();

		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerPhoneNumber()).isEqualTo(customerPhoneNumber);
		verify(spyOrder).setCustomerPhoneNumber(customerPhoneNumber);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsWithSpecialCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372@78";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsWithAlphabetCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372a78";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsWithMiddleWhiteSpaceCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372 78";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsWithLeadingWhiteSpaceCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = " 340137278";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsWithEndingWhiteSpaceCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "340137278 ";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberIsWithTabsCharachters() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "340137\t278";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerPhoneNumberWithLeadingNumberExceptThree() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "4340137278";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number must start with 3. Please provide a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsNull() {
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372678";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsWithShortStringLessThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "42 Will";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address must be at least 10 characters long. Please provide a valid Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsShortStringEqualsToTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "42 Willowa";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address must be at least 10 characters long. Please provide a valid Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsLargeStringGreaterThanFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street Near Bakary, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address cannot exceed 50 characters. Please provide a shorter Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsWithLargeStringEqualsToFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "1234 Main Street , Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(customerAddress);
		verify(spyOrder).setCustomerAddress(customerAddress);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsWithTabs() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "1234 Main Street\t Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address cannot contain tabs. Please remove any tabs from the address.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsWithOneLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String actualAddress = " 123 Main Street , Apt 101, Springfield, USA 12345";
		String expectedAddress = "123 Main Street , Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(actualAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(expectedAddress);
		verify(spyOrder).setCustomerAddress(expectedAddress);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsWithTwoLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String actualAddress = "  123 Main Street, Apt 101, Springfield, USA 12345";
		String expectedAddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(actualAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(expectedAddress);
		verify(spyOrder).setCustomerAddress(expectedAddress);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsWithOneMiddleWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "1234 Main Street , Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(address);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(address);
		verify(spyOrder).setCustomerAddress(address);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerAddressIsWithOneEndingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String actualAddress = "123 Main Street, Apt 101, Springfield, USA 12345 ";
		String expectedAddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(actualAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(expectedAddress);
		verify(spyOrder).setCustomerAddress(expectedAddress);
	}

	@Test
	public void testCreateOrderMethodWhenAppointmentDateIsNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The Date field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenAppointmentDateAsPreviousTwoDaysDate() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().minusDays(2);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date that is not before today's date.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenAppointmentDateAsPreviousOneDaysDate() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().minusDays(1);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date that is not before today's date.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDateIsWithCurrentDate() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(address);
		order.setAppointmentDate(appointmentDate);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getAppointmentDate()).isEqualTo(appointmentDate);
		verify(spyOrder).setAppointmentDate(appointmentDate);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDateIsEqualToAfterSixMonthDate() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().plusMonths(6);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(address);
		order.setAppointmentDate(appointmentDate);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getAppointmentDate()).isEqualTo(appointmentDate);
		verify(spyOrder).setAppointmentDate(appointmentDate);
	}

	@Test
	public void testCreateOrderMethodWhenAppointmentDateAsAfterSevenMonthDate() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().plusMonths(7);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date that is not 6 months after today's date.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);

		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The description field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The description field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsWithShortStringLessThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "repair";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The description must be at least 10 characters long. Please provide a valid description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsShortStringEqualsToTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "change pip";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.createNewOrder(order);

		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The description must be at least 10 characters long. Please provide a valid description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsLargeStringGreaterThanFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure the pipes are tightly sealed and all connections are leak-proof. Thank you!";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The description cannot exceed 50 characters. Please provide a shorter description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsWithLargeStringEqualsToFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection are leak-proof.Thanks";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(description);
		verify(spyOrder).setOrderDescription(description);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsWithTabs() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection \t are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);

		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The description cannot contain tabs. Please remove any tabs from the description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsWithOneLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = " Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(expectedDescription);
		verify(spyOrder).setOrderDescription(expectedDescription);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsWithTwoLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "  Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(expectedDescription);
		verify(spyOrder).setOrderDescription(expectedDescription);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsWithOneMiddleWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(actualDescription);
		verify(spyOrder).setOrderDescription(actualDescription);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerDescriptionIsWithOneEndingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof. ";
		String expectedDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(expectedDescription);
		verify(spyOrder).setOrderDescription(expectedDescription);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerCategoryIsNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The category field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerCategoryIsNotNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);

		CustomerOrder spyOrder = spy(order);
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getOrderCategory()).isEqualTo(category);
		verify(spyOrder).setOrderCategory(category);
	}

	@Test
	public void testCreateOrderMethodWhenCustomerStatusIsNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		order.setOrderCategory(category);

		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The status field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenCustomerStatusIsNotNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
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
		orderController.createNewOrder(spyOrder);
		assertThat(spyOrder.getOrderStatus()).isEqualTo(status);
		verify(spyOrder).setOrderStatus(status);
	}

	@Test
	public void testCreateOrderMethodWhenWorkerIsNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
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

		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The worker field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerIdIsNull() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
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

		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerIdIsZero() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		worker.setWorkerId(0l);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);

		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerIdIsNegativeNumber() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		worker.setWorkerId(-1l);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);

		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testCreateOrderMethodWhenWorkerIdIsPositiveNumber() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		worker.setWorkerId(1l);

		Worker spyWorker = spy(worker);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(spyWorker);

		orderController.createNewOrder(order);
		assertThat(spyWorker.getWorkerId()).isEqualTo(1l);
		verify(spyWorker).setWorkerId(1l);

	}

	@Test
	public void testCreateOrderMethodWhenOrderStatusIsNotPending() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.COMPLETED;
		Worker worker = new Worker();
		long workerId = 1l;
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
		when(workerRepository.findById(workerId)).thenReturn(worker);

		orderController.createNewOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
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
		orderController.createNewOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
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

		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.createNewOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
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

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createNewOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
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

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createNewOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
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

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createNewOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
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

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.save(order)).thenReturn(order);
		orderController.createNewOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	// tests for method update order
	@Test
	public void testUpdateOrderMethodWhenNullCustomerOrder() {
		orderController.updateOrder(null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order is null", null);
	}

	@Test
	public void testUpdateOrderMethodWhenOrderIdIsNull() {
		CustomerOrder order = new CustomerOrder();
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
	}

	@Test
	public void testUpdateOrderMethodWhenOrderIdIsZero() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(0l);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
	}

	@Test
	public void testUpdateOrderMethodWhenOrderIdIsLessThanZero() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(-1l);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
	}

	@Test
	public void testUpdateOrderMethodWhenOrderIdIsGreaterThanZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderId()).isEqualTo(orderId);
		verify(spyOrder).setOrderId(orderId);
		;
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerOrderIsShortStringLessThanTwoCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("a");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name must be at least 3 characters long. Please provide a valid name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameIsShortStringEqualsToTwoCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("ab");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name must be at least 3 characters long. Please provide a valid name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameIsLargeStringEqualsToTwentyCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj Naeem");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot exceed 20 characters. Please provide a shorter name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameIsLargeStringGreaterThanTwentyCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj Naeem");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot exceed 20 characters. Please provide a shorter name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateNewOrderMethodWhenCustomerNameIsLargeStringEqualsToTwentyCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj Nae";
		order.setCustomerName(customerName);
		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(customerName);
		verify(spyOrder).setCustomerName(customerName);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameWithSpecialCharacters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad@Ibtihaj");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The name cannot contain special characters. Please remove any special characters from the name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameWithNumbers() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad1Ibtihaj");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot contain numbers. Please remove any number from the name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameWithTabs() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad\tIbtihaj");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The name cannot contain tabs. Please remove any tabs from the name.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameWithOneLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String actualCustomerName = " MuhammadIbtihaj";
		String expectedCustomerName = "MuhammadIbtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameWithTwoLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String actualCustomerName = "  MuhammadIbtihaj";
		String expectedCustomerName = "MuhammadIbtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameWithOneMiddleWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String actualCustomerName = "Muhammad Ibtihaj";
		String expectedCustomerName = "Muhammad Ibtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerNameWithOneEndingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String actualCustomerName = "Muhammad Ibtihaj ";
		String expectedCustomerName = "Muhammad Ibtihaj";
		order.setCustomerName(actualCustomerName);
		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerName()).isEqualTo(expectedCustomerName);
		verify(spyOrder).setCustomerName(expectedCustomerName);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsShortStringLessThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "000000";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsLongStringGreaterThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "00000000000";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The phone number must be 10 characters long. Please provide a valid phone number.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateNewOrderMethodWhenCustomerPhoneNumberIsLongStringEqualsTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerPhoneNumber()).isEqualTo(customerPhoneNumber);
		verify(spyOrder).setCustomerPhoneNumber(customerPhoneNumber);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsWithSpecialCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372@78";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsWithAlphabetCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372a78";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsWithMiddleWhiteSpaceCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372 78";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsWithLeadingWhiteSpaceCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = " 340137278";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsWithEndingWhiteSpaceCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "340137278 ";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberIsWithTabsCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "340137\t278";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerPhoneNumberWithLeadingNumberExceptThree() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "4340137278";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The phone number must start with 3. Please provide a valid phone number.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		order.setCustomerName("Muhammad Ibtihaj");
		String customerPhoneNumber = "3401372678";
		order.setCustomerPhoneNumber(customerPhoneNumber);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsWithShortStringLessThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "42 Will";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address must be at least 10 characters long. Please provide a valid Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsShortStringEqualsToTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "42 Willowa";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address must be at least 10 characters long. Please provide a valid Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsLargeStringGreaterThanFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street Near Bakary, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The Address cannot exceed 50 characters. Please provide a shorter Address.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsWithLargeStringEqualsToFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "1234 Main Street , Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(customerAddress);
		verify(spyOrder).setCustomerAddress(customerAddress);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsWithTabs() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "1234 Main Street\t Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The address cannot contain tabs. Please remove any tabs from the address.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsWithOneLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String actualAddress = " 123 Main Street , Apt 101, Springfield, USA 12345";
		String expectedAddress = "123 Main Street , Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(actualAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(expectedAddress);
		verify(spyOrder).setCustomerAddress(expectedAddress);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsWithTwoLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String actualAddress = "  123 Main Street, Apt 101, Springfield, USA 12345";
		String expectedAddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(actualAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(expectedAddress);
		verify(spyOrder).setCustomerAddress(expectedAddress);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsWithOneMiddleWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "1234 Main Street , Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(address);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(address);
		verify(spyOrder).setCustomerAddress(address);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerAddressIsWithOneEndingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String actualAddress = "123 Main Street, Apt 101, Springfield, USA 12345 ";
		String expectedAddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(actualAddress);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getCustomerAddress()).isEqualTo(expectedAddress);
		verify(spyOrder).setCustomerAddress(expectedAddress);
	}

	@Test
	public void testUpdateOrderMethodWhenAppointmentDateIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The Date field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenAppointmentDateAsPreviousTwoDaysDate() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().minusDays(2);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date that is not before today's date.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenAppointmentDateAsPreviousOneDaysDate() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().minusDays(1);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date that is not before today's date.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDateIsWithCurrentDate() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(address);
		order.setAppointmentDate(appointmentDate);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getAppointmentDate()).isEqualTo(appointmentDate);
		verify(spyOrder).setAppointmentDate(appointmentDate);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDateIsEqualToAfterSixMonthDate() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().plusMonths(6);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(address);
		order.setAppointmentDate(appointmentDate);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getAppointmentDate()).isEqualTo(appointmentDate);
		verify(spyOrder).setAppointmentDate(appointmentDate);
	}

	@Test
	public void testUpdateOrderMethodWhenAppointmentDateAsAfterSevenMonthDate() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String customerAddress = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now().plusMonths(7);

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setCustomerAddress(customerAddress);
		order.setAppointmentDate(appointmentDate);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Please provide a valid date that is not 6 months after today's date.",
				order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The description field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsEmpty() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The description field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsWithShortStringLessThanTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "repair";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The description must be at least 10 characters long. Please provide a valid description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsShortStringEqualsToTenCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "change pip";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.updateOrder(order);

		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"The description must be at least 10 characters long. Please provide a valid description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsLargeStringGreaterThanFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure the pipes are tightly sealed and all connections are leak-proof. Thank you!";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The description cannot exceed 50 characters. Please provide a shorter description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsWithLargeStringEqualsToFiftyCharachters() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection are leak-proof.Thanks";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(description);
		verify(spyOrder).setOrderDescription(description);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsWithTabs() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection \t are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showError("The description cannot contain tabs. Please remove any tabs from the description.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsWithOneLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = " Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(expectedDescription);
		verify(spyOrder).setOrderDescription(expectedDescription);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsWithTwoLeadingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "  Please ensure all connection are leak-proof.";
		String expectedDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(expectedDescription);
		verify(spyOrder).setOrderDescription(expectedDescription);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsWithOneMiddleWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(actualDescription);
		verify(spyOrder).setOrderDescription(actualDescription);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerDescriptionIsWithOneEndingWhiteSpace() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof. ";
		String expectedDescription = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderDescription()).isEqualTo(expectedDescription);
		verify(spyOrder).setOrderDescription(expectedDescription);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerCategoryIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection are leak-proof.";
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The category field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerCategoryIsNotNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);

		CustomerOrder spyOrder = spy(order);
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderCategory()).isEqualTo(category);
		verify(spyOrder).setOrderCategory(category);
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerStatusIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String description = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;

		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(description);
		order.setOrderCategory(category);

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The status field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenCustomerStatusIsNotNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
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
		orderController.updateOrder(spyOrder);
		assertThat(spyOrder.getOrderStatus()).isEqualTo(status);
		verify(spyOrder).setOrderStatus(status);
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
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

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The worker field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerIdIsNull() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
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

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerIdIsZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		worker.setWorkerId(0l);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerIdIsNegativeNumber() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		worker.setWorkerId(-1l);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(worker);

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerIdIsPositiveNumber() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory category = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		Worker worker = new Worker();
		worker.setWorkerId(1l);

		Worker spyWorker = spy(worker);
		order.setCustomerName(customerName);
		order.setCustomerPhoneNumber(customerPhoneNumber);
		order.setAppointmentDate(appointmentDate);
		order.setCustomerAddress(address);
		order.setOrderDescription(actualDescription);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(spyWorker);

		orderController.updateOrder(order);
		assertThat(spyWorker.getWorkerId()).isEqualTo(1l);
		verify(spyWorker).setWorkerId(1l);

	}

	@Test
	public void testUpdateOrderMethodWhenWorkerNotFound() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		long orderId = 1l;
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
		orderController.updateOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;

		long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);
		long orderId = 1l;
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
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("Order and worker categories must align", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerIsNotChanged() {
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connections are leak-proof.";
		OrderCategory orderCategory = OrderCategory.PLUMBER;
		OrderCategory workerCategory = OrderCategory.PLUMBER;
		OrderStatus status = OrderStatus.PENDING;
		long workerId = 1L;
		long orderId = 1L;
		Worker worker = new Worker(workerId, "Worker Name", "1234567890", workerCategory);
		CustomerOrder order = new CustomerOrder(orderId, customerName, address, customerPhoneNumber, appointmentDate,
				actualDescription, orderCategory, status, worker);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, worker);
		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		orderController.updateOrder(order);
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
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		long orderId = 1l;
		order.setOrderId(orderId);
		long workerId = 1l;
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
		long savedWorkerId = 2l;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);

		when(orderRepository.modify(order)).thenReturn(order);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).modify(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersAreEmpty() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		long orderId = 1l;
		order.setOrderId(orderId);
		long workerId = 1l;
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
		long savedWorkerId = 2l;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.modify(order)).thenReturn(order);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).modify(order);
		inOrder.verify(orderView).orderModified(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersHavePendingOrder() {
		CustomerOrder order = new CustomerOrder();
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connection are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		long orderId = 1l;
		order.setOrderId(orderId);
		long workerId = 1l;
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
		long savedWorkerId = 2l;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.modify(order)).thenReturn(order);
		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError(
				"Cannot assign a new order to this worker because they already have a pending order.", order);

		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderMethodWhenWorkerOrdersHaveNoPendingOrders() {
		long orderId = 1L;
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connections are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		long workerId = 1L;

		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		CustomerOrder order = new CustomerOrder(orderId, customerName, address, customerPhoneNumber, appointmentDate,
				actualDescription, orderCategory, status, worker);

		Worker savedWorker = new Worker();
		long savedWorkerId = 2L;
		savedWorker.setWorkerId(savedWorkerId);
		savedWorker.setWorkerCategory(workerCategory);
		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, savedWorker);

		when(workerRepository.findById(workerId)).thenReturn(worker);
		when(orderRepository.findById(orderId)).thenReturn(savedOrder);
		when(orderRepository.modify(order)).thenReturn(order);

		orderController.updateOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).modify(order);
		inOrder.verify(orderView).orderModified(order);
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
	public void testFetchOrderByIdMethodWhenOrderIdIsNull() {
		CustomerOrder order = new CustomerOrder();
		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 0l;
		order.setOrderId(orderId);
		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsLessThenZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = -1l;
		order.setOrderId(orderId);
		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsGreaterThanZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		orderController.fetchOrderById(order);
		CustomerOrder spyOrder = spy(order);
		orderController.fetchOrderById(spyOrder);
		assertThat(spyOrder.getOrderId()).isEqualTo(orderId);
		verify(spyOrder).setOrderId(orderId);
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsGreaterThanZeroAndOrderNotFound() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		when(orderRepository.findById(orderId)).thenReturn(null);
		orderController.fetchOrderById(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("Order with ID " + order.getOrderId() + " not found.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testFetchOrderByIdMethodWhenOrderIdIsGreaterThanZeroAndOrderFound() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		String customerName = "Muhammad Ibtihaj";
		String customerPhoneNumber = "3401372678";
		String address = "123 Main Street, Apt 101, Springfield, USA 12345";
		LocalDateTime appointmentDate = LocalDateTime.now();
		String actualDescription = "Please ensure all connections are leak-proof.";
		OrderCategory orderCategory = OrderCategory.ELECTRICIAN;
		OrderCategory workerCategory = OrderCategory.ELECTRICIAN;
		OrderStatus status = OrderStatus.PENDING;
		long workerId = 1L;

		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setWorkerCategory(workerCategory);

		CustomerOrder savedOrder = new CustomerOrder(orderId, customerName, address, customerPhoneNumber,
				appointmentDate, actualDescription, orderCategory, status, worker);
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
	public void testDeleteOrderMethodWhenNullCustomerOrderIdIsNull() {
		CustomerOrder order = new CustomerOrder();
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be empty.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderMethodWhenNullCustomerOrderIdIsZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 0l;
		order.setOrderId(orderId);
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderMethodWhenNullCustomerOrderIdIsLessThanZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = -1l;
		order.setOrderId(orderId);
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showError("The id field cannot be less than 1. Please provide a valid id.", order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteWorkerMethodWhenWorkerIdIsGreaterThanZero() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		CustomerOrder spyOrder = spy(order);
		orderController.deleteOrder(spyOrder);
		assertThat(spyOrder.getOrderId()).isEqualTo(orderId);
		verify(spyOrder).setOrderId(orderId);
	}

	@Test
	public void testDeleteOrderMethodWhenCustomerOrderIdIsValidButNoOrderFoundWithId() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		when(orderRepository.findById(orderId)).thenReturn(null);
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showErrorNotFound("No order found with ID: " + order.getOrderId(), order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderMethodWhenCustomerOrderIdIsValidAndOrderFound() {
		CustomerOrder order = new CustomerOrder();
		long orderId = 1l;
		order.setOrderId(orderId);
		when(orderRepository.findById(orderId)).thenReturn(order);
		orderController.deleteOrder(order);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderRepository).delete(order);
		inOrder.verify(orderView).orderRemoved(order);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	// tests for search options
	@Test
	public void testSearchOrderMethodWhenSearchTextIsNull() {
		orderController.searchOrder(null, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The search Text field cannot be empty.", null);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testSearchOrderMethodWhenSearchTextIsEmpty() {
		String searchText = "";
		orderController.searchOrder(searchText, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The search Text field cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testSearchOrderMethodWhenSearchTextIsLargeStringGreaterThanTwentyCharachters() {
		String searchText = "Muhammad Ibtihaj Naeem";
		orderController.searchOrder(searchText, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The search Text cannot exceed 20 characters. Please provide a shorter search Text.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testSearchOrderMethodWhenSearchTextIsWithTabs() {
		String searchText = "Muhammad\tIbtihaj";
		orderController.searchOrder(searchText, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The search Text cannot contain tabs. Please remove any tabs from the search Text.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testSearchOrderMethodWhenSearchTextIsValidStringAndSearchOptionIsNull() {
		String searchText = "1";
		orderController.searchOrder(searchText, null);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Search option cannot be empty.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_NonInteger() {
		String searchText = "a";
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_Zero() {
		String searchText = "0";
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The id field cannot be less than 1. Please provide a valid id.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_NegativeIntegar() {
		String searchText = "-1";
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The id field cannot be less than 1. Please provide a valid id.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_ValidNumberButWithLeadingWhiteSpaces() {
		String searchText = " 1";
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showSearchError("The number cannot contains whitespace. Please provide a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_ValidNumberButWithEndingWhiteSpaces() {
		String searchText = "1 ";
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showSearchError("The number cannot contains whitespace. Please provide a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_ValidNumberButWithMiddleWhiteSpaces() {
		String searchText = "1 0";
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showSearchError("The number cannot contains whitespace. Please provide a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_ValidNumberButOrderNotFound() {
		String searchText = "1";
		long orderId = 1l;
		when(orderRepository.findById(orderId)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No result found with ID: " + orderId, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderId_SearchTextIs_ValidNumberAndOrderFound() {
		String searchText = "1";
		long orderId = 1l;
		CustomerOrder order = new CustomerOrder();
		when(orderRepository.findById(orderId)).thenReturn(order);
		orderController.searchOrder(searchText, OrderSearchOptions.ORDER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_NonInteger() {
		String searchText = "a";
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please enter a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_Zero() {
		String searchText = "0";
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The id field cannot be less than 1. Please provide a valid id.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_NegativeIntegar() {
		String searchText = "-1";
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The id field cannot be less than 1. Please provide a valid id.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButWithLeadingWhiteSpaces() {
		String searchText = " 1";
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showSearchError("The number cannot contains whitespace. Please provide a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButWithEndingWhiteSpaces() {
		String searchText = "1 ";
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showSearchError("The number cannot contains whitespace. Please provide a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButWithMiddleWhiteSpaces() {
		String searchText = "1 0";
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showSearchError("The number cannot contains whitespace. Please provide a valid number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButWorkerNotFound() {
		String searchText = "1";
		long workerId = 1l;
		when(workerRepository.findById(workerId)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No result found with ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButOrdersAreNull() {
		String searchText = "1";
		long workerId = 1l;
		Worker worker = new Worker();
		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with worker ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberButOrdersAreEmpty() {
		String searchText = "1";
		long workerId = 1l;
		Worker worker = new Worker();
		worker.setOrders(Collections.emptyList());
		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with worker ID: " + workerId, searchText);
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsWorkerId_SearchTextIs_ValidNumberAndOrderFound() {
		String searchText = "1";
		long workerId = 1l;
		Worker worker = new Worker();
		CustomerOrder order = new CustomerOrder();
		worker.setOrders(asList(order));
		when(workerRepository.findById(workerId)).thenReturn(worker);
		orderController.searchOrder(searchText, OrderSearchOptions.WORKER_ID);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(workerRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_StringLessThanTenCharacters() {

		String searchText = "000000";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The phone number must be 10 characters long. Please provide a valid phone number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_StringGreaterThanTenCharachters() {
		String searchText = "000000000000000000";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The phone number must be 10 characters long. Please provide a valid phone number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_WithAlphabetCharachters() {
		String searchText = "3401372a78";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_WithMiddleWhiteSpaceCharachters() {
		String searchText = "3401372 78";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_WithSpecialCharacters() {
		String searchText = "3401372@78";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_WithLeadingWhiteSpaceCharachters() {
		String searchText = " 340137278";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The phone number should only consist of numbers and should not contain any whitespaces, special characters, or alphabets. Please enter a valid phone number.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_WithLeadingNumberExceptThree() {
		String searchText = "4401372078";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The phone number must start with 3. Please provide a valid phone number.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_ValidPhoneNumber_ButOrdersAreNull() {
		String searchText = "3401372678";
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with phone number: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerPhoneNumber_SearchTextIs_ValidPhoneNumber_ButOrdersAreEmpty() {
		String searchText = "3401372678";
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
		when(orderRepository.findByCustomerPhoneNumber(searchText)).thenReturn(asList(order));
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_PHONE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(order));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_SearchTextIs_StringLessThanTwoCharachters() {

		String searchText = "a";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The name must be at least 3 characters long. Please provide a valid name.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_SearchTextIs_StringEqualsToTwoCharachters() {

		String searchText = "ab";
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The name must be at least 3 characters long. Please provide a valid name.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_SearchTextIs_ValidName_ButOrdersAreNull() {
		String searchText = "Muhammad";
		when(orderRepository.findByCustomerName(searchText)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with customer name: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsCustomerName_SearchTextIs_ValidName_ButOrdersAreEmpty() {
		String searchText = "Muhammad";
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
		when(orderRepository.findByCustomerName(searchText)).thenReturn(asList(customerOrder));
		orderController.searchOrder(searchText, OrderSearchOptions.CUSTOMER_NAME);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_SearchTextIs_WithWhiteSpace() {
		String searchText = " Pending ";
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The status cannot contain whitespaces. Please remove any whitespaces from the status.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_SearchTextIs_WithUnavailableStatus() {
		String searchText = "DELETED";
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("The specified status was not found. Please provide a valid status.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderStatus_SearchTextIs_ValidStatus_ButOrdersAreNull() {
		OrderStatus status = OrderStatus.PENDING;
		String searchText = status.toString();
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
		when(orderRepository.findByOrderStatus(status)).thenReturn(asList(customerOrder));
		orderController.searchOrder(searchText, OrderSearchOptions.STATUS);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_WithWhiteSpace() {
		String searchText = " Plumber ";
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError(
				"The category cannot contain whitespaces. Please remove any whitespaces from the category.",
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_WithUnavailableCategory() {
		String searchText = "Manager";
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView)
				.showSearchError("The specified category was not found. Please provide a valid category.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_ValidCategory_ButOrdersAreNull() {
		OrderCategory status = OrderCategory.PLUMBER;
		String searchText = status.toString();
		when(orderRepository.findByOrderCategory(status)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with category: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_ValidCategory_ButOrdersAreEmpty() {
		OrderCategory status = OrderCategory.PLUMBER;
		String searchText = status.toString();
		when(orderRepository.findByOrderCategory(status)).thenReturn(Collections.emptyList());

		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with category: " + searchText.toUpperCase(),
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderCategory_SearchTextIs_ValidCategory_AndOrdersFound() {
		OrderCategory status = OrderCategory.PLUMBER;
		String searchText = status.toString();
		CustomerOrder customerOrder = new CustomerOrder();
		when(orderRepository.findByOrderCategory(status)).thenReturn(asList(customerOrder));
		orderController.searchOrder(searchText, OrderSearchOptions.CATEGORY);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchResultForOrder(asList(customerOrder));
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_StringLessThenNineLength() {
		String searchText = "12/12/";
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Date must not be less then 10 characters.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_StringEqualToNineLength() {
		String searchText = "12/12/202";
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Date must not be less then 10 characters.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_StringGreaterThenNineLength() {
		String searchText = "12/12/20224";
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Date must not be greater then 10 characters.", searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_WithInValidStringDate() {
		String searchText = "12/12-2024";
		String pattern = "dd-MM-yyyy";

		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("Please ensure that the date follows the format" + pattern,
				searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_ValidStringDate_ButOrdersAreNull() {
		String searchText = "30-12-2024";
		LocalDate date = LocalDate.of(2024, 12, 30);

		when(orderRepository.findByDate(date)).thenReturn(null);
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with date: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_ValidStringDate_ButOrdersAreEmpty() {
		String searchText = "30-12-2024";
		LocalDate date = LocalDate.of(2024, 12, 30);

		when(orderRepository.findByDate(date)).thenReturn(Collections.emptyList());
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);
		inOrder.verify(orderView).showSearchError("No orders found with date: " + searchText, searchText);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void searchOrder_WhenSearchOptionIsOrderDate_SearchTextIs_ValidStringDate_AndOrdersFound() {
		String searchText = "30-12-2024";
		LocalDate date = LocalDate.of(2024, 12, 30);
		CustomerOrder order = new CustomerOrder();
		when(orderRepository.findByDate(date)).thenReturn(asList(order));
		orderController.searchOrder(searchText, OrderSearchOptions.DATE);
		InOrder inOrder = Mockito.inOrder(orderView, orderRepository, workerRepository);

		inOrder.verify(orderView).showSearchResultForOrder(asList(order));

		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}
}
