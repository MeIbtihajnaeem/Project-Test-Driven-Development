/*
 * Integration tests for the OrderModelViewControllerIT class.
 *
 * These tests verify the integration between the OrderController, OrderSwingView,
 * and the underlying repositories. The tests cover scenarios such as adding, updating,
 * fetching, and deleting orders. The tests utilize AssertJ Swing for GUI testing,
 * ensuring that the user interface behaves correctly and interacts properly with the
 * controller and repositories.
 *
 * Key features tested include:
 * - Adding 
 * - Updating 
 * - Fetching 
 * - Deleting 
 *
 * The setup and teardown methods handle the initialization and cleanup of resources,
 * including database connections and GUI components.
 *
 * The databaseConfig variable is responsible for starting the Docker container.
 * If the test is run from Eclipse, it runs the Docker container using Testcontainers.
 * If the test is run using a Maven command, it starts a real Docker container.
 *
 * @see OrderController
 * @see OrderRepository
 * @see WorkerRepository
 * @see OrderSwingView
 * @see ExtendedValidationConfigurations
 * @see DatabaseConfig
 * @see DBConfig
 * @see MavenContainerConfig
 * @see TestContainerConfig
 */

package com.mycompany.orderassignmentsystem.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

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
 * The Class OrderModelViewControllerIT.
 */
@RunWith(GUITestRunner.class)

public class OrderModelViewControllerIT extends AssertJSwingJUnitTestCase {

	/** The order repository. */
	private OrderRepository orderRepository;

	/** The order swing view. */
	private OrderSwingView orderSwingView;

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The order controller. */
	private OrderController orderController;

	/** The window. */
	private FrameFixture window;

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/** The validation config. */
	private ValidationConfigurations validationConfig;

	/**
	 * This variable is responsible for starting the Docker container. If the test
	 * is run from Eclipse, it runs the Docker container using Testcontainers. If
	 * the test is run using a Maven command, it starts a real Docker container.
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

	/** The selecting category index. */
	private int SELECTING_CATEGORY_INDEX = 0;

	/** The selecting status index. */
	private int SELECTING_STATUS_INDEX = 0;

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
		validationConfig = new ExtendedValidationConfigurations();

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderController = new OrderController(orderRepository, orderSwingView, workerRepository, validationConfig);
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
	 * Test add order.
	 */
	@Test
	public void testAddOrder() {
		// Setup & Exercise
		GuiActionRunner.execute(() -> orderController.allWorkers());

		window.textBox("txtOrderId").enterText(" "); // Make sure orderId is empty
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(Pattern.compile(".*" + WORKER_NAME_1 + ".*"));

		window.button(JButtonMatcher.withName("btnAdd")).click();

		// Verify
		CustomerOrder savedOrder = orderRepository.findAll().get(0);
		assertThat(orderRepository.findById(savedOrder.getOrderId())).isEqualTo(savedOrder);
	}

	/**
	 * Test update and fetch order.
	 */
	@Test
	public void testUpdateAndFetchOrder() {
		// This method first save order in the database then fetch the
		// order by id using fetch button and then
		// edit the customer name using update button
		// both fetch and update are by button clicked

		// Setup
		GuiActionRunner.execute(() -> orderController.allWorkers());
		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);

		// Exercise
		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedCustomerName = "Samad";
		window.textBox("txtCustomerName").enterText(updatedCustomerName);
		window.button(JButtonMatcher.withName("btnUpdate")).click();

		// Verify
		order.setCustomerName(updatedCustomerName);
		assertThat(orderRepository.findById(ORDER_ID_1)).isEqualTo(order);
	}

	/**
	 * Test delete order.
	 */
	@Test
	public void testDeleteOrder() {

		// Setup
		GuiActionRunner.execute(() -> orderController.allWorkers());
		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);
		order = orderRepository.save(order);
		GuiActionRunner.execute(() -> orderController.allOrders());

		// Exercise
		window.list("listOrders").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();

		// Verify
		assertThat(orderRepository.findById(order.getOrderId())).isNull();
	}
}
