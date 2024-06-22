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

public class OrderControllerRaceConditionTest {

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

	@Test
	public void testNewOrderConcurrent() {
		List<CustomerOrder> orders = new ArrayList<>();
		String customerPhoneNumber = "3401372678";
		OrderCategory category = OrderCategory.PLUMBER;
		long workerId = 1l;
		Worker worker = new Worker(workerId, "Alic", customerPhoneNumber, category);
		String customerName = "Jhon";
		String customerAddress = "Piazza Luigi Dalla";
		String appointmentDate = "12-12-2024";
		String orderDescription = "No description";
		OrderStatus status = OrderStatus.PENDING;
		CustomerOrder order = new CustomerOrder(customerName, customerAddress, customerPhoneNumber, appointmentDate,
				orderDescription, category, status, worker);

		when(validationConfigurations.validateName(anyString())).thenReturn(customerName);
		when(validationConfigurations.validatePhoneNumber(anyString())).thenReturn(customerPhoneNumber);
		when(validationConfigurations.validateAddress(anyString())).thenReturn(customerAddress);
		when(validationConfigurations.validateStringDate(anyString())).thenReturn(appointmentDate);
		when(validationConfigurations.validateDescription(anyString())).thenReturn(orderDescription);
		when(validationConfigurations.validateCategory(category)).thenReturn(category);
		when(validationConfigurations.validateStatus(status)).thenReturn(status);
		when(validationConfigurations.validateStringNumber(anyString())).thenReturn(workerId);
		when(workerRepository.findById(anyLong())).thenAnswer(invocation -> worker);

		doAnswer(invocation -> {
			orders.add(order);
			worker.setOrders(orders);
			return order;
		}).when(orderRepository).save(any(CustomerOrder.class));
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> orderController.createOrUpdateOrder(order, OperationType.ADD)))
				.peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		assertThat(orders).containsExactly(order);
	}

	@Test
	public void testDeleteOrderConcurrent() {
		List<CustomerOrder> orders = new ArrayList<>();
		Long workerId = 1l;
		String customerPhoneNumber = "3401372678";
		OrderCategory category = OrderCategory.PLUMBER;
		Worker worker = new Worker(workerId, "Alic", customerPhoneNumber, category);
		Long orderId = 1l;
		String customerName = "Jhon";
		String customerAddress = "Piazza Luigi Dalla";
		String appointmentDate = "12-12-2024";
		String orderDescription = "No description";
		OrderStatus status = OrderStatus.PENDING;
		CustomerOrder order = new CustomerOrder(orderId, customerName, customerAddress, customerPhoneNumber,
				appointmentDate, orderDescription, category, status, worker);
		when(validationConfigurations.validateStringNumber(anyString())).thenReturn(orderId);
		when(orderRepository.findById(anyLong())).thenAnswer(invocation -> orders.stream().findFirst().orElse(null));

		doAnswer(invocation -> {
			orders.remove(order);
			return null;
		}).when(orderRepository).delete(any(CustomerOrder.class));

		orders.add(order);

		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> orderController.deleteOrder(order))).peek(t -> t.start())
				.collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		assertThat(orders).isEmpty();

	}
}
