package com.mycompany.orderAssignmentSystem.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycompany.orderAssignmentSystem.controller.OrderController;
import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.OrderRepository;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderAssignmentSystem.view.swing.OrderSwingView;

@RunWith(GUITestRunner.class)

public class OrderModelViewControllerIT extends AssertJSwingJUnitTestCase {
	private static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";
	private static final int MAX_RETRIES = 3;
	private static final long RETRY_DELAY_SECONDS = 10;

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

	private OrderRepository orderRepository;

	private OrderSwingView orderSwingView;

	private WorkerRepository workerRepository;

	private OrderController orderController;

	private FrameFixture window;
	private EntityManagerFactory entityManagerFactory;
//	private EntityManager entityManager;
	private ValidationConfigurations validationConfig;

	@Override
	protected void onSetUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//		entityManager = entityManagerFactory.createEntityManager();
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

	}

	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
//		entityManager.close();
	}

	@Test
	public void testAddOrder() {

		String name = "Naeem";
		String phoneNumber = "3401372678";

		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
//		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());

		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(Pattern.compile(".*" + savedWorker.getWorkerName() + ".*"));

		window.button(JButtonMatcher.withName("btnAdd")).click();
		CustomerOrder savedOrder = orderRepository.findAll().get(0);
//		CustomerOrder order = new CustomerOrder(savedOrder.getOrderId(), customerName, customerAddress, customerPhone,
//				appointmentDate, orderDescription, category, status, savedWorker);
		assertThat(orderRepository.findById(savedOrder.getOrderId())).isEqualTo(savedOrder);
	}

	// This method first save order in the database then fetch the
	// order by id and edit the customer name and then update it
	// both fetch and update are by button click
	@Test
	public void testUpdateAndFetchOrder() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(customerName);
		order.setCustomerAddress(customerAddress);
		order.setCustomerPhoneNumber(customerPhone);
		order.setOrderDescription(orderDescription);
		order.setAppointmentDate(appointmentDate);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(savedWorker);
		order = orderRepository.save(order);
		window.textBox("txtOrderId").enterText(order.getOrderId().toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();
		String updatedCustomerName = "Jhon";
		window.textBox("txtCustomerName").enterText(updatedCustomerName);
		window.button(JButtonMatcher.withName("btnUpdate")).click();
		order.setCustomerName(updatedCustomerName);
		order.setOrderId(order.getOrderId());

		assertThat(orderRepository.findById(order.getOrderId())).isEqualTo(order);
	}

	@Test
	public void testDeleteOrder() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(customerName);
		order.setCustomerAddress(customerAddress);
		order.setCustomerPhoneNumber(customerPhone);
		order.setOrderDescription(orderDescription);
		order.setAppointmentDate(appointmentDate);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		GuiActionRunner.execute(() -> orderController.allOrders());
		window.list("listOrders").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(orderRepository.findById(savedOrder.getOrderId())).isNull();
	}
}
