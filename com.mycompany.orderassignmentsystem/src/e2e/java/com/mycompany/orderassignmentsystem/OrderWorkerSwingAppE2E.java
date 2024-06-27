/*
 * End-to-end tests for the OrderWorkerSwingApp.
 * 
 * These tests cover the following functionalities:
 * 
 * - Setting up and tearing down the test environment, including database connections and GUI initialization.
 * - Interactions with the Order and Worker views, including adding, updating, fetching, searching, and deleting orders and workers.
 * - Verification of the correct display of database records in the GUI and the correct handling of various operations.
 * - Ensuring proper error handling and validation for both orders and workers.
 * - Using the AssertJSwingJUnitTestCase framework for GUI testing, Awaitility for asynchronous operations, and persistence for database operations.
 * 
 * The tests simulate real-world scenarios by interacting with the GUI and verifying the expected outcomes.
 * @see OrderController
 * @see OrderRepository
 * @see WorkerRepository
 * @see OrderView
 * @see WorkerView
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 */

package com.mycompany.orderassignmentsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;

/**
 * The Class OrderWorkerSwingAppE2E.
 */
@RunWith(GUITestRunner.class)
public class OrderWorkerSwingAppE2E extends AssertJSwingJUnitTestCase {

	/** The Constant PERSISTENCE_UNIT_NAME. */
	private static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";

	/** The Constant MAX_RETRIES. */
	private static final int MAX_RETRIES = 3;

	/** The Constant RETRY_DELAY_SECONDS. */
	private static final long RETRY_DELAY_SECONDS = 10;

	/** The Constant host. */
	private static final String host = "localhost";

	/** The Constant port. */
	private static final String port = "5432";

	/** The Constant database. */
	private static final String database = System.getProperty("postgres.dbName");

	/** The Constant user. */
	private static final String user = System.getProperty("postgres.user");

	/** The Constant password. */
	private static final String password = System.getProperty("postgres.password");

	/** The order view window. */
	private FrameFixture orderViewWindow;

	/** The worker view window. */
	private FrameFixture workerViewWindow;

	/** The entity manager factory. */
	private static EntityManagerFactory entityManagerFactory;

	/** The entity manager. */
	private static EntityManager entityManager;

	/** The Constant WORKER_FIXTURE_1_ID. */
	private static final Long WORKER_FIXTURE_1_ID = 1L;

	/** The Constant WORKER_FIXTURE_2_ID. */
	private static final Long WORKER_FIXTURE_2_ID = 2L;

	/** The Constant WORKER_FIXTURE_3_ID. */
	private static final Long WORKER_FIXTURE_3_ID = 3L;

	/** The Constant WORKER_FIXTURE_1_NAME. */
	private static final String WORKER_FIXTURE_1_NAME = "Alic";

	/** The Constant WORKER_FIXTURE_2_NAME. */
	private static final String WORKER_FIXTURE_2_NAME = "Bob";

	/** The Constant WORKER_FIXTURE_3_NAME. */
	private static final String WORKER_FIXTURE_3_NAME = "Jhon";

	/** The Constant WORKER_FIXTURE_1_WORKER_PHONE_NUMBER. */
	private static final String WORKER_FIXTURE_1_WORKER_PHONE_NUMBER = "3401372678";

	/** The Constant WORKER_FIXTURE_2_WORKER_PHONE_NUMBER. */
	private static final String WORKER_FIXTURE_2_WORKER_PHONE_NUMBER = "3401372679";

	/** The Constant WORKER_FIXTURE_3_WORKER_PHONE_NUMBER. */
	private static final String WORKER_FIXTURE_3_WORKER_PHONE_NUMBER = "3401372677";

	/** The Constant ORDER_WORKER_FIXTURE_1_CATEGORY. */
	private static final OrderCategory ORDER_WORKER_FIXTURE_1_CATEGORY = OrderCategory.PLUMBER;

	/** The Constant ORDER_WORKER_FIXTURE_2_CATEGORY. */
	private static final OrderCategory ORDER_WORKER_FIXTURE_2_CATEGORY = OrderCategory.ELECTRICIAN;

