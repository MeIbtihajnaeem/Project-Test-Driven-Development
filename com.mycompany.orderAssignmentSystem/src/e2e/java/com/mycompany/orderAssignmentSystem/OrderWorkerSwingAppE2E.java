package com.mycompany.orderassignmentsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.HashMap;
import java.util.Map;
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

@RunWith(GUITestRunner.class)
public class OrderWorkerSwingAppE2E extends AssertJSwingJUnitTestCase {
//	private static final String persistenceUnitName = "myPersistenceUnit";
	private static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";
	private static final int MAX_RETRIES = 3;
	private static final long RETRY_DELAY_SECONDS = 10;
	private static final String host = "localhost";
	private static final String port = "5432";
	private static final String database = "orderWorkerTestDb";
	private static final String user = "testUser";
	private static final String password = "test123";
	private FrameFixture orderViewWindow;
	private FrameFixture workerViewWindow;
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;
	private static Map<String, String> properties = new HashMap<>();

	private static final Long WORKER_FIXTURE_1_ID = 1L;
	private static final Long WORKER_FIXTURE_2_ID = 2L;
	private static final Long WORKER_FIXTURE_3_ID = 3L;

	private static final String WORKER_FIXTURE_1_NAME = "Alic";
	private static final String WORKER_FIXTURE_2_NAME = "Bob";
	private static final String WORKER_FIXTURE_3_NAME = "Jhon";

	private static final String WORKER_FIXTURE_1_WORKER_PHONE_NUMBER = "3401372678";
	private static final String WORKER_FIXTURE_2_WORKER_PHONE_NUMBER = "3401372679";
	private static final String WORKER_FIXTURE_3_WORKER_PHONE_NUMBER = "3401372677";

	private static final OrderCategory ORDER_WORKER_FIXTURE_1_CATEGORY = OrderCategory.PLUMBER;
	private static final OrderCategory ORDER_WORKER_FIXTURE_2_CATEGORY = OrderCategory.ELECTRICIAN;
	private static final OrderCategory ORDER_WORKER_FIXTURE_3_CATEGORY = OrderCategory.ELECTRICIAN;

	private static final Long ORDER_FIXTURE_1_ID = 1L;
	private static final Long ORDER_FIXTURE_2_ID = 2L;

	private static final String ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME = "Jhon";
	private static final String ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_NAME = "Bill";

	private static final String ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS = "Piazza Luigi Dalla";
	private static final String ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_ADDRESS = "Piazza Luigi Dalla";

	private static final String ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_PHONE_NUMBER = "3401372671";
	private static final String ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_PHONE_NUMBER = "3401372672";

	private static final String ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE = "12-12-2024";
	private static final String ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DATE = "13-12-2024";

	private static final String ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION = "No description";
	private static final String ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DESCRIPTION = "Bring tape";

	private static final OrderStatus ORDER_FIXTURE_1_STATUS = OrderStatus.COMPLETED;
	private static final OrderStatus ORDER_FIXTURE_2_STATUS = OrderStatus.COMPLETED;

	private Worker worker1 = new Worker(WORKER_FIXTURE_1_NAME, WORKER_FIXTURE_1_WORKER_PHONE_NUMBER,
			ORDER_WORKER_FIXTURE_1_CATEGORY);

	private Worker worker2 = new Worker(WORKER_FIXTURE_2_NAME, WORKER_FIXTURE_2_WORKER_PHONE_NUMBER,
			ORDER_WORKER_FIXTURE_2_CATEGORY);

	private Worker worker3 = new Worker(WORKER_FIXTURE_3_NAME, WORKER_FIXTURE_3_WORKER_PHONE_NUMBER,
			ORDER_WORKER_FIXTURE_3_CATEGORY);

	CustomerOrder order1 = new CustomerOrder(ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_NAME,
			ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_ADDRESS, ORDER_CUSTOMER_FIXTURE_1_CUSTOMER_PHONE_NUMBER,
			ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DATE, ORDER_CUSTOMER_FIXTURE_1_ORDER_APPOINTMENT_DESCRIPTION,
			ORDER_WORKER_FIXTURE_1_CATEGORY, ORDER_FIXTURE_1_STATUS, worker1);

