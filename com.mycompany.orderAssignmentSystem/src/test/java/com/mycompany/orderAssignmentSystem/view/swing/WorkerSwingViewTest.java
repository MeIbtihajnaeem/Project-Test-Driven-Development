package com.mycompany.orderAssignmentSystem.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderAssignmentSystem.controller.WorkerController;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.enumerations.WorkerSearchOption;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;

@RunWith(GUITestRunner.class)
public class WorkerSwingViewTest extends AssertJSwingJUnitTestCase {
	private FrameFixture window;

	private WorkerSwingView workerSwingView;
	@Mock
	private WorkerController workerController;
	private AutoCloseable closeable;

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
		window.target().setExtendedState(JFrame.NORMAL);

	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

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
		window.label(JLabelMatcher.withText("Search Orders By Worker ID"));

		// Verify Error Labels
		window.label(JLabelMatcher.withName("showErrorLbl")).requireText("");
		window.label(JLabelMatcher.withName("showErrorLblSearchWorker")).requireText("");
		window.label(JLabelMatcher.withName("showErrorLblSearchOrder")).requireText("");
		window.label(JLabelMatcher.withName("showErrorNotFoundLbl")).requireText("");

		// Verify TextFields
		window.textBox("txtWorkerId").requireEnabled();
		window.textBox("txtWorkerName").requireEnabled();
		window.textBox("txtWorkerPhone").requireEnabled();
		window.textBox("txtSearchWorker").requireEnabled();
		window.textBox("txtOrdersByWorkerId").requireEnabled();

		// Verify Combo Box
		window.comboBox("cmbWorkerCategory").requireEnabled();
		window.comboBox("cmbSearchByOptions").requireEnabled();

		// Verify ListLayout JList
		window.list("listWorkers");
		window.list("listOrders");

