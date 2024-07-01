/*
 * Integration tests for the WorkerSwingView class.
 * 
 * These tests cover the following functionalities:
 * 
 * - Setting up and tearing down the test environment using Docker containers and database configurations.
 * - Creating, updating, fetching, searching, and deleting workers through the graphical user interface.
 * - Verifying successful and failed operations for adding, updating, fetching, searching, and deleting workers.
 * - Ensuring correct validation and error handling for invalid input data.
 * - Using the AssertJSwingJUnitTestCase framework for GUI testing and Awaitility for asynchronous operations.
 * 
 * The databaseConfig variable is responsible for starting the Docker container.
 * If the test is run from Eclipse, it runs the Docker container using Testcontainers.
 * If the test is run using a Maven command, it starts a Docker container without test containers.
 * 
 * @see WorkerController
 * @see WorkerRepository
 * @see WorkerView
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
import com.mycompany.orderassignmentsystem.controller.WorkerController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.swing.WorkerSwingView;

/**
 * The Class WorkerSwingViewIT.
 */
@RunWith(GUITestRunner.class)

public class WorkerSwingViewIT extends AssertJSwingJUnitTestCase {

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The worker swing view. */
	private WorkerSwingView workerSwingView;

	/** The worker controller. */
	private WorkerController workerController;

	/** The validated config. */
	private ValidationConfigurations validatedConfig;

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

	/** The worker id 1. */
	private long WORKER_ID_1 = 1l;

	/** The worker name 1. */
	private String WORKER_NAME_1 = "Bob";

	/** The worker phone 1. */
	private String WORKER_PHONE_1 = "3401372678";

	/** The worker category 1. */
	private OrderCategory WORKER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The worker id 2. */
	private Long WORKER_ID_2 = 2l;

	/** The worker name 2. */
	private String WORKER_NAME_2 = "Alic";

	/** The worker phone 2. */
	private String WORKER_PHONE_2 = "3401372679";

	/** The worker category 2. */
	private OrderCategory WORKER_CATEGORY_2 = OrderCategory.ELECTRICIAN;

	/** The selecting category index. */
	private int SELECTING_CATEGORY_INDEX = 0;

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
		validatedConfig = new ExtendedValidationConfigurations();
		GuiActionRunner.execute(() -> {
			workerSwingView = new WorkerSwingView();
			workerController = new WorkerController(workerRepository, workerSwingView, validatedConfig);
			workerSwingView.setWorkerController(workerController);
			return workerSwingView;
		});

