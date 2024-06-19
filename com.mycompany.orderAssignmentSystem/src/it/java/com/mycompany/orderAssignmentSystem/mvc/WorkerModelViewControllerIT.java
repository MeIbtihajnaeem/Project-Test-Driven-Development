package com.mycompany.orderAssignmentSystem.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

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

import com.mycompany.orderAssignmentSystem.controller.WorkerController;
import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.model.Worker;
import com.mycompany.orderAssignmentSystem.repository.WorkerRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderAssignmentSystem.view.swing.WorkerSwingView;

@RunWith(GUITestRunner.class)
public class WorkerModelViewControllerIT extends AssertJSwingJUnitTestCase {
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

	private WorkerRepository workerRepository;

	private WorkerSwingView workerSwingView;

	private WorkerController workerController;

	private ValidationConfigurations validationConfig;

	private FrameFixture window;
	private EntityManagerFactory entityManagerFactory;
//	private EntityManager entityManager;

	@Override
	protected void onSetUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//		entityManager = entityManagerFactory.createEntityManager();
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

	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
//		entityManager.close();
	}

	@Test
	public void testAddWorker() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
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
		Worker savedWorker = workerRepository.findByPhoneNumber(worker.getWorkerPhoneNumber());
		assertThat(workerRepository.findById(savedWorker.getWorkerId())).isEqualTo(savedWorker);

	}

	@Test
	public void testUpdateAndFetchWorker() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
		int categoryIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);

//		int updatedCategoryIndex = 1;
		window.textBox("txtWorkerId").enterText(savedWorker.getWorkerId().toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();

		String updatedName = "Ibtihaj";
		window.textBox("txtWorkerName").enterText(updatedName);

		window.button(JButtonMatcher.withName("btnUpdate")).click();

//		Worker updatedWorker = new Worker(savedWorker.getWorkerId(), updatedName, phoneNumber, category);
		savedWorker.setWorkerName(updatedName);
		// updatedWorker.setWorkerId(savedWorker.getWorkerId());
//		updatedWorker.setWorkerName(updatedName);
//		updatedWorker.setWorkerPhoneNumber(savedWorker.getWorkerPhoneNumber());
//		updatedWorker.setWorkerCategory(savedWorker.getWorkerCategory());
		assertThat(workerRepository.findById(savedWorker.getWorkerId())).isEqualTo(savedWorker);

	}

	@Test
	public void testDelete() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
		int categoryIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> workerController.getAllWorkers());
		window.list("listWorkers").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(workerRepository.findById(savedWorker.getWorkerId())).isNull();

	}

}
