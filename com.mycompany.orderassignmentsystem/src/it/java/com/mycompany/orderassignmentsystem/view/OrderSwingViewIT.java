package com.mycompany.orderassignmentsystem.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycompany.orderassignmentsystem.DatabaseConfig;
import com.mycompany.orderassignmentsystem.configurations.DBConfig;
import com.mycompany.orderassignmentsystem.controller.OrderController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.swing.OrderSwingView;

@RunWith(GUITestRunner.class)

public class OrderSwingViewIT extends AssertJSwingJUnitTestCase {

	private OrderRepository orderRepository;

	private OrderSwingView orderSwingView;

	private WorkerRepository workerRepository;
	private ValidationConfigurations validatedConfig;

	private OrderController orderController;

	private FrameFixture window;
	private EntityManagerFactory entityManagerFactory;

	/**
	 * This variable is responsible for starting the Docker container. If the test
	 * is run from Eclipse, it runs the Docker container using Testcontainers. If
	 * the test is run using a Maven command, it starts a real Docker container.
	 */
	private static DBConfig databaseConfig;

	@BeforeClass
	public static void setupServer() {
		databaseConfig = DatabaseConfig.getDatabaseConfig();

		databaseConfig.testAndStartDatabaseConnection();
	}

	@Override
	protected void onSetUp() throws Exception {
		entityManagerFactory = databaseConfig.getEntityManagerFactory();
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		orderRepository = new OrderDatabaseRepository(entityManagerFactory);
		validatedConfig = new ExtendedValidationConfigurations();

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderController = new OrderController(orderRepository, orderSwingView, workerRepository, validatedConfig);
			orderSwingView.setOrderController(orderController);
			return orderSwingView;
		});

		window = new FrameFixture(robot(), orderSwingView);
		window.show();

	}

	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
	}

	// show all orders
	@Test
	public void testAllOrders() {
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);
		CustomerOrder order1 = new CustomerOrder("Jhon", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		CustomerOrder order2 = new CustomerOrder("Alic", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, worker);
		order1 = orderRepository.save(order1);
		order2 = orderRepository.save(order2);
		GuiActionRunner.execute(() -> orderController.allOrders());
		assertThat(window.list().contents()).containsExactly(order1.toString(), order2.toString());
	}

	// add button success
	@Test
	public void testAddSuccess() {
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());

		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);

		window.button(JButtonMatcher.withName("btnAdd")).click();
		CustomerOrder createdOrder = orderRepository.findAll().get(0);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(createdOrder.toString());
		});
	}
	// add button failure

	@Test
	public void testAddFailure() {
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());

		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "4401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);

		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(categoryIndex);

		window.button(JButtonMatcher.withName("btnAdd")).click();
		CustomerOrder order = new CustomerOrder(customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, category, status, worker);

		assertThat(window.list().contents()).isEmpty();
		window.label("showError")
				.requireText("The phone number must start with 3. Please provide a valid phone number.: " + order);

	}

	// update button susscess
	@Test
	public void testUpdateSuccess() {

		String customerName = "Ibtihaj Naeem";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);

		CustomerOrder order1 = new CustomerOrder(customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		GuiActionRunner.execute(() -> {
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
			orderController.allWorkers();
		});
		Long orderId = 1l;
		String updatedName = "Ibtihaj";
		window.textBox("txtOrderId").enterText(orderId.toString());
		window.textBox("txtCustomerName").enterText(updatedName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(0);
		window.comboBox("cmbOrderStatus").selectItem(0);
		window.comboBox("cmbWorker").selectItem(0);

		window.button(JButtonMatcher.withName("btnUpdate")).click();

		order1.setOrderId(orderId);
		order1.setCustomerName(updatedName);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(order1.toString());
		});
	}

	@Test
	public void testFetchSuccess() {

		String customerName = "Ibtihaj Naeem";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		OrderCategory category = OrderCategory.PLUMBER;
		Worker worker = new Worker("Jhon", "123456789", category);
		worker = workerRepository.save(worker);

		OrderStatus status = OrderStatus.PENDING;
		CustomerOrder order1 = new CustomerOrder(customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, category, status, worker);
		GuiActionRunner.execute(() -> {
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
			orderController.allWorkers();
		});
		Long orderId = 1l;

		window.textBox("txtOrderId").enterText(orderId.toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();

		assertEquals(customerName, window.textBox("txtCustomerName").text());
		assertEquals(customerPhone, window.textBox("txtCustomerPhone").text());
		assertEquals(customerAddress, window.textBox("txtCustomerAddress").text());
		assertEquals(orderDescription, window.textBox("txtOrderDescription").text());
		assertEquals(appointmentDate, window.textBox("txtSelectedDate").text());

		assertEquals(category.name(), window.comboBox("cmbOrderCategory").selectedItem());
		assertEquals(status.name(), window.comboBox("cmbOrderStatus").selectedItem());
		assertEquals(worker.toString(), window.comboBox("cmbWorker").selectedItem());

	}

	// update button failure
	@Test
	public void testUpdateFailure() {
		String customerName = "Ibtihaj Naeem";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);

		CustomerOrder order1 = new CustomerOrder(customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, OrderCategory.PLUMBER, OrderStatus.PENDING, worker);

		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
		});
		Long orderId = 1l;
		order1.setOrderId(orderId);
		window.textBox("txtOrderId").enterText(orderId.toString());
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(0);
		window.comboBox("cmbOrderStatus").selectItem(0);
		window.comboBox("cmbWorker").selectItem(0);

		String updatedPhone = "4401372678";
		window.textBox("txtCustomerPhone").enterText(updatedPhone);
		window.button(JButtonMatcher.withName("btnUpdate")).click();
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(order1.toString());
		});
		order1.setCustomerPhoneNumber(updatedPhone);
		window.label("showError")
				.requireText("The phone number must start with 3. Please provide a valid phone number.: " + order1);

	}
	// search button success

	@Test
	public void testSearchSuccess() {
		String appointmentDate = "12-12-2024";
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);
		CustomerOrder order1 = new CustomerOrder("Jhon", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		CustomerOrder order2 = new CustomerOrder("Alic", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, worker);
		order1 = orderRepository.save(order1);
		orderRepository.save(order2);
		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.allOrders();
		});
		String searchText = "Jhon";
		window.textBox("txtSearchOrder").enterText(searchText);
		int searchOptionIndex = 2;
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		assertThat(window.list("listOrders").contents()).containsExactly(order1.toString());

	}

	// search button failure
	@Test
	public void testSearchFailure() {
		String appointmentDate = "12-12-2024";
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);
		CustomerOrder order1 = new CustomerOrder("Jhon", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		CustomerOrder order2 = new CustomerOrder("Alic", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, worker);
		order1 = orderRepository.save(order1);
		order2 = orderRepository.save(order2);
		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.allOrders();
		});
		String searchText = "Bob";
		window.textBox("txtSearchOrder").deleteText();
		window.textBox("txtSearchOrder").enterText(searchText);
		int searchOptionIndex = 2;
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchOrder")).click();
		assertThat(window.list().contents()).containsExactly(order1.toString(), order2.toString());

		window.label("showSearchErrorLbl")
				.requireText("No orders found with customer name: " + searchText + ": " + searchText);
	}
	// clear button success

	@Test
	public void testClearSearch() {
		String appointmentDate = "12-12-2024";
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);
		CustomerOrder order1 = new CustomerOrder("Jhon", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		CustomerOrder order2 = new CustomerOrder("Alic", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, worker);
		order1 = orderRepository.save(order1);
		order2 = orderRepository.save(order2);
		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.allOrders();
		});
		String searchText = "Jhon";
		int searchOptionIndex = 2;
		window.textBox("txtSearchOrder").enterText(searchText);
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchOrder")).click();
		assertThat(window.list().contents()).containsExactly(order1.toString());
		window.button(JButtonMatcher.withName("btnClearSearch")).click();
		assertThat(window.list().contents()).containsExactly(order1.toString(), order2.toString());

	}

	// delete button sucess

	@Test
	public void testDeleteButtonSuccess() {
		String customerName = "Ibtihaj Naeem";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);

		CustomerOrder order1 = new CustomerOrder(customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, OrderCategory.PLUMBER, OrderStatus.PENDING, worker);

		GuiActionRunner.execute(() -> {

			orderController.createOrUpdateOrder(order1, OperationType.ADD);
		});
		window.list("listOrders").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).isEmpty();
		});
	}
	// delete button failure

	@Test
	public void testDeleteButtonError() {
		String appointmentDate = "12-12-2024";
		Worker worker = new Worker("Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);
		CustomerOrder order1 = new CustomerOrder(1l, "Jhon", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderSwingView.getOrderListModel().addElement(order1);
		});
		window.list().selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(window.list().contents()).containsExactly(order1.toString());
		window.label("showErrorNotFoundLbl")
				.requireText("No order found with ID: " + order1.getOrderId() + ": " + order1);
	}

	// fetch button failure

	@Test
	public void testFetchFailure() {

		String customerName = "Ibtihaj Naeem";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		OrderCategory category = OrderCategory.PLUMBER;
		Worker worker = new Worker(1l, "Jhon", "123456789", category);
		worker = workerRepository.save(worker);

		OrderStatus status = OrderStatus.PENDING;
		CustomerOrder order1 = new CustomerOrder(customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, category, status, worker);
		GuiActionRunner.execute(() -> {
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
			orderController.allOrders();
		});
		Long orderId = 2l;

		window.textBox("txtOrderId").enterText(orderId.toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();
		assertEquals("", window.textBox("txtCustomerName").text());
		assertEquals("", window.textBox("txtCustomerPhone").text());
		assertEquals("", window.textBox("txtCustomerAddress").text());
		assertEquals("", window.textBox("txtOrderDescription").text());
		assertEquals("", window.textBox("txtSelectedDate").text());

		assertNull(window.comboBox("cmbOrderCategory").selectedItem());
		assertNull(window.comboBox("cmbOrderStatus").selectedItem());
		assertNull(window.comboBox("cmbWorker").selectedItem());
		CustomerOrder newOrder = new CustomerOrder();
		newOrder.setOrderId(orderId);
		window.label("showErrorNotFoundLbl").requireText("Order with ID " + orderId + " not found.: " + newOrder);
	}
}