	/** The Constant ORDER_WORKER_FIXTURE_3_CATEGORY. */
	private static final OrderCategory ORDER_WORKER_FIXTURE_3_CATEGORY = OrderCategory.ELECTRICIAN;

	/** The Constant ORDER_FIXTURE_1_ID. */
	private static final Long ORDER_FIXTURE_1_ID = 1L;

	/** The Constant ORDER_FIXTURE_2_ID. */
	private static final Long ORDER_FIXTURE_2_ID = 2L;

	/** The Constant ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME. */
	private static final String ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME = "Jhon";

	/** The Constant ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_NAME. */
	private static final String ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_NAME = "Bill";

	/** The Constant ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS. */
	private static final String ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS = "Piazza Luigi Dalla";

	/** The Constant ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_ADDRESS. */
	private static final String ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_ADDRESS = "Piazza Luigi Dalla";

	/** The Constant ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_PHONE_NUMBER. */
	private static final String ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_PHONE_NUMBER = "3401372671";

	/** The Constant ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_PHONE_NUMBER. */
	private static final String ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_PHONE_NUMBER = "3401372672";

	/** The Constant ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE. */
	private static final String ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE = "12-12-2024";

	/** The Constant ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DATE. */
	private static final String ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DATE = "13-12-2024";

	/** The Constant ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION. */
	private static final String ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION = "No description";

	/** The Constant ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DESCRIPTION. */
	private static final String ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DESCRIPTION = "Bring tape";

	/** The Constant ORDER_FIXTURE_1_STATUS. */
	private static final OrderStatus ORDER_FIXTURE_1_STATUS = OrderStatus.COMPLETED;

	/** The Constant ORDER_FIXTURE_2_STATUS. */
	private static final OrderStatus ORDER_FIXTURE_2_STATUS = OrderStatus.COMPLETED;

	/** The worker 1. */
	private Worker worker1 = new Worker(WORKER_FIXTURE_1_NAME, WORKER_FIXTURE_1_WORKER_PHONE_NUMBER,
			ORDER_WORKER_FIXTURE_1_CATEGORY);

	/** The worker 2. */
	private Worker worker2 = new Worker(WORKER_FIXTURE_2_NAME, WORKER_FIXTURE_2_WORKER_PHONE_NUMBER,
			ORDER_WORKER_FIXTURE_2_CATEGORY);

	/** The worker 3. */
	private Worker worker3 = new Worker(WORKER_FIXTURE_3_NAME, WORKER_FIXTURE_3_WORKER_PHONE_NUMBER,
			ORDER_WORKER_FIXTURE_3_CATEGORY);

	/** The order 1. */
	CustomerOrder order1 = new CustomerOrder(ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME,
			ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS, ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_PHONE_NUMBER,
			ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE, ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION,
			ORDER_WORKER_FIXTURE_1_CATEGORY, ORDER_FIXTURE_1_STATUS, worker1);

	/** The order 2. */
	CustomerOrder order2 = new CustomerOrder(ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_NAME,
			ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_ADDRESS, ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_PHONE_NUMBER,
			ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DATE, ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DESCRIPTION,
			ORDER_WORKER_FIXTURE_2_CATEGORY, ORDER_FIXTURE_2_STATUS, worker2);

