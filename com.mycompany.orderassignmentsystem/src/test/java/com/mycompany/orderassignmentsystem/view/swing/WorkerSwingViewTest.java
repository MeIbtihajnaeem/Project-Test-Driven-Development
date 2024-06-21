package com.mycompany.orderassignmentsystem.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderassignmentsystem.controller.WorkerController;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.WorkerSearchOption;
import com.mycompany.orderassignmentsystem.model.Worker;

/**
 * The Class WorkerSwingViewTest.
 */
@RunWith(GUITestRunner.class)
public class WorkerSwingViewTest extends AssertJSwingJUnitTestCase {

	/** The window. */
	private FrameFixture window;

	/** The worker swing view. */
	private WorkerSwingView workerSwingView;

	/** The worker controller. */
	@Mock
	private WorkerController workerController;

	/** The closeable. */
	private AutoCloseable closeable;

	/**
	 * On set up.
	 *
	 * @throws Exception the exception
	 */
	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			workerSwingView = new WorkerSwingView();
			workerSwingView.setWorkerController(workerController);
			return workerSwingView;
		});
		window = new FrameFixture(robot(), workerSwingView);
		window.show();

	}

	/**
	 * On tear down.
	 *
	 * @throws Exception the exception
	 */
	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	/**
	 * Test controls initial states.
	 */
	@Test
	@GUITest
	public void testControlsInitialStates() {

		// Verify Labels
		window.label(JLabelMatcher.withText("Worker ID"));
		window.label(JLabelMatcher.withText("Worker Name"));
		window.label(JLabelMatcher.withText("Worker Phone No."));
		window.label(JLabelMatcher.withText("Worker Category"));
		window.label(JLabelMatcher.withText("Search Worker"));
		window.label(JLabelMatcher.withText("Search By"));

		// Verify Error Labels
		window.label(JLabelMatcher.withName("showErrorLbl")).requireText("");
		window.label(JLabelMatcher.withName("showErrorLblSearchWorker")).requireText("");
		window.label(JLabelMatcher.withName("showErrorNotFoundLbl")).requireText("");

		// Verify TextFields
		window.textBox("txtWorkerId").requireEnabled();
		window.textBox("txtWorkerName").requireEnabled();
		window.textBox("txtWorkerPhone").requireEnabled();
		window.textBox("txtSearchWorker").requireEnabled();

		// Verify Combo Box
		window.comboBox("cmbWorkerCategory").requireEnabled();
		window.comboBox("cmbSearchByOptions").requireEnabled();

		// Verify ListLayout JList
		window.list("listWorkers");

		// Verify Buttons using text
		window.button(JButtonMatcher.withText("Manage Order")).requireEnabled();
		window.button(JButtonMatcher.withText("Fetch")).requireDisabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.button(JButtonMatcher.withText("Clear")).requireDisabled();
		window.button(JButtonMatcher.withText("Search Worker")).requireDisabled();

	}

	/**
	 * Verify that the "Add" button is disabled when the worker ID, worker name,
	 * worker phone, and worker category fields are not empty. This is because the
	 * worker ID must be null when creating a new worker.
	 */

	@Test
	public void testWhenWorkerIdWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndAddButtonShouldBeDisable() {
		window.textBox("txtWorkerId").enterText("1");
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
	}

	/**
	 * Verify that the "Add" button is enabled when the worker ID field is empty,
	 * worker name, worker phone, and worker category fields are not empty. This is
	 * because the worker ID must null when creating a new worker as it is
	 * automatically assigned by the database.
	 */
	@Test
	public void testWhenWorkerIdIsEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndAddButtonShouldBeEnable() {
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnAdd")).requireEnabled();
	}

	/**
	 * Verify that the "Search Worker" button is enabled when the search string and
	 * search option fields are not empty.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionIsNotEmptyAndSearchButtonShouldBeEnabled() {
		window.textBox("txtSearchWorker").enterText("Naeem");
		window.comboBox("cmbSearchByOptions").selectItem(0);
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireEnabled();
	}

	/**
	 * Verify that the "Search Worker" button is disabled when the search string and
	 * search option fields are empty.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionIsEmptyAndSearchButtonShouldBeDisabled() {
		window.textBox("txtSearchWorker").enterText(" ");
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireDisabled();
	}

	/**
	 * Verify that the "Clear" button is disabled when the search string and search
	 * options fields are empty.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionsIsEmptyAndClearWorkerButtonShouldBeDisabled() {
		window.textBox("txtSearchWorker").enterText(" ");
		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireDisabled();
	}

	/**
	 * Verify that the "Clear" button is enabled when the search string and search
	 * options fields are not empty.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionsIsNotEmptyAndClearWorkerButtonShouldBeEnabled() {
		window.textBox("txtSearchWorker").enterText("Naeem");
		window.comboBox("cmbSearchByOptions").selectItem(0);
		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireEnabled();
	}

	/**
	 * Verify that the "Update" button is disabled when the worker ID is empty, but
	 * the worker name, worker phone number, and worker category fields are not
	 * empty.
	 */
	@Test
	public void testWhenWorkerIdIsEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndUpdateButtonShouldBeDisable() {
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
	}

	/**
	 * Verify that the "Update" button is enabled when the worker ID, worker name,
	 * worker phone number, and worker category fields are not empty.
	 */
	@Test
	public void testWhenWorkerIdWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndUpdateButtonShouldBeEnable() {
		window.textBox("txtWorkerId").enterText("1");
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnUpdate")).requireEnabled();
	}

	/**
	 * Verify that the "Fetch" button is disabled when the worker ID is empty, but
	 * the worker name, worker phone number, and worker category fields are not
	 * empty.
	 */
	@Test
	public void testWhenWorkerIdIsEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndFetchButtonShouldBeDisable() {
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
	}

	/**
	 * Verify that the "Fetch" button is enabled when the worker ID is not empty,
	 * but the worker name, worker phone number, and worker category fields are
	 * empty.
	 */
	@Test
	public void testWhenWorkerIdIsNotEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsEmptyAndFetchButtonShouldBeEnable() {
		window.textBox("txtWorkerId").enterText("1");
		window.button(JButtonMatcher.withName("btnFetch")).requireEnabled();
		window.comboBox("cmbWorkerCategory").requireNoSelection();

	}

	/**
	 * Verify that the "Add" button is disabled when either the worker name, worker
	 * phone number, or worker category fields are blank.
	 */
	@Test
	public void testWhenEitherNameOrCategoryOrPhoneNumberAreBlankThenAddButtonShouldBeDisabled() {
		JTextComponentFixture name = window.textBox("txtWorkerName");
		JTextComponentFixture phoneNumber = window.textBox("txtWorkerPhone");
		name.enterText(" ");
		phoneNumber.enterText(" ");
		JComboBoxFixture combo = window.comboBox("cmbWorkerCategory");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
		phoneNumber.enterText("123");
		name.setText("abc");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
		phoneNumber.enterText(" ");
		name.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
	}

	/**
	 * Verify that the "Search Worker" button is disabled when either the search
	 * string or search option fields are blank.
	 */
	@Test
	public void testWhenEitherSearchStringOrSearchOptionAreBlankThenSearchWorkerButtonShouldBeDisabled() {
		JTextComponentFixture txtSearch = window.textBox("txtSearchWorker");
		txtSearch.enterText(" ");
		JComboBoxFixture combo = window.comboBox("cmbSearchByOptions");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireDisabled();
		txtSearch.enterText("1");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireDisabled();
		txtSearch.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireDisabled();
	}

	/**
	 * Verify that the "Clear Search" button is disabled when either the search
	 * string or search option fields are blank.
	 */
	@Test
	public void testWhenEitherSearchStringOrSearchOptionAreBlankThenClearSearchButtonShouldBeDisabled() {
		JTextComponentFixture txtSearch = window.textBox("txtSearchWorker");
		txtSearch.enterText(" ");
		JComboBoxFixture combo = window.comboBox("cmbSearchByOptions");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireDisabled();
		txtSearch.enterText("1");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireDisabled();
		txtSearch.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireDisabled();
	}

	/**
	 * Verify that the "Update" button is disabled when either the worker ID, worker
	 * name, worker phone number, or worker category fields are blank.
	 */
	@Test
	public void testWhenEitherNameOrCategoryOrPhoneNumberOrOrderIdAreBlankThenUpdateButtonShouldBeDisabled() {
		JTextComponentFixture orderId = window.textBox("txtWorkerId");
		JTextComponentFixture name = window.textBox("txtWorkerName");
		JTextComponentFixture phoneNumber = window.textBox("txtWorkerPhone");
		orderId.enterText("\b");
		name.enterText(" ");
		phoneNumber.enterText(" ");
		JComboBoxFixture combo = window.comboBox("cmbWorkerCategory");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
		orderId.enterText("1");
		phoneNumber.enterText("123");
		name.setText("abc");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
		orderId.enterText("\b");
		phoneNumber.enterText(" ");
		name.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
	}

	/**
	 * Verify that the "Fetch" button is disabled when either the worker ID, worker
	 * name, or worker phone number fields are blank.
	 */
	@Test
	public void testWhenEitherNameOrPhoneNumberOrOrderIdAreBlankThenFetchButtonShouldBeDisabled() {
		JTextComponentFixture orderId = window.textBox("txtWorkerId");
		JTextComponentFixture name = window.textBox("txtWorkerName");
		JTextComponentFixture phoneNumber = window.textBox("txtWorkerPhone");
		orderId.enterText("\b");
		name.enterText(" ");
		phoneNumber.enterText(" ");
		JComboBoxFixture combo = window.comboBox("cmbWorkerCategory");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
		orderId.enterText("1");
		phoneNumber.enterText("123");
		name.setText("abc");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
		orderId.enterText("\b");
		phoneNumber.enterText(" ");
		name.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
	}

	@Test
	public void testWhenWorkerIdOnlyNumbersThenFetchButtonShouldBeFetchButtonEnabled() {
		JTextComponentFixture workerId = window.textBox("txtWorkerId");
		workerId.enterText("a");
		workerId.enterText("@");
		workerId.enterText(" ");
		workerId.enterText("\0");
		workerId.enterText(":");
		workerId.enterText("0");
		workerId.enterText("\b");
		workerId.enterText("9");
		workerId.enterText("\b");
		workerId.enterText("5");
		window.button(JButtonMatcher.withName("btnFetch")).requireEnabled();

	}

	/**
	 * Verify that the "Delete" button is enabled only when a worker is selected.
	 */
	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAWorkerIsSelected() {
		JListFixture list = window.list("listWorkers");
		JButtonFixture deleteButton = window.button(JButtonMatcher.withName("btnDelete"));
		Worker worker = new Worker(1l, "Naeem Ibtihaj", "3401372678", OrderCategory.PLUMBER);

		GuiActionRunner.execute(() -> {

			workerSwingView.getWorkerListModel().addElement(worker);

		});

		list.selectItem(0);

		deleteButton.requireEnabled();
		list.clearSelection();
		deleteButton.requireDisabled();
	}

	/**
	 * Verify that the "Show All Workers" functionality adds worker descriptions to
	 * the list of workers.
	 */
	@Test
	public void testsShowAllWorkersAddWorkerDescriptionToTheListWorkers() {
		Worker worker1 = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);

		Worker worker2 = new Worker(2l, "Bob", "3401372678", OrderCategory.PLUMBER);

		GuiActionRunner.execute(() -> workerSwingView.showAllWorkers(Arrays.asList(worker1, worker2)));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(worker1.toString(), worker2.toString());
	}

	/**
	 * Verify that the "Show Error" functionality displays the message in the error
	 * label.
	 */
	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Worker worker = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.showError("error message", worker));
		window.label("showErrorLbl").requireText("error message: " + worker);
	}

	/**
	 * Verify that the "Show Search Worker Error" functionality displays the message
	 * in the error label.
	 */
	@Test
	public void testShowSearchWorkerErrorShouldShowTheMessageInTheErrorLabel() {
		String searchText = "1";
		GuiActionRunner.execute(() -> workerSwingView.showSearchError("error message", searchText));
		window.label("showErrorLblSearchWorker").requireText("error message: " + searchText);
	}

	/**
	 * Verify that the "Show Not Found Error" functionality displays the message in
	 * the error label.
	 */
	@Test
	public void testShowNotFoundErrorShouldShowTheMessageInTheErrorLabel() {
		Worker worker = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.showErrorNotFound("error message", worker));
		window.label("showErrorNotFoundLbl").requireText("error message: " + worker);
	}

	/**
	 * Verify that when a worker is added, it is added to the list of workers and
	 * the error label is reset.
	 */
	@Test
	public void testWorkerAddedShouldAddTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.workerAdded(worker));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(worker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	/**
	 * Verify that when a worker is fetched, it populates the fields with worker
	 * information and resets the error label.
	 */
	@Test
	public void testWorkerFetchShouldFetchTheWorkerToTheFieldsAndResetTheErrorLabel() {
		Worker worker = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.showFetchedWorker(worker));
		String name = window.textBox("txtWorkerName").target().getText();
		String phoneNumber = window.textBox("txtWorkerPhone").target().getText();
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getSelectedItem();

		assertThat(name).isEqualTo(worker.getWorkerName());
		assertThat(phoneNumber).isEqualTo(worker.getWorkerPhoneNumber());
		assertThat(category).isEqualTo(worker.getWorkerCategory());
		window.label("showErrorLbl").requireText(" ");
	}

	/**
	 * Verify that when a worker is modified, it updates the worker in the list of
	 * workers and resets the error label.
	 */
	@Test
	public void testWorkerModifiedShouldModifyAndAddTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.workerAdded(worker));
		Worker updatedWorker = new Worker(1l, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> workerSwingView.workerModified(updatedWorker));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(updatedWorker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	/**
	 * Verify that when a worker is modified but not found in the list, it does not
	 * modify the list of workers and resets the error label.
	 */
	@Test
	public void testWorkerModifiedShouldNotModifyAndDonotAddTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.workerAdded(worker));
		Worker updatedWorker = new Worker(2l, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> workerSwingView.workerModified(updatedWorker));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(worker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	/**
	 * Verify that when a worker is removed, it is removed from the list of workers
	 * and resets the error label.
	 */
	@Test
	public void testWorkerRemovedShouldRemoveTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);

		Worker worker2 = new Worker(1l, "Bob", "3401372679", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> {
			DefaultListModel<Worker> listOrderModel = workerSwingView.getWorkerListModel();
			listOrderModel.addElement(worker);
			listOrderModel.addElement(worker2);
		});

		GuiActionRunner.execute(() -> workerSwingView.workerRemoved(worker));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(worker2.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	/**
	 * Verify that when searching for a worker, only the searched worker is shown in
	 * the list of workers and the error label is reset.
	 */
	@Test
	public void testWorkerSearchShouldModifyAndShowOnlySearchedWorkerToTheListAndResetTheErrorLabel() {
		Worker worker1 = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker(2l, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> {
			workerSwingView.workerAdded(worker1);
			workerSwingView.workerAdded(worker2);
		});
		Worker searchWorker = new Worker(2l, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> workerSwingView.showSearchResultForWorker(Arrays.asList(searchWorker)));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(searchWorker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	@Test
	public void testManageOrderButtonHideWorkerView() {

		window.button(JButtonMatcher.withName("lblManageOrder")).click();

		assertFalse(window.target().isVisible());
	}

	/**
	 * Verify that clicking the "Add" button delegates to the worker controller to
	 * create a new worker.
	 */

	@Test
	public void testAddButtonShouldDelegateToWorkerControllerNewWorker() {
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
		verify(workerController).createOrUpdateWorker(worker, OperationType.ADD);
	}

	/**
	 * Verify that clicking the "Update" button delegates to the worker controller
	 * to update the worker.
	 */
	@Test
	public void testUpdateButtonShouldDelegateToWorkerControllerUpdateWorker() {
		Long workerId = 1l;

		String name = "Naeem";
		String updatedName = "Ibtihaj";
		String phoneNumber = "3401372678";
		int categoryIndex = 0;

		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);

		Worker worker = new Worker(workerId, name, phoneNumber, category);

		GuiActionRunner.execute(() -> {
			workerSwingView.workerAdded(worker);
		});

		window.textBox("txtWorkerId").enterText(workerId.toString());
		window.textBox("txtWorkerName").enterText(updatedName);
		window.textBox("txtWorkerPhone").enterText(phoneNumber);
		window.comboBox("cmbWorkerCategory").selectItem(categoryIndex);

		window.button(JButtonMatcher.withName("btnUpdate")).click();
		Worker updatedWorker = new Worker(workerId, updatedName, phoneNumber, category);
		verify(workerController).createOrUpdateWorker(updatedWorker, OperationType.UPDATE);
	}

	/**
	 * Verify that clicking the "Fetch" button delegates to the worker controller to
	 * fetch a worker by ID.
	 */
	@Test
	public void testFetchButtonShouldDelegateToWorkerControllerFetchWorkerById() {
		Long workerId = 1l;

		window.textBox("txtWorkerId").enterText(workerId.toString());

		window.button(JButtonMatcher.withName("btnFetch")).click();
		Worker worker = new Worker();
		worker.setWorkerId(workerId);

		verify(workerController).fetchWorkerById(worker);
	}

	/**
	 * Verify that clicking the "Search Worker" button delegates to the worker
	 * controller to search for workers by options.
	 */
	@Test
	public void testSearchWorkerButtonShouldDelegateToWorkerControllerSearchWorkerByOptions() {
		String searchText = "Ibtihaj";
		int searchOptionIndex = 1;
		WorkerSearchOption searchOption = (WorkerSearchOption) window.comboBox("cmbSearchByOptions").target()
				.getItemAt(searchOptionIndex);

		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);

		window.button(JButtonMatcher.withName("btnSearchWorker")).click();

		verify(workerController).searchWorker(searchText, searchOption);

	}

	/**
	 * Verify that clicking the "Clear Search Worker" button delegates to the worker
	 * controller to show all workers.
	 */
	@Test
	public void testClearSearchWorkerButtonShouldDelegateToWorkerControllerShowAllWorkers() {
		String searchText = "Ibtihaj";
		int searchOptionIndex = 1;
		Long workerId = 1l;
		int categoryIndex = 0;
		String name = "Naeem";
		String phoneNumber = "3401372678";
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);

		Worker worker = new Worker(workerId, name, phoneNumber, category);

		GuiActionRunner.execute(() -> {
			workerSwingView.workerAdded(worker);
		});

		window.textBox("txtSearchWorker").enterText(searchText);
		window.comboBox("cmbSearchByOptions").selectItem(searchOptionIndex);

		window.button(JButtonMatcher.withName("btnClearSearchWorker")).click();
		verify(workerController).getAllWorkers();
	}

	/**
	 * Verify that clicking the "Delete" button delegates to the worker controller
	 * to remove a worker.
	 */
	@Test
	public void testDeleteButtonShouldDelegateToWorkerControllerRemoveWorker() {
		Long workerId = 1l;
		Long workerId2 = 2l;

		String name = "Naeem";
		String name2 = "Bob";
		String phoneNumber = "3401372678";
		String phoneNumber2 = "3401372679";
		int categoryIndex = 0;

		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);

		Worker worker1 = new Worker(workerId, name, phoneNumber, category);
		Worker worker2 = new Worker(workerId2, name2, phoneNumber2, category);

		GuiActionRunner.execute(() -> {
			DefaultListModel<Worker> listOrderModel = workerSwingView.getWorkerListModel();
			listOrderModel.addElement(worker1);
			listOrderModel.addElement(worker2);
		});
		window.list("listWorkers").selectItem(1);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		verify(workerController).deleteWorker(worker2);
	}
}
