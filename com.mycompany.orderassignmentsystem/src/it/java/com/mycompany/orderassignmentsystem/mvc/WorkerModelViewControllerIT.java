/*
 * Integration tests for the WorkerModelViewControllerIT class.
 *
 * These tests verify the integration between the WorkerController, WorkerSwingView,
 * and the underlying repositories. The tests cover scenarios such as adding, updating,
 * fetching, and deleting workers. The tests utilize AssertJ Swing for GUI testing,
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
 * @see WorkerController
 * @see WorkerRepository
 * @see WorkerSwingView
 */

package com.mycompany.orderassignmentsystem.mvc;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.swing.WorkerSwingView;

/**
 * The Class WorkerModelViewControllerIT.
 */
@RunWith(GUITestRunner.class)
public class WorkerModelViewControllerIT extends AssertJSwingJUnitTestCase {

	/** The worker repository. */
	private WorkerRepository workerRepository;

	/** The worker swing view. */
	private WorkerSwingView workerSwingView;

	/** The worker controller. */
	private WorkerController workerController;

	/** The validation config. */
	private ValidationConfigurations validationConfig;

	/** The window. */
	private FrameFixture window;

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

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
		validationConfig = new ExtendedValidationConfigurations();

		GuiActionRunner.execute(() -> {
			workerSwingView = new WorkerSwingView();
			workerController = new WorkerController(workerRepository, workerSwingView, validationConfig);
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
	 * Test add worker.
	 */
	@Test
	public void testAddWorker() {
		// Setup & Exercise
		window.textBox("txtWorkerName").enterText(WORKER_NAME_1);
		window.textBox("txtWorkerPhone").enterText(WORKER_PHONE_1);
		window.comboBox("cmbWorkerCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.button(JButtonMatcher.withName("btnAdd")).click();

		// Verify
		Worker savedWorker = workerRepository.findByPhoneNumber(WORKER_PHONE_1);
		assertThat(workerRepository.findById(savedWorker.getWorkerId())).isEqualTo(savedWorker);

	}

	/**
	 * Test update and fetch worker.
	 */
	@Test
	public void testUpdateAndFetchWorker() {
		// This method first save worker in the database then fetch the
		// worker by id using fetch button and then
		// edit the worker name using update button
		// both fetch and update are by button clicked

		// Setup
		Worker worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		worker = workerRepository.save(worker);

		// Exercise
		window.textBox("txtWorkerId").enterText(worker.getWorkerId().toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedName = "Ibtihaj";
		window.textBox("txtWorkerName").enterText(updatedName);
		window.button(JButtonMatcher.withName("btnUpdate")).click();

		// Verify
		worker.setWorkerName(updatedName);
		assertThat(workerRepository.findById(worker.getWorkerId())).isEqualTo(worker);

	}

	/**
	 * Test delete.
	 */
	@Test
	public void testDelete() {
		// Setup
		Worker worker = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		worker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> workerController.getAllWorkers());

		// Exercise
		window.list("listWorkers").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();

		// Verify
		assertThat(workerRepository.findById(worker.getWorkerId())).isNull();

	}

}
