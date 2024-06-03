package com.mycompany.orderAssignmentSystem.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycompany.orderAssignmentSystem.controller.WorkerController;
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
import com.mycompany.orderAssignmentSystem.view.swing.WorkerSwingView;

@RunWith(GUITestRunner.class)

public class WorkerSwingViewIT extends AssertJSwingJUnitTestCase {

	private WorkerRepository workerRepository;

	private WorkerSwingView workerSwingView;

	private WorkerController workerController;
	private ValidationConfigurations validatedConfig;

	private FrameFixture window;
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private OrderRepository orderRepository;
	private static final String PERSISTENCE_UNIT_NAME = "test_myPersistenceUnit";

	@Override
	protected void onSetUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();
		workerRepository = new WorkerDatabaseRepository(entityManager);
		validatedConfig = new ExtendedValidationConfigurations();
		orderRepository = new OrderDatabaseRepository(entityManager);
//		for (Worker worker : workerRepository.findAll()) {
//			workerRepository.delete(worker);
//		}

		GuiActionRunner.execute(() -> {
			workerSwingView = new WorkerSwingView();
			workerController = new WorkerController(workerRepository, workerSwingView, validatedConfig);
			workerSwingView.setWorkerController(workerController);
			return workerSwingView;
		});

		window = new FrameFixture(robot(), workerSwingView);
		window.show();

	}

	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

	@Test
	public void testAllWorkers() {
		Worker worker1 = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker(2l, "Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);
		GuiActionRunner.execute(() -> workerController.getAllWorkers());
		assertThat(window.list("listWorkers").contents()).containsExactly(worker1.toString(), worker2.toString());
	}

	@Test
	public void testAddButtonSuccess() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
		int categoryIndex = 0;
		window.textBox("txtWorkerName").enterText(name);
		window.textBox("txtWorkerPhone").enterText(phoneNumber);

		window.comboBox("cmbWorkerCategory").selectItem(categoryIndex);
		window.button(JButtonMatcher.withName("btnAdd")).click();
		Worker createdWorker = workerRepository.findAll().get(0);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list("listWorkers").contents()).containsExactly(createdWorker.toString());
		});
	}

	@Test
	@GUITest
	public void testAddButtonError() {
		String name = "Naeem";
		String phoneNumber = "4401372678";
		int categoryIndex = 0;
		window.textBox("txtWorkerName").enterText(name);
		window.textBox("txtWorkerPhone").enterText(phoneNumber);

		window.comboBox("cmbWorkerCategory").selectItem(categoryIndex);
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);

		window.button(JButtonMatcher.withName("btnAdd")).click();
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);

		assertThat(window.list("listWorkers").contents()).isEmpty();
		window.label("showErrorLbl")
				.requireText("The phone number must start with 3. Please provide a valid phone number.: " + worker);
	}

	@Test
	@GUITest
	public void testDeleteButtonSuccess() {
		int categoryIndex = 0;
		String name = "Naeem";
		String phoneNumber = "3401372678";
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));
		window.list("listWorkers").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list("listWorkers").contents()).isEmpty();
		});

	}

	@Test
	@GUITest
	public void testFetchAndUpdateButtonSuccess() {
		int categoryIndex = 0;
		String name = "Naeem";
		String phoneNumber = "3401372678";
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));
		window.textBox("txtWorkerId").enterText("1");
		window.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedName = "Ibtihaj";
		window.textBox("txtWorkerName").enterText(updatedName);
		window.button(JButtonMatcher.withName("btnUpdate")).click();

		Worker createdWorker = workerRepository.findAll().get(0);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list("listWorkers").contents()).containsExactly(createdWorker.toString());
		});
	}

	@Test
	@GUITest
	public void testUpdateButtonError() {
		int categoryIndex = 0;
		String name = "Naeem";
		String phoneNumber = "3401372678";
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);
		Worker worker = new Worker();

		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));
		worker.setWorkerId(1l);
		window.textBox("txtWorkerId").enterText("1");
		window.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedPhoneNumber = "4401372678";
		window.textBox("txtWorkerPhone").enterText(updatedPhoneNumber);
		window.button(JButtonMatcher.withName("btnUpdate")).click();
//		assertThat(window.list("listWorkers").contents()).isEqualTo(worker.toString());

		window.label("showErrorLbl")
				.requireText("The phone number must start with 3. Please provide a valid phone number.: " + worker);
	}

	// TODO: fetch Button Error

	@Test
	@GUITest
	public void testDeleteButtonError() {
		int categoryIndex = 0;
		String name = "Naeem";
		String phoneNumber = "4401372678";
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);
		Long workerId = 1l;
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		worker.setWorkerId(workerId);
		GuiActionRunner.execute(() -> workerSwingView.getWorkerListModel().addElement(worker));
		window.list("listWorkers").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(window.list("listWorkers").contents()).containsExactly(worker.toString());
		window.label("showErrorNotFoundLbl")
				.requireText("No Worker found with ID: " + worker.getWorkerId() + ": " + worker);

	}

	// Search worker button success
	@Test
	public void testSearchWorkerSuccess() {
		Worker worker1 = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker(2l, "Ibtihaj", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);
		GuiActionRunner.execute(() -> workerController.getAllWorkers());
		String searchText = "Ibtihaj";
		int searchOptionIndex = 2;

		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchWorker")).click();

		assertThat(window.list("listWorkers").contents()).containsExactly(worker2.toString());

	}

	// Search worker button error

	@Test
	public void testSearchWorkerError() {
		Worker worker1 = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker(2l, "Ibtihaj", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);
		GuiActionRunner.execute(() -> workerController.getAllWorkers());
		String searchText = "Ibtihaj";
		int searchOptionIndex = 1;

		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchWorker")).click();
		window.label("showErrorLblSearchWorker").requireText("Please enter a valid number.: " + searchText);

	}

	// Clear Search worker button

	@Test
	public void testClearSearch() {
		Worker worker1 = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker(2l, "Ibtihaj", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);
		GuiActionRunner.execute(() -> workerController.getAllWorkers());
		String searchText = "Ibtihaj";
		int searchOptionIndex = 2;
		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchWorker")).click();
		assertThat(window.list("listWorkers").contents()).containsExactly(worker2.toString());

		window.button(JButtonMatcher.withName("btnClearSearchWorker")).click();

		assertThat(window.list("listWorkers").contents()).containsExactly(worker1.toString(), worker2.toString());

	}

	// Search Order by worker button success

	@Test
	public void testSearchOrderByWorkerIdSuccess() {
		Worker worker1 = new Worker();
		worker1.setWorkerName("John");
		worker1.setWorkerPhoneNumber("3401372678");
		worker1.setWorkerCategory(OrderCategory.PLUMBER);

		worker1 = workerRepository.save(worker1);
		System.out.println("Created Worker");
		System.out.println(worker1.toString());
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(customerName);
		order.setCustomerAddress(customerAddress);
		order.setCustomerPhoneNumber(customerPhone); // Assuming the parameter name is customerPhone
		order.setAppointmentDate(appointmentDate);
		order.setOrderDescription(orderDescription);
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setWorker(worker1);

		order = orderRepository.save(order);
		System.out.println("Created Order");
		System.out.println(order.toString());
//		worker2.setOrders(Arrays.asList(order));
//		worker2 = workerRepository.save(worker2);

		window.textBox("txtOrdersByWorkerId").enterText(worker1.getWorkerId().toString());

		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		assertThat(window.list("listOrders").contents()).containsExactly(order.toString());

	}

	// Search Order by worker button error
}