		// Verify Buttons
		window.button(JButtonMatcher.withName("lblManageOrder")).requireEnabled();
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
		window.button(JButtonMatcher.withName("btnDelete")).requireDisabled();

		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireDisabled();
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireDisabled();
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireDisabled();

	}

	@Test
	@GUITest
	public void testWhenWorkerIdWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndAddButtonShouldBeDisable() {
		window.textBox("txtWorkerId").enterText("1");
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
	}

	@Test
	@GUITest
	public void testWhenWorkerIdIsEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndAddButtonShouldBeEnable() {
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnAdd")).requireEnabled();
	}

	@Test
	@GUITest
	public void testWhenSearchStringAndSearchOptionIsNotEmptyAndSearchButtonShouldBeEnabled() {
		window.textBox("txtSearchWorker").enterText("Naeem");
		window.comboBox("cmbSearchByOptions").selectItem(0);
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireEnabled();
	}

	@Test
	@GUITest
	public void testWhenSearchStringAndSearchOptionIsEmptyAndSearchButtonShouldBeDisabled() {
		window.textBox("txtSearchWorker").enterText(" ");
		window.button(JButtonMatcher.withName("btnSearchWorker")).requireDisabled();
	}

	@Test
	@GUITest
	public void testWhenSearchStringAndSearchOptionsIsEmptyAndClearWorkerButtonShouldBeDisabled() {
		window.textBox("txtSearchWorker").enterText(" ");
		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireDisabled();
	}

	@Test
	@GUITest
	public void testWhenSearchStringAndSearchOptionsIsNotEmptyAndClearWorkerButtonShouldBeEnabled() {
		window.textBox("txtSearchWorker").enterText("Naeem");
		window.comboBox("cmbSearchByOptions").selectItem(0);
		window.button(JButtonMatcher.withName("btnClearSearchWorker")).requireEnabled();
	}

	@Test
	@GUITest
	public void testWhenSearchOrderByWorkerIdStringIsNotEmptyAndSearchOrderButtonShouldBeEnabled() {
		window.textBox("txtOrdersByWorkerId").enterText("1");
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireEnabled();
	}

	@Test
	@GUITest
	public void testWhenSearchOrderByWorkerIdStringIsEmptyAndSearchOrderButtonShouldBeDisabled() {
		window.textBox("txtOrdersByWorkerId").enterText(" ");
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireDisabled();
	}

	@Test
	@GUITest
	public void testWhenWorkerIdIsEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndUpdateButtonShouldBeDisable() {
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
	}

	@Test
	@GUITest
	public void testWhenWorkerIdWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndUpdateButtonShouldBeEnable() {
		window.textBox("txtWorkerId").enterText("1");
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnUpdate")).requireEnabled();
	}

	@Test
	@GUITest
	public void testWhenWorkerIdIsEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndFetchButtonShouldBeDisable() {
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
	}

	@Test
	@GUITest
	public void testWhenWorkerIdIsNotEmptyWorkerNameWorkerPhoneNumberWorkerCategoryIsEmptyAndFetchButtonShouldBeEnable() {
		window.textBox("txtWorkerId").enterText("1");
		window.button(JButtonMatcher.withName("btnFetch")).requireEnabled();
		window.comboBox("cmbWorkerCategory").requireNoSelection();

	}

	@Test
	@GUITest
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

	@Test
	@GUITest
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

	@Test
	@GUITest
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

	@Test
	@GUITest
	public void testWhenEitherNameOrCategoryOrPhoneNumberOrOrderIdAreBlankThenUpdateButtonShouldBeDisabled() {
		JTextComponentFixture orderId = window.textBox("txtWorkerId");
		JTextComponentFixture name = window.textBox("txtWorkerName");
		JTextComponentFixture phoneNumber = window.textBox("txtWorkerPhone");
		orderId.enterText(" ");
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
		orderId.enterText(" ");
		phoneNumber.enterText(" ");
		name.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
	}

	@Test
	@GUITest
	public void testWhenEitherNameOrPhoneNumberOrOrderIdAreBlankThenFetchButtonShouldBeDisabled() {
		JTextComponentFixture orderId = window.textBox("txtWorkerId");
		JTextComponentFixture name = window.textBox("txtWorkerName");
		JTextComponentFixture phoneNumber = window.textBox("txtWorkerPhone");
		orderId.enterText(" ");
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
		orderId.enterText(" ");
		phoneNumber.enterText(" ");
		name.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
	}

	// Introducing a delay as GuiActionRunner.execute() functions reliably when the
	// test runs individually,
	// However, when run as part of a test collection, it fails due to an issue that
	// is currently unknown to me.
	@Test
	@GUITest
	public void testDeleteButtonShouldBeEnabledOnlyWhenAWorkerIsSelected() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
		GuiActionRunner.execute(() -> {
			workerSwingView.getWorkerListModel()
					.addElement(new Worker(1L, "Naeem Ibtihaj", "3401372678", OrderCategory.PLUMBER));
		});
		window.list("listWorkers").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withName("btnDelete"));
		deleteButton.requireEnabled();
		window.list("listWorkers").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testsShowAllWorkersAddWorkerDescriptionToTheListWorkers() {
		Worker worker1 = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);

		Worker worker2 = new Worker(2L, "Bob", "3401372678", OrderCategory.PLUMBER);

		GuiActionRunner.execute(() -> workerSwingView.showAllWorkers(Arrays.asList(worker1, worker2)));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(worker1.toString(), worker2.toString());
	}

	@Test
	public void testsShowSearchOrderAddOrderDescriptionToTheListOrders() {

		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		CustomerOrder order1 = new CustomerOrder(1l, "John", "Piazza Luigi", "3401372678", LocalDateTime.now(),
				"Plumbing required", OrderCategory.PLUMBER, OrderStatus.COMPLETED, worker);

		CustomerOrder order2 = new CustomerOrder(2l, "Bob", "Piazza Luigi", "3401372679", LocalDateTime.now(),
				"Plumbing required", OrderCategory.PLUMBER, OrderStatus.PENDING, worker);

		GuiActionRunner.execute(() -> workerSwingView.showOrderByWorkerId(Arrays.asList(order1, order2)));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.showError("error message", worker));
		window.label("showErrorLbl").requireText("error message: " + worker);
	}

	@Test
	public void testShowSearchWorkerErrorShouldShowTheMessageInTheErrorLabel() {
		String searchText = "1";
		GuiActionRunner.execute(() -> workerSwingView.showSearchError("error message", searchText));
		window.label("showErrorLblSearchWorker").requireText("error message: " + searchText);
	}

	@Test
	public void testShowNotFoundErrorShouldShowTheMessageInTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.showErrorNotFound("error message", worker));
		window.label("showErrorNotFoundLbl").requireText("error message: " + worker);
	}

	@Test
	public void testShowOrderByWorkerIdErrorShouldShowTheMessageInTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.showSearchOrderByWorkerIdError("error message", worker));
		window.label("showErrorLblSearchOrder").requireText("error message: " + worker);
	}

	@Test
	public void testWorkerAddedShouldAddTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.workerAdded(worker));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(worker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	@Test
	public void testWorkerFetchShouldFetchTheWorkerToTheFieldsAndResetTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.showFetchedWorker(worker));
		String name = window.textBox("txtWorkerName").target().getText();
		String phoneNumber = window.textBox("txtWorkerPhone").target().getText();
		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getSelectedItem();

		assertThat(name).isEqualTo(worker.getWorkerName());
		assertThat(phoneNumber).isEqualTo(worker.getWorkerPhoneNumber());
		assertThat(category).isEqualTo(worker.getWorkerCategory());
		window.label("showErrorLbl").requireText(" ");
	}

	@Test
	public void testWorkerModifiedShouldModifyAndAddTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.workerAdded(worker));
		Worker updatedWorker = new Worker(1L, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> workerSwingView.workerModified(updatedWorker));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(updatedWorker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	@Test
	public void testWorkerModifiedShouldNotModifyAndDonotAddTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		GuiActionRunner.execute(() -> workerSwingView.workerAdded(worker));
		Worker updatedWorker = new Worker(2L, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> workerSwingView.workerModified(updatedWorker));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(worker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	@Test
	public void testWorkerRemovedShouldRemoveTheWorkerToTheListAndResetTheErrorLabel() {
		Worker worker = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);

		Worker worker2 = new Worker(1L, "Bob", "3401372679", OrderCategory.ELECTRICIAN);

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

	@Test
	public void testWorkerSearchShouldModifyAndShowOnlySearchedWorkerToTheListAndResetTheErrorLabel() {
		Worker worker1 = new Worker(1L, "John", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker(2L, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> {
			workerSwingView.workerAdded(worker1);
			workerSwingView.workerAdded(worker2);
		});
		Worker searchWorker = new Worker(2L, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> workerSwingView.showSearchResultForWorker(Arrays.asList(searchWorker)));
		String[] listContents = window.list("listWorkers").contents();
		assertThat(listContents).containsExactly(searchWorker.toString());
		window.label("showErrorLbl").requireText(" ");
	}

	@Test
	public void testOrderSearchShouldModifyAndShowOnlySearchedOrdersToTheListAndResetTheErrorLabel() {
		CustomerOrder order1 = new CustomerOrder(1l, "John", "Piazza Luigi", "3401372678", LocalDateTime.now(),
				"Plumbing required", OrderCategory.PLUMBER, OrderStatus.COMPLETED, new Worker());

		CustomerOrder order2 = new CustomerOrder(2l, "Bob", "Piazza Luigi", "3401372679", LocalDateTime.now(),
				"Plumbing required", OrderCategory.PLUMBER, OrderStatus.PENDING, new Worker());

//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			Thread.currentThread().interrupt();
//			throw new RuntimeException(e);
//		}
		GuiActionRunner.execute(() -> {
			workerSwingView.getOrderListModel().addElement(order1);
			workerSwingView.getOrderListModel().addElement(order2);
		});
		CustomerOrder searchedOrder = new CustomerOrder(2l, "Bob", "Piazza Luigi", "3401372679", LocalDateTime.now(),
				"Plumbing required", OrderCategory.PLUMBER, OrderStatus.PENDING, new Worker());
		GuiActionRunner.execute(() -> workerSwingView.showOrderByWorkerId(Arrays.asList(searchedOrder)));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(searchedOrder.toString());
		window.label("showErrorLbl").requireText(" ");
	}

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
		verify(workerController).createNewWorker(worker);
	}

	@Test
	public void testUpdateButtonShouldDelegateToWorkerControllerUpdateWorker() {
		long workerId = 1L;

		String name = "Naeem";
		String updatedName = "Ibtihaj";
		String phoneNumber = "3401372678";
		int categoryIndex = 0;

		OrderCategory category = (OrderCategory) window.comboBox("cmbWorkerCategory").target().getItemAt(categoryIndex);

		Worker worker = new Worker(workerId, name, phoneNumber, category);

		GuiActionRunner.execute(() -> {
			workerSwingView.workerAdded(worker);
		});

		window.textBox("txtWorkerId").enterText(Long.toString(workerId));
		window.textBox("txtWorkerName").enterText(updatedName);
		window.textBox("txtWorkerPhone").enterText(phoneNumber);
		window.comboBox("cmbWorkerCategory").selectItem(categoryIndex);

		window.button(JButtonMatcher.withName("btnUpdate")).click();
		Worker updatedWorker = new Worker(workerId, updatedName, phoneNumber, category);
		verify(workerController).updateWorker(updatedWorker);
	}

	@Test
	public void testFetchButtonShouldDelegateToWorkerControllerFetchWorkerById() {
		Long workerId = 1l;

		window.textBox("txtWorkerId").enterText(Long.toString(workerId));

		window.button(JButtonMatcher.withName("btnFetch")).click();
		Worker worker = new Worker();
		worker.setWorkerId(workerId);

		verify(workerController).fetchWorkerById(worker);
	}

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

	@Test
	public void testClearSearchWorkerButtonShouldDelegateToWorkerControllerShowAllWorkers() {
		String searchText = "Ibtihaj";
		int searchOptionIndex = 1;
		long workerId = 1L;
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

	@Test
	public void testDeleteButtonShouldDelegateToWorkerControllerRemoveWorker() {
		long workerId = 1L;
		long workerId2 = 2L;

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

	@Test
	public void testSearcOrderButtonShouldDelegateToWorkerControllerSearchOrderByWorkerId() {
		Worker worker = new Worker();
		String workerId = "1";
		worker.setWorkerId(Long.parseLong(workerId));
		window.textBox("txtOrdersByWorkerId").enterText(workerId);

		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		verify(workerController).fetchOrdersByWorkerId(worker);

	}
}
