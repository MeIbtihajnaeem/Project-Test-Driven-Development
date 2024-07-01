/*
 * Integration tests for the OrderSwingView class.
 * 
 * These tests cover the following functionalities:
 * 
 * - Setting up and tearing down the test environment using Docker containers and database configurations.
 * - Creating, updating, fetching, searching, and deleting customer orders through the graphical user interface.
 * - Verifying successful and failed operations for adding, updating, fetching, searching, and deleting orders.
 * - Ensuring correct validation and error handling for invalid input data.
 * - Using the AssertJSwingJUnitTestCase framework for GUI testing and Awaitility for asynchronous operations.
 * 
 * The databaseConfig variable is responsible for starting the Docker container.
 * If the test is run from Eclipse, it runs the Docker container using Testcontainers.
 * If the test is run using a Maven command, it starts a Docker container without test containers.
 * 
 * @see OrderController
 * @see OrderRepository
 * @see WorkerRepository
 * @see OrderView
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 * @see DatabaseConfig
 * @see DBConfig
 * @see MavenContainerConfig
 * @see TestContainerConfig
 */

package com.mycompany.orderassignmentsystem.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

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

/**
 * The Class OrderSwingViewIT.
 */
@RunWith(GUITestRunner.class)

public class OrderSwingViewIT extends AssertJSwingJUnitTestCase {

	/** The order repository. */
	private OrderRepository orderRepository;

	/** The order swing view. */
	private OrderSwingView orderSwingView;

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The validated config. */
	private ValidationConfigurations validatedConfig;

	/** The order controller. */
	private OrderController orderController;

	/** The window. */
	private FrameFixture window;

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/**
	 * This variable is responsible for starting the Docker container. If the test
	 * is run from Eclipse, it runs the Docker container using Testcontainers. If
	 * the test is run using a Maven command, it starts a Docker container without
	 * test containers
	 */
	private static DBConfig databaseConfig;

	/** The worker name 1. */
	private String WORKER_NAME_1 = "Bob";

	/** The worker phone 1. */
	private String WORKER_PHONE_1 = "3401372678";

	/** The worker category 1. */
	private OrderCategory WORKER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The order id 1. */
	private long ORDER_ID_1 = 1l;

	/** The order id 2. */
	private long ORDER_ID_2 = 2l;

	/** The customer name 1. */
	private String CUSTOMER_NAME_1 = "Jhon";

	/** The customer phone 1. */
	private String CUSTOMER_PHONE_1 = "3401372671";

	/** The customer address 1. */
	private String CUSTOMER_ADDRESS_1 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 1. */
	private String ORDER_APPOINTMENT_DATE_1 = "12-12-2024";

	/** The order description 1. */
	private String ORDER_DESCRIPTION_1 = "Please be on time";

	/** The order category 1. */
	private OrderCategory ORDER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The order status 1. */
	private OrderStatus ORDER_STATUS_1 = OrderStatus.PENDING;

	/** The customer name 2. */
	private String CUSTOMER_NAME_2 = "Alic";

	/** The customer phone 2. */
	private String CUSTOMER_PHONE_2 = "3401372672";

	/** The customer address 2. */
	private String CUSTOMER_ADDRESS_2 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 2. */
	private String ORDER_APPOINTMENT_DATE_2 = "12-12-2024";

	/** The order description 2. */
	private String ORDER_DESCRIPTION_2 = "Please bring tape";

	/** The order category 2. */
	private OrderCategory ORDER_CATEGORY_2 = OrderCategory.PLUMBER;

	/** The order status 2. */
	private OrderStatus ORDER_STATUS_2 = OrderStatus.PENDING;

	/** The selecting category index. */
	private int SELECTING_CATEGORY_INDEX = 0;

	/** The selecting status index. */
	private int SELECTING_STATUS_INDEX = 0;

	/** The selecting worker index. */
	private int SELECTING_WORKER_INDEX = 0;

	/** The worker. */
	private Worker worker;

	/**
	 * Setup server.
	 */
	@BeforeClass
	public static void setupServer() {
		databaseConfig = DatabaseConfig.getDatabaseConfig();

		databaseConfig.testAndStartDatabaseConnection();
	}

	/**
	 * On set up.
	 *
	 * @throws Exception the exception
	 */
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

		worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		worker = workerRepository.save(worker);

	}

	/**
	 * On tear down.
	 */
	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
	}

	/**
	 * Test all orders.
	 */
	@Test
	public void testAllOrders() {
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		CustomerOrder order2 = new CustomerOrder(CUSTOMER_NAME_2, CUSTOMER_ADDRESS_2, CUSTOMER_PHONE_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker);

		order1 = orderRepository.save(order1);
		order2 = orderRepository.save(order2);
		GuiActionRunner.execute(() -> orderController.allOrders());
		assertThat(window.list().contents()).containsExactly(order1.toString(), order2.toString());
	}

	/**
	 * Test add success.
	 */
	@Test
	public void testAddSuccess() {

		GuiActionRunner.execute(() -> orderController.allWorkers());

		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);
		window.button(JButtonMatcher.withName("btnAdd")).click();

		CustomerOrder createdOrder = orderRepository.findAll().get(0);

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(createdOrder.toString());
		});
	}

	/**
	 * Test add failure.
	 */
	@Test
	public void testAddFailure() {

		GuiActionRunner.execute(() -> orderController.allWorkers());
		String customerPhone = "4401372678";
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);

		window.button(JButtonMatcher.withName("btnAdd")).click();
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, customerPhone,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		assertThat(window.list().contents()).isEmpty();
		window.label("showError")
				.requireText("The phone number must start with 3. Please provide a valid phone number.: " + order);

	}

	/**
	 * Test update success.
	 */
	@Test
	public void testUpdateSuccess() {

		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		GuiActionRunner.execute(() -> {
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
			orderController.allWorkers();
		});
		String updatedName = "Ibtihaj";
		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.textBox("txtCustomerName").enterText(updatedName);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);

		window.button(JButtonMatcher.withName("btnUpdate")).click();

		order1.setOrderId(ORDER_ID_1);
		order1.setCustomerName(updatedName);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(order1.toString());
		});
	}

	/**
	 * Test fetch success.
	 */
	@Test
	public void testFetchSuccess() {

		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		GuiActionRunner.execute(() -> {
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
			orderController.allWorkers();
		});

		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.button(JButtonMatcher.withName("btnFetch")).click();

		assertThat(window.textBox("txtCustomerName").text()).isEqualTo(CUSTOMER_NAME_1);
		assertThat(window.textBox("txtCustomerPhone").text()).isEqualTo(CUSTOMER_PHONE_1);
		assertThat(window.textBox("txtCustomerAddress").text()).isEqualTo(CUSTOMER_ADDRESS_1);
		assertThat(window.textBox("txtOrderDescription").text()).isEqualTo(ORDER_DESCRIPTION_1);
		assertThat(window.textBox("txtSelectedDate").text()).isEqualTo(ORDER_APPOINTMENT_DATE_1);

		assertThat(window.comboBox("cmbOrderCategory").selectedItem()).isEqualTo(ORDER_CATEGORY_1.name());
		assertThat(window.comboBox("cmbOrderStatus").selectedItem()).isEqualTo(ORDER_STATUS_1.name());
		assertThat(window.comboBox("cmbWorker").selectedItem()).isEqualTo(worker.toString());

	}

	/**
	 * Test update failure.
	 */
	@Test
	public void testUpdateFailure() {

		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
		});
		Long orderId = 1l;
		order1.setOrderId(orderId);
		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);

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

	/**
	 * Test search success.
	 */
	@Test
	public void testSearchSuccess() {

		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		CustomerOrder order2 = new CustomerOrder(CUSTOMER_NAME_2, CUSTOMER_ADDRESS_2, CUSTOMER_PHONE_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker);
		order1 = orderRepository.save(order1);
		orderRepository.save(order2);

		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.allOrders();
		});

		String searchText = CUSTOMER_NAME_1;
		int searchOptionIndex = 2;

		window.textBox("txtSearchOrder").enterText(searchText);
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		assertThat(window.list("listOrders").contents()).containsExactly(order1.toString());

	}

	/**
	 * Test search failure.
	 */
	@Test
	public void testSearchFailure() {
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		CustomerOrder order2 = new CustomerOrder(CUSTOMER_NAME_2, CUSTOMER_ADDRESS_2, CUSTOMER_PHONE_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker);
		order1 = orderRepository.save(order1);
		order2 = orderRepository.save(order2);

		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.allOrders();
		});

		String searchText = "Samad";
		int searchOptionIndex = 2;

		window.textBox("txtSearchOrder").deleteText();
		window.textBox("txtSearchOrder").enterText(searchText);
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		assertThat(window.list().contents()).containsExactly(order1.toString(), order2.toString());
		window.label("showSearchErrorLbl")
				.requireText("No orders found with customer name: " + searchText + ": " + searchText);
	}

	/**
	 * Test clear search.
	 */
	@Test
	public void testClearSearch() {
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		CustomerOrder order2 = new CustomerOrder(CUSTOMER_NAME_2, CUSTOMER_ADDRESS_2, CUSTOMER_PHONE_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker);
		order1 = orderRepository.save(order1);
		order2 = orderRepository.save(order2);
		GuiActionRunner.execute(() -> {
			orderController.allWorkers();
			orderController.allOrders();
		});
		String searchText = CUSTOMER_NAME_1;
		int searchOptionIndex = 2;

		window.textBox("txtSearchOrder").enterText(searchText);
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		assertThat(window.list().contents()).containsExactly(order1.toString());

		window.button(JButtonMatcher.withName("btnClearSearch")).click();

		assertThat(window.list().contents()).containsExactly(order1.toString(), order2.toString());

	}

	/**
	 * Test delete button success.
	 */
	@Test
	public void testDeleteButtonSuccess() {

		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		GuiActionRunner.execute(() -> {

			orderController.createOrUpdateOrder(order1, OperationType.ADD);
		});
		window.list("listOrders").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).isEmpty();
		});
	}

	/**
	 * Test delete button error.
	 */
	@Test
	public void testDeleteButtonError() {
		CustomerOrder order1 = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

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

	/**
	 * Test fetch failure.
	 */
	@Test
	public void testFetchFailure() {

		CustomerOrder order1 = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

		GuiActionRunner.execute(() -> {
			orderRepository.save(order1);
			orderController.allOrders();
		});
		Long orderId = ORDER_ID_2;

		window.textBox("txtOrderId").enterText(orderId.toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();

		assertThat(window.textBox("txtCustomerName").text()).isEmpty();
		assertThat(window.textBox("txtCustomerPhone").text()).isEmpty();
		assertThat(window.textBox("txtCustomerAddress").text()).isEmpty();
		assertThat(window.textBox("txtOrderDescription").text()).isEmpty();
		assertThat(window.textBox("txtSelectedDate").text()).isEmpty();

		assertThat(window.comboBox("cmbOrderCategory").selectedItem()).isNull();
		assertThat(window.comboBox("cmbOrderStatus").selectedItem()).isNull();
		assertThat(window.comboBox("cmbWorker").selectedItem()).isNull();

		CustomerOrder newOrder = new CustomerOrder();
		newOrder.setOrderId(orderId);
		window.label("showErrorNotFoundLbl").requireText("Order with ID " + orderId + " not found.: " + newOrder);
	}
}