		window = new FrameFixture(robot(), workerSwingView);
		window.show();

	}

	/**
	 * On tear down.
	 */
	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
	}

	/**
	 * Test all workers.
	 */
	@Test
	public void testAllWorkers() {
		Worker worker1 = new Worker(WORKER_ID_1, WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		Worker worker2 = new Worker(WORKER_ID_2, WORKER_NAME_2, WORKER_PHONE_2, WORKER_CATEGORY_2);
		worker1 = workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);
		GuiActionRunner.execute(() -> workerController.getAllWorkers());
		assertThat(window.list("listWorkers").contents()).containsExactly(worker1.toString(), worker2.toString());
	}

	/**
	 * Test add button success.
	 */
	@Test
	public void testAddButtonSuccess() {
		window.textBox("txtWorkerName").enterText(WORKER_NAME_1);
		window.textBox("txtWorkerPhone").enterText(WORKER_PHONE_1);
		window.comboBox("cmbWorkerCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.button(JButtonMatcher.withName("btnAdd")).click();

		Worker createdWorker = workerRepository.findAll().get(0);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list("listWorkers").contents()).containsExactly(createdWorker.toString());
		});
	}

	/**
	 * Test add button error.
	 */
	@Test
	public void testAddButtonError() {

		String phoneNumber = "4401372678";

		window.textBox("txtWorkerName").enterText(WORKER_NAME_1);
		window.textBox("txtWorkerPhone").enterText(phoneNumber);

		window.comboBox("cmbWorkerCategory").selectItem(SELECTING_CATEGORY_INDEX);

		window.button(JButtonMatcher.withName("btnAdd")).click();
		Worker worker = new Worker(WORKER_NAME_1, phoneNumber, WORKER_CATEGORY_1);

		assertThat(window.list("listWorkers").contents()).isEmpty();
		window.label("showErrorLbl")
				.requireText("The phone number must start with 3. Please provide a valid phone number.: " + worker);
	}

	/**
	 * Test delete button success.
	 */
	@Test
	public void testDeleteButtonSuccess() {
		Worker worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));
		window.list("listWorkers").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list("listWorkers").contents()).isEmpty();
		});

	}

	/**
	 * Test update button success.
	 */
	@Test
	public void testUpdateButtonSuccess() {

		Worker worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));

		window.textBox("txtWorkerId").enterText(Long.toString(WORKER_ID_1));
		window.textBox("txtWorkerPhone").enterText(WORKER_PHONE_1);
		window.comboBox("cmbWorkerCategory").selectItem(SELECTING_CATEGORY_INDEX);

		String updatedName = "Ibtihaj";

		window.textBox("txtWorkerName").enterText(updatedName);
		window.button(JButtonMatcher.withName("btnUpdate")).click();

		Worker createdWorker = workerRepository.findAll().get(0);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list("listWorkers").contents()).containsExactly(createdWorker.toString());
		});
	}

	/**
	 * Test fetch button success.
	 */
	@Test
	public void testFetchButtonSuccess() {
		Worker worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));

		window.textBox("txtWorkerId").enterText(Long.toString(WORKER_ID_1));
		window.button(JButtonMatcher.withName("btnFetch")).click();
		window.comboBox("cmbWorkerCategory").selectItem(WORKER_CATEGORY_1.name());

		assertThat(window.textBox("txtWorkerPhone").text()).isEqualTo(WORKER_PHONE_1);
		assertThat(window.textBox("txtWorkerName").text()).isEqualTo(WORKER_NAME_1);
		assertThat(window.comboBox("cmbWorkerCategory").selectedItem()).isEqualTo(WORKER_CATEGORY_1.name());

	}

	/**
	 * Test fetch button error.
	 */
	@Test
	public void testFetchButtonError() {
		Worker worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));
		Long workerId = WORKER_ID_2;

		window.textBox("txtWorkerId").enterText(workerId.toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();

		assertThat(window.textBox("txtWorkerPhone").text()).isEmpty();
		assertThat(window.textBox("txtWorkerName").text()).isEmpty();
		assertThat(window.comboBox("cmbWorkerCategory").selectedItem()).isNull();

		Worker newWorker = new Worker();
		newWorker.setWorkerId(workerId);
		window.label("showErrorNotFoundLbl")
				.requireText("Worker with id " + workerId + " Not Found.: " + newWorker.toString());

	}

	/**
	 * Test update button error.
	 */
	@Test
	public void testUpdateButtonError() {
		Worker worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		GuiActionRunner.execute(() -> workerController.createOrUpdateWorker(worker, OperationType.ADD));

		String updatedPhoneNumber = "4401372678";

		window.textBox("txtWorkerId").enterText(Long.toString(WORKER_ID_1));
		window.textBox("txtWorkerName").enterText(WORKER_NAME_1);
		window.textBox("txtWorkerPhone").enterText(updatedPhoneNumber);
		window.comboBox("cmbWorkerCategory").selectItem(SELECTING_CATEGORY_INDEX);

		window.button(JButtonMatcher.withName("btnUpdate")).click();

		worker.setWorkerId(WORKER_ID_1);
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(worker.toString());
		});

		worker.setWorkerPhoneNumber(updatedPhoneNumber);

		window.label("showErrorLbl")
				.requireText("The phone number must start with 3. Please provide a valid phone number.: " + worker);
	}

	/**
	 * Test delete button error.
	 */
	@Test
	public void testDeleteButtonError() {

		Worker worker = new Worker(WORKER_ID_1, WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		GuiActionRunner.execute(() -> workerSwingView.getWorkerListModel().addElement(worker));
		window.list("listWorkers").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(window.list("listWorkers").contents()).containsExactly(worker.toString());
		window.label("showErrorNotFoundLbl")
				.requireText("No Worker found with ID: " + worker.getWorkerId() + ": " + worker);

	}

	/**
	 * Test search worker success.
	 */
	@Test
	public void testSearchWorkerSuccess() {
		Worker worker1 = new Worker(WORKER_ID_1, WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		Worker worker2 = new Worker(WORKER_ID_2, WORKER_NAME_2, WORKER_PHONE_2, WORKER_CATEGORY_2);

		workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);

		GuiActionRunner.execute(() -> workerController.getAllWorkers());

		String searchText = WORKER_NAME_2;
		int searchOptionIndex = 2;

		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchWorker")).click();

		assertThat(window.list("listWorkers").contents()).containsExactly(worker2.toString());

	}

	/**
	 * Test search worker error.
	 */
	@Test
	public void testSearchWorkerError() {
		Worker worker1 = new Worker(WORKER_ID_1, WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		Worker worker2 = new Worker(WORKER_ID_2, WORKER_NAME_2, WORKER_PHONE_2, WORKER_CATEGORY_2);

		worker1 = workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);

		GuiActionRunner.execute(() -> workerController.getAllWorkers());
		String searchText = WORKER_NAME_1;
		int searchOptionIndex = 1;

		window.textBox("txtSearchWorker").deleteText();
		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchWorker")).click();

		assertThat(window.list().contents()).containsExactly(worker1.toString(), worker2.toString());
		window.label("showErrorLblSearchWorker").requireText("Please enter a valid number.: " + searchText);
	}

	/**
	 * Test clear search.
	 */
	@Test
	public void testClearSearch() {
		Worker worker1 = new Worker(WORKER_ID_1, WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		Worker worker2 = new Worker(WORKER_ID_2, WORKER_NAME_2, WORKER_PHONE_2, WORKER_CATEGORY_2);

		worker1 = workerRepository.save(worker1);
		worker2 = workerRepository.save(worker2);

		GuiActionRunner.execute(() -> workerController.getAllWorkers());

		String searchText = WORKER_NAME_2;
		int searchOptionIndex = 2;

		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);
		window.button(JButtonMatcher.withName("btnSearchWorker")).click();

		assertThat(window.list("listWorkers").contents()).containsExactly(worker2.toString());

		window.button(JButtonMatcher.withName("btnClearSearchWorker")).click();

		assertThat(window.list("listWorkers").contents()).containsExactly(worker1.toString(), worker2.toString());

	}
}