	/**
	 * Setup server.
	 */
	@BeforeClass
	public static void setupServer() {

		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				EntityManagerFactory entityManagerFactory = Persistence
						.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

				EntityManager entityManager = entityManagerFactory.createEntityManager();
				if (entityManager != null && entityManager.isOpen()) {
					entityManager.close();
					break;
				}
			} catch (Exception i) {
				attempt++;
				if (attempt < MAX_RETRIES) {
					await().atMost(RETRY_DELAY_SECONDS, TimeUnit.SECONDS);
				}
			}

		}
	}

	/**
	 * On set up.
	 *
	 * @throws Exception the exception
	 */
	@Override
	protected void onSetUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();

		addTestOrderAndWorkerToDatabase(worker1, order1);
		addTestOrderAndWorkerToDatabase(worker2, order2);
		addTestWorkerToDatabase(worker3);

		application("com.mycompany.orderassignmentsystem.app.OrderWorkerAssignmentSwingApp")
				.withArgs("--postgres-host=" + host, "--postgres-database=" + database, "--postgres-user=" + user,
						"--postgres-pass=" + password, "--postgres-port=" + port)
				.start();

		orderViewWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Order Form".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	/**
	 * On tear down.
	 */
	// create a after to close entityManager
	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

	/**
	 * Adds the test order and worker to database.
	 *
	 * @param worker the worker
	 * @param order  the order
	 */
	private void addTestOrderAndWorkerToDatabase(Worker worker, CustomerOrder order) {
		EntityTransaction workerTransaction = entityManager.getTransaction();
		workerTransaction.begin();
		Worker savedWorker = entityManager.merge(worker);
		workerTransaction.commit();
		order.setWorker(savedWorker);
		EntityTransaction orderTransaction = entityManager.getTransaction();
		orderTransaction.begin();
		entityManager.merge(order);

		orderTransaction.commit();
	}

	/**
	 * Test on start all database elements are shown for order view.
	 */
	@Test
	@GUITest
	public void testOnStartAllDatabaseElementsAreShownForOrderView() {
		assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(ORDER_FIXTURE_1_ID.toString(),
						ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME, ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS,
						ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_PHONE_NUMBER, ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE,
						ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION,
						ORDER_WORKER_FIXTURE_1_CATEGORY.toString(), ORDER_FIXTURE_1_STATUS.toString(),
						WORKER_FIXTURE_1_NAME, WORKER_FIXTURE_1_WORKER_PHONE_NUMBER,
						ORDER_WORKER_FIXTURE_1_CATEGORY.toString()))
				.anySatisfy(e -> assertThat(e).contains(ORDER_FIXTURE_2_ID.toString(),
						ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_NAME, ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_ADDRESS,
						ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_PHONE_NUMBER, ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DATE,
						ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DESCRIPTION,
						ORDER_WORKER_FIXTURE_2_CATEGORY.toString(), ORDER_FIXTURE_2_STATUS.toString(),
						WORKER_FIXTURE_2_NAME, WORKER_FIXTURE_2_WORKER_PHONE_NUMBER,
						ORDER_WORKER_FIXTURE_2_CATEGORY.toString()));
	}

	/**
	 * Test order view add button success.
	 */
	@Test
	@GUITest
	public void testOrderViewAddButtonSuccess() {
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372670";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
		OrderCategory category = (OrderCategory) orderViewWindow.comboBox("cmbOrderCategory").target()
				.getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) orderViewWindow.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		orderViewWindow.textBox("txtCustomerName").enterText(customerName);
		orderViewWindow.textBox("txtCustomerAddress").enterText(customerAddress);
		orderViewWindow.textBox("txtCustomerPhone").enterText(customerPhone);
		orderViewWindow.textBox("txtOrderDescription").enterText(orderDescription);
		orderViewWindow.textBox("txtSelectedDate").enterText(appointmentDate);
		orderViewWindow.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		orderViewWindow.comboBox("cmbOrderStatus").selectItem(statusIndex);
		orderViewWindow.comboBox("cmbWorker").selectItem(workerIndex);

		orderViewWindow.button(JButtonMatcher.withName("btnAdd")).click();

		assertThat(orderViewWindow.list().contents()).anySatisfy(e -> assertThat(e).contains(customerName,
				customerAddress, customerPhone, orderDescription, appointmentDate, category.toString(),
				status.toString(), WORKER_FIXTURE_1_ID.toString(), WORKER_FIXTURE_1_NAME));
	}

	/**
	 * Test order view add button error.
	 */
	@Test
	@GUITest
	public void testOrderViewAddButtonError() {
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372670";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 1;
		int statusIndex = 0;
		int workerIndex = 0;
		OrderCategory category = (OrderCategory) orderViewWindow.comboBox("cmbOrderCategory").target()
				.getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) orderViewWindow.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		orderViewWindow.textBox("txtCustomerName").enterText(customerName);
		orderViewWindow.textBox("txtCustomerAddress").enterText(customerAddress);
		orderViewWindow.textBox("txtCustomerPhone").enterText(customerPhone);
		orderViewWindow.textBox("txtOrderDescription").enterText(orderDescription);
		orderViewWindow.textBox("txtSelectedDate").enterText(appointmentDate);
		orderViewWindow.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		orderViewWindow.comboBox("cmbOrderStatus").selectItem(statusIndex);
		orderViewWindow.comboBox("cmbWorker").selectItem(workerIndex);

		orderViewWindow.button(JButtonMatcher.withName("btnAdd")).click();

		assertThat(orderViewWindow.label("showError").text()).contains(customerName, customerAddress, customerPhone,
				orderDescription, appointmentDate, category.toString(), status.toString(),
				WORKER_FIXTURE_1_ID.toString(), WORKER_FIXTURE_1_NAME);
	}

	/**
	 * Test order view update and fetch button success.
	 */
	@Test
	public void testOrderViewUpdateAndFetchButtonSuccess() {
		String updatedName = "Ibtihaj";

		orderViewWindow.textBox("txtOrderId").enterText(ORDER_FIXTURE_1_ID.toString());
		orderViewWindow.button(JButtonMatcher.withName("btnFetch")).click();
		orderViewWindow.textBox("txtCustomerName").enterText(updatedName);

		orderViewWindow.button(JButtonMatcher.withName("btnUpdate")).click();
		assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(ORDER_FIXTURE_1_ID.toString(), updatedName,
						ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS, WORKER_FIXTURE_1_WORKER_PHONE_NUMBER,
						ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION,
						ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE, WORKER_FIXTURE_1_ID.toString(),
						WORKER_FIXTURE_1_NAME));
	}

	/**
	 * Test order view fetch failure.
	 */
	@Test
	public void testOrderViewFetchFailure() {

		Long orderId = 3L;

		orderViewWindow.textBox("txtOrderId").enterText(orderId.toString());
		orderViewWindow.button(JButtonMatcher.withName("btnFetch")).click();

		assertThat(orderViewWindow.label("showErrorNotFoundLbl").text()).contains(orderId.toString());
	}

	/**
	 * Test order view update button failure.
	 */
	@Test
	public void testOrderViewUpdateButtonFailure() {

		String updatedPhone = "4401372678";

		orderViewWindow.textBox("txtOrderId").enterText(ORDER_FIXTURE_1_ID.toString());
		orderViewWindow.button(JButtonMatcher.withName("btnFetch")).click();
		orderViewWindow.textBox("txtCustomerPhone").enterText(updatedPhone);

		orderViewWindow.button(JButtonMatcher.withName("btnUpdate")).click();

		assertThat(orderViewWindow.label("showError").text()).contains(ORDER_FIXTURE_1_ID.toString(),
				ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME, ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS, updatedPhone,
				ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION, ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE,
				WORKER_FIXTURE_1_ID.toString(), WORKER_FIXTURE_1_NAME);

	}

	/**
	 * Test order view search success.
	 */
	@Test
	public void testOrderViewSearchSuccess() {
		String searchText = ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME;
		orderViewWindow.textBox("txtSearchOrder").enterText(searchText);
		int searchOptionIndex = 2;
		orderViewWindow.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		orderViewWindow.button(JButtonMatcher.withName("btnSearchOrder")).click();

		assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(ORDER_FIXTURE_1_ID.toString(),
						ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME, ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS,
						WORKER_FIXTURE_1_WORKER_PHONE_NUMBER, ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION,
						ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE, WORKER_FIXTURE_1_ID.toString(),
						WORKER_FIXTURE_1_NAME));
	}

	/**
	 * Test order view search failure.
	 */
	@Test
	public void testOrderViewSearchFailure() {
		String searchText = "Bob";
		orderViewWindow.textBox("txtSearchOrder").enterText(searchText);
		int searchOptionIndex = 2;
		orderViewWindow.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		orderViewWindow.button(JButtonMatcher.withName("btnSearchOrder")).click();
		assertThat(orderViewWindow.label("showSearchErrorLbl").text()).contains(searchText);
	}

	/**
	 * Test order view clear search.
	 */
	@Test
	public void testOrderViewClearSearch() {
		String searchText = ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME;
		int searchOptionIndex = 2;
		orderViewWindow.textBox("txtSearchOrder").enterText(searchText);
		orderViewWindow.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		orderViewWindow.button(JButtonMatcher.withName("btnSearchOrder")).click();
		assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(ORDER_FIXTURE_1_ID.toString(),
						ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME, ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS,
						WORKER_FIXTURE_1_WORKER_PHONE_NUMBER, ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION,
						ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE, WORKER_FIXTURE_1_ID.toString(),
						WORKER_FIXTURE_1_NAME));
		orderViewWindow.button(JButtonMatcher.withName("btnClearSearch")).click();

		assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(ORDER_FIXTURE_1_ID.toString(), ORDER_FIXTURE_2_ID.toString()));
	}

	/**
	 * Test order view delete button success.
	 */
	@Test
	public void testOrderViewDeleteButtonSuccess() {
		orderViewWindow.list("listOrders").selectItem(0);
		orderViewWindow.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(ORDER_FIXTURE_2_ID.toString(),
						ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_NAME, ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_ADDRESS,
						WORKER_FIXTURE_2_WORKER_PHONE_NUMBER, ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DESCRIPTION,
						ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DATE, WORKER_FIXTURE_2_ID.toString(),
						WORKER_FIXTURE_2_NAME));

	}

	/**
	 * Adds the test worker to database.
	 *
	 * @param worker the worker
	 */
	/// ------------------- Tests for worker view ---------- ///
	private void addTestWorkerToDatabase(Worker worker) {
		EntityTransaction workerTransaction = entityManager.getTransaction();
		workerTransaction.begin();
		entityManager.merge(worker);
		workerTransaction.commit();
	}

	/**
	 * Open worker view.
	 */
	private void openWorkerView() {
		orderViewWindow.button("btnManageWorker").click();

		workerViewWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Worker View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	/**
	 * Test on start all database elements are shown for worker view.
	 */
	@Test
	@GUITest
	public void testOnStartAllDatabaseElementsAreShownForWorkerView() {
		openWorkerView();
		assertThat(workerViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_1_NAME, WORKER_FIXTURE_1_WORKER_PHONE_NUMBER,
						ORDER_WORKER_FIXTURE_1_CATEGORY.toString()))
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_2_NAME, WORKER_FIXTURE_2_WORKER_PHONE_NUMBER,
						ORDER_WORKER_FIXTURE_2_CATEGORY.toString()));
	}

	/**
	 * Test worker view add button success.
	 */
	@Test
	@GUITest
	public void testWorkerViewAddButtonSuccess() {
		openWorkerView();
		String name = "Naeem";
		String phoneNumber = "3401372671";
		int categoryIndex = 0;
		OrderCategory category = (OrderCategory) workerViewWindow.comboBox("cmbWorkerCategory").target()
				.getItemAt(categoryIndex);
		workerViewWindow.textBox("txtWorkerName").enterText(name);
		workerViewWindow.textBox("txtWorkerPhone").enterText(phoneNumber);

		workerViewWindow.comboBox("cmbWorkerCategory").selectItem(categoryIndex);
		workerViewWindow.button(JButtonMatcher.withName("btnAdd")).click();
		assertThat(workerViewWindow.list("listWorkers").contents())
				.anySatisfy(e -> assertThat(e).contains(name, phoneNumber, category.toString()));
	}

	/**
	 * Test worker view add button failure.
	 */
	@Test
	@GUITest
	public void testWorkerViewAddButtonFailure() {
		openWorkerView();
		String name = "Naeem";
		String phoneNumber = "340137267100";
		int categoryIndex = 0;
		OrderCategory category = (OrderCategory) workerViewWindow.comboBox("cmbWorkerCategory").target()
				.getItemAt(categoryIndex);
		workerViewWindow.textBox("txtWorkerName").enterText(name);
		workerViewWindow.textBox("txtWorkerPhone").enterText(phoneNumber);

		workerViewWindow.comboBox("cmbWorkerCategory").selectItem(categoryIndex);
		workerViewWindow.button(JButtonMatcher.withName("btnAdd")).click();

		assertThat(workerViewWindow.label("showErrorLbl").text()).contains(name, phoneNumber, category.toString());
	}

	/**
	 * Test worker view fetch and update button success.
	 */
	@Test
	@GUITest
	public void testWorkerViewFetchAndUpdateButtonSuccess() {
		openWorkerView();
		workerViewWindow.textBox("txtWorkerId").enterText(WORKER_FIXTURE_3_ID.toString());
		workerViewWindow.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedName = "Ibtihaj";
		workerViewWindow.textBox("txtWorkerName").enterText(updatedName);
		workerViewWindow.button(JButtonMatcher.withName("btnUpdate")).click();
		assertThat(workerViewWindow.list("listWorkers").contents())
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_3_ID.toString(), updatedName,
						WORKER_FIXTURE_3_WORKER_PHONE_NUMBER, ORDER_WORKER_FIXTURE_3_CATEGORY.toString()));

	}

	/**
	 * Test worker view update button error.
	 */
	@Test
	@GUITest
	public void testWorkerViewUpdateButtonError() {
		openWorkerView();
		workerViewWindow.textBox("txtWorkerId").enterText(WORKER_FIXTURE_1_ID.toString());
		workerViewWindow.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedPhoneNumber = "4401372678";
		workerViewWindow.textBox("txtWorkerPhone").enterText(updatedPhoneNumber);
		workerViewWindow.button(JButtonMatcher.withName("btnUpdate")).click();

		assertThat(workerViewWindow.label("showErrorLbl").text()).contains(WORKER_FIXTURE_1_ID.toString(),
				WORKER_FIXTURE_1_NAME, updatedPhoneNumber, ORDER_WORKER_FIXTURE_1_CATEGORY.toString());

	}

	/**
	 * Test worker view delete button success.
	 */
	@Test
	@GUITest
	public void testWorkerViewDeleteButtonSuccess() {
		openWorkerView();
		workerViewWindow.list("listWorkers").selectItem(0);
		workerViewWindow.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(workerViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_2_ID.toString(), WORKER_FIXTURE_2_NAME,
						WORKER_FIXTURE_2_WORKER_PHONE_NUMBER, ORDER_WORKER_FIXTURE_2_CATEGORY.toString()));

	}

	/**
	 * Test worker view search worker success.
	 */
	@Test
	public void testWorkerViewSearchWorkerSuccess() {
		openWorkerView();
		String searchText = WORKER_FIXTURE_1_NAME;
		int searchOptionIndex = 2;

		workerViewWindow.textBox("txtSearchWorker").enterText(searchText);
		workerViewWindow.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		workerViewWindow.button(JButtonMatcher.withName("btnSearchWorker")).click();

		assertThat(workerViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_1_ID.toString(), WORKER_FIXTURE_1_NAME,
						ORDER_WORKER_FIXTURE_1_CATEGORY.toString(), WORKER_FIXTURE_1_WORKER_PHONE_NUMBER));

	}

	/**
	 * Test worker view search worker error.
	 */
	@Test
	public void testWorkerViewSearchWorkerError() {
		openWorkerView();
		String searchText = "Ibtihaj";
		int searchOptionIndex = 1;

		workerViewWindow.textBox("txtSearchWorker").enterText(searchText);
		workerViewWindow.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		workerViewWindow.button(JButtonMatcher.withName("btnSearchWorker")).click();
		assertThat(workerViewWindow.label("showErrorLblSearchWorker").text()).contains(searchText);

	}

	/**
	 * Test worker view clear search.
	 */
	@Test
	public void testWorkerViewClearSearch() {
		openWorkerView();
		String searchText = WORKER_FIXTURE_1_NAME;
		int searchOptionIndex = 2;
		workerViewWindow.textBox("txtSearchWorker").enterText(searchText);
		workerViewWindow.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		workerViewWindow.button(JButtonMatcher.withName("btnSearchWorker")).click();

		assertThat(workerViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_1_ID.toString(), WORKER_FIXTURE_1_NAME,
						ORDER_WORKER_FIXTURE_1_CATEGORY.toString(), WORKER_FIXTURE_1_WORKER_PHONE_NUMBER));
		workerViewWindow.button(JButtonMatcher.withName("btnClearSearchWorker")).click();

		assertThat(workerViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_1_ID.toString(), ORDER_FIXTURE_2_ID.toString()));

	}

}