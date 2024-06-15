package com.mycompany.orderAssignmentSystem.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycompany.orderAssignmentSystem.controller.OrderController;
import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
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

public class OrderSwingViewIT extends AssertJSwingJUnitTestCase {

	private OrderRepository orderRepository;

	private OrderSwingView orderSwingView;

	private WorkerRepository workerRepository;
	private ValidationConfigurations validatedConfig;

	private OrderController orderController;

	private FrameFixture window;
	private EntityManagerFactory entityManagerFactory;
//	private EntityManager entityManager;
	private static final String PERSISTENCE_UNIT_NAME = "test_myPersistenceUnit";

	@Override
	protected void onSetUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//		entityManager = entityManagerFactory.createEntityManager();
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
//		entityManager.close();
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
		worker = workerRepository.save(worker);
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
	public void testFetchAndUpdateSuccess() {

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
		window.button(JButtonMatcher.withName("btnFetch")).click();
		window.textBox("txtCustomerName").enterText(updatedName);

		window.button(JButtonMatcher.withName("btnUpdate")).click();

		order1.setOrderId(orderId);
		order1.setCustomerName(updatedName);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(order1.toString());
		});
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
		window.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedPhone = "4401372678";
		window.textBox("txtCustomerPhone").enterText(updatedPhone);
		window.button(JButtonMatcher.withName("btnUpdate")).click();
		order1.setCustomerPhoneNumber(updatedPhone);
//		assertThat(window.list().contents()).isEqualTo(Arrays.asList(order1.toString()));
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
		order2 = orderRepository.save(order2);
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
		window.textBox("txtSearchOrder").enterText(searchText);
		int searchOptionIndex = 2;
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

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
		Worker worker = new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER);
		worker = workerRepository.save(worker);

		CustomerOrder order1 = new CustomerOrder(customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		GuiActionRunner.execute(() -> {
			orderController.createOrUpdateOrder(order1, OperationType.ADD);
			orderController.allOrders();
		});
		Long orderId = 2l;

		window.textBox("txtOrderId").enterText(orderId.toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();
		order1.setOrderId(orderId);
		window.label("showErrorNotFoundLbl")
				.requireText("Order with ID " + order1.getOrderId() + " not found.: " + null);
	}
}
