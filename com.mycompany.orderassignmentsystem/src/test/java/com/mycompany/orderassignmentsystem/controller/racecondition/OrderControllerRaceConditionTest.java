/*
 * Unit tests for the OrderController class focused on race conditions.
 *
 * These tests verify the functionality of the OrderController in concurrent
 * environments, ensuring that the application handles race conditions properly
 * when multiple threads are accessing and modifying order data simultaneously.
 * The tests utilise Mockito for mocking dependencies and Awaitility for 
 * handling asynchronous operations.
 *
 * The methods tested include:
 * - createOrUpdateOrder() for concurrent creation of orders.
 * - deleteOrder() for concurrent deletion of orders.
 *
 * Each test follows a structured approach with three main phases:
 * 1. Setup: Created environment for the test.
 * 2. Mocks: Configuring the mock objects (Added separate comment just for better readability).
 * 3. Exercise: Calling an instance method.
 * 4. Verify: Verify that the outcome matches the expected behaviour.
 *
 * The setup and teardown methods handle the initialisation and cleanup of mock objects.
 *
 * @see OrderController
 * @see OrderRepository
 * @see WorkerRepository
 * @see OrderView
 * @see ValidationConfigurations
 */

package com.mycompany.orderassignmentsystem.controller.racecondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

import com.mycompany.orderassignmentsystem.controller.OrderController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.view.OrderView;

/**
 * The Class OrderControllerRaceConditionTest.
 */
public class OrderControllerRaceConditionTest {

	/** The order repository. */
	@Mock
	private OrderRepository orderRepository;

	/** The order view. */
	@Mock
	private OrderView orderView;

	/** The worker repository. */
	@Mock
	private WorkerRepository workerRepository;

	/** The validation configurations. */
	@Mock
	private ValidationConfigurations validationConfigurations;

	/** The order controller. */
	@InjectMocks
	private OrderController orderController;

	/** The closeable. */
	private AutoCloseable closeable;
	/** The order id. */
	private long ORDER_ID = 1l;

	/** The customer name. */
	private String CUSTOMER_NAME = "Muhammad Ibtihaj";

	/** The customer phone. */
	private String CUSTOMER_PHONE = "3401372678";

	/** The customer address. */
	private String CUSTOMER_ADDRESS = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date. */
	private String ORDER_APPOINTMENT_DATE = "12-12-2024";

	/** The order description. */
	private String ORDER_DESCRIPTION = "Please ensure all connection are leak-proof.Thanks";

	/** The order category. */
	private OrderCategory ORDER_CATEGORY = OrderCategory.PLUMBER;

	/** The order status. */
	private OrderStatus ORDER_STATUS = OrderStatus.PENDING;

	/** The worker. */
	private Worker WORKER = new Worker(1l, "Alic", "3401372676", ORDER_CATEGORY);

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
	 * Test new order concurrent.
	 */
	@Test
	public void testNewOrderConcurrent() {
		// Setup
		List<CustomerOrder> orders = new ArrayList<>();
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS, ORDER_APPOINTMENT_DATE,
				ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, WORKER);

		// Mocks
		when(workerRepository.findById(anyLong())).thenAnswer(invocation -> WORKER);
		doAnswer(invocation -> {
			orders.add(order);
			WORKER.setOrders(orders);
			return order;
		}).when(orderRepository).save(any(CustomerOrder.class));

		// Exercise
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> orderController.createOrUpdateOrder(order, OperationType.ADD)))
				.peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));

		// Verify
		assertThat(orders).containsExactly(order);
	}

	/**
	 * Test delete order concurrent.
	 */
	@Test
	public void testDeleteOrderConcurrent() {
		// Setup
		List<CustomerOrder> orders = new ArrayList<>();

		CustomerOrder order = new CustomerOrder(ORDER_ID, CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS,
				ORDER_APPOINTMENT_DATE, ORDER_DESCRIPTION, ORDER_CATEGORY, ORDER_STATUS, WORKER);

		// Mocks
		when(orderRepository.findById(anyLong())).thenAnswer(invocation -> orders.stream().findFirst().orElse(null));

		doAnswer(invocation -> {
			orders.remove(order);
			return null;
		}).when(orderRepository).delete(any(CustomerOrder.class));

		// Exercise
		orders.add(order);
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> orderController.deleteOrder(order))).peek(t -> t.start())
				.collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));

		// Verify
		assertThat(orders).isEmpty();

	}
}