	CustomerOrder order2 = new CustomerOrder(ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_NAME,
			ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_ADDRESS, ORDER_CUSTOMER_FIXTURE_2_CUSTOMER_PHONE_NUMBER,
			ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DATE, ORDER_CUSTOMER_FIXTURE_2_ORDER_APPOINTMENT_DESCRIPTION,
			ORDER_WORKER_FIXTURE_2_CATEGORY, ORDER_FIXTURE_2_STATUS, worker2);

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
					try {
						TimeUnit.SECONDS.sleep(RETRY_DELAY_SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	@Override
	protected void onSetUp() throws Exception {
		// TODO Auto-generated method stub
		String persistenceUnitName = "OriginalPersistenceUnit";
		String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
		System.out.println(jdbcUrl);

		properties.put("javax.persistence.jdbc.url", jdbcUrl);
		properties.put("javax.persistence.jdbc.user", user);
		properties.put("javax.persistence.jdbc.password", password);
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "create-drop");

		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		entityManager = entityManagerFactory.createEntityManager();

		addTestOrderAndWorkerToDatabase(worker1, order1);
		addTestOrderAndWorkerToDatabase(worker2, order2);
		addTestWorkerToDatabase(worker3);

		application("com.mycompany.orderAssignmentSystem.app.OrderWorkerAssignmentSwingApp")
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

	// create a after to close entityManager
	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

	private void addTestOrderAndWorkerToDatabase(Worker worker, CustomerOrder order) {
		EntityTransaction workerTransaction = entityManager.getTransaction();
		workerTransaction.begin();
		Worker savedWorker = entityManager.merge(worker);
		workerTransaction.commit();
		order.setWorker(savedWorker);
		EntityTransaction orderTransaction = entityManager.getTransaction();
		orderTransaction.begin();
		CustomerOrder savedOrder = entityManager.merge(order);
		order = savedOrder;
		orderTransaction.commit();
	}

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

	@Test
	public void testOrderViewFetchFailure() {

		Long orderId = 3L;

		orderViewWindow.textBox("txtOrderId").enterText(orderId.toString());
		orderViewWindow.button(JButtonMatcher.withName("btnFetch")).click();

		assertThat(orderViewWindow.label("showErrorNotFoundLbl").text()).contains(orderId.toString());
	}

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

	@Test
	public void testOrderViewSearchFailure() {
		String searchText = "Bob";
		orderViewWindow.textBox("txtSearchOrder").enterText(searchText);
		int searchOptionIndex = 2;
		orderViewWindow.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		orderViewWindow.button(JButtonMatcher.withName("btnSearchOrder")).click();
		assertThat(orderViewWindow.label("showSearchErrorLbl").text()).contains(searchText);
	}

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

	/// ------------------- Tests for worker view ---------- ///
	private void addTestWorkerToDatabase(Worker worker) {
		EntityTransaction workerTransaction = entityManager.getTransaction();
		workerTransaction.begin();
		entityManager.merge(worker);
		workerTransaction.commit();
	}

	private void openWorkerView() {
		orderViewWindow.button("btnManageWorker").click();

		workerViewWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Worker View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

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

	@Test
	@GUITest
	public void testWorkerViewFetchAndUpdateButtonSuccess() {
		openWorkerView();
		workerViewWindow.textBox("txtWorkerId").enterText(WORKER_FIXTURE_3_ID.toString());
		workerViewWindow.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedName = "Ibtihaj";
		workerViewWindow.textBox("txtWorkerName").enterText(updatedName);
		workerViewWindow.button(JButtonMatcher.withName("btnUpdate")).click();
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		assertThat(workerViewWindow.list("listWorkers").contents())
				.anySatisfy(e -> assertThat(e).contains(WORKER_FIXTURE_3_ID.toString(), updatedName,
						WORKER_FIXTURE_3_WORKER_PHONE_NUMBER, ORDER_WORKER_FIXTURE_3_CATEGORY.toString()));

	}

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
