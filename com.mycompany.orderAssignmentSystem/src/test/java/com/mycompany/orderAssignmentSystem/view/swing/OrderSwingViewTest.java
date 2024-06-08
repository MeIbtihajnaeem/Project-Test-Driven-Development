package com.mycompany.orderAssignmentSystem.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

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

import com.mycompany.orderAssignmentSystem.controller.OrderController;
import com.mycompany.orderAssignmentSystem.enumerations.OperationType;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderSearchOptions;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;

@RunWith(GUITestRunner.class)

public class OrderSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private OrderSwingView orderSwingView;
	@Mock
	private OrderController orderController;

	private AutoCloseable closeable;

//	private List<Worker> workers;

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderSwingView.setOrderController(orderController);

			return orderSwingView;
		});
		window = new FrameFixture(robot(), orderSwingView);
		window.show();

		// Creating worker list since almost all the tests will use this worker list to
		// populate the combo box
//		workers = new ArrayList<Worker>();
//		workers.add(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
//		workers.add(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
//		workers.add(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));

	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {
		// verify Labels
		window.label(JLabelMatcher.withText("Order ID"));
		window.label(JLabelMatcher.withText("Customer Name"));
		window.label(JLabelMatcher.withText("Customer Address"));
		window.label(JLabelMatcher.withText("Customer Phone #"));
		window.label(JLabelMatcher.withText("Order Description"));
		window.label(JLabelMatcher.withText("Appointment Date"));
		window.label(JLabelMatcher.withText("Order Category"));
		window.label(JLabelMatcher.withText("Order Status"));
		window.label(JLabelMatcher.withText("Worker"));
		window.label(JLabelMatcher.withText("Search Order"));
		window.label(JLabelMatcher.withText("Search By"));

		// Verify Error Labels
		window.label(JLabelMatcher.withName("showError")).requireText("");
		window.label(JLabelMatcher.withName("showSearchErrorLbl")).requireText("");
		window.label(JLabelMatcher.withName("showErrorNotFoundLbl")).requireText("");

		// Verify TextFields
		window.textBox("txtOrderId").requireEnabled();
		window.textBox("txtCustomerName").requireEnabled();
		window.textBox("txtCustomerAddress").requireEnabled();
		window.textBox("txtCustomerPhone").requireEnabled();
		window.textBox("txtOrderDescription").requireEnabled();
		window.textBox("txtSearchOrder").requireEnabled();
		window.textBox("txtSelectedDate").requireEnabled();

		// verify combo box
		window.comboBox("cmbOrderCategory").requireEnabled();
		window.comboBox("cmbOrderStatus").requireEnabled();
		window.comboBox("cmbWorker").requireEnabled();
		window.comboBox("cmbSearchBy").requireEnabled();

		// verify buttons using text
		window.button(JButtonMatcher.withText("Manage Worker")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Fetch")).requireDisabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Search")).requireDisabled();

		window.button(JButtonMatcher.withText("Clear")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();

		// verify lists
		window.list("listOrders");

	}

	@Test
	public void testWhenOrderIdIsEmptyCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndAddButtonShouldBeEnable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();

		});

		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12/12/20224";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
//		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
//		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		window.textBox("txtOrderId").enterText(" "); // Make sure orderId is empty
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);
		window.button(JButtonMatcher.withName("btnAdd")).requireEnabled();
	}

	@Test
	public void testWhenOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndAddButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		Long orderId = 1l;
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12/12/20224";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
//		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
//		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		window.textBox("txtOrderId").enterText(orderId.toString());
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
	}

	@Test
	public void testWhenOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndUpdateButtonShouldBeEnable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		Long orderId = 1l;
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12/12/20224";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
//		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
//		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		window.textBox("txtOrderId").enterText(orderId.toString());
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);
		window.button(JButtonMatcher.withName("btnUpdate")).requireEnabled();
	}

	@Test
	public void testWhenOrderIdIsEmptyButCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndUpdateButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12/12/20224";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
//		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
//		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
	}

	@Test
	public void testWhenOrderIdIsNotEmptyButCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsEmptyAndFetchButtonShouldBeEnable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		Long orderId = 1l;
		window.textBox("txtOrderId").enterText(orderId.toString());
		window.textBox("txtCustomerName").enterText(" ");
		window.textBox("txtCustomerAddress").enterText(" ");
		window.textBox("txtCustomerPhone").enterText(" ");
		window.textBox("txtOrderDescription").enterText(" ");
		window.textBox("txtSelectedDate").enterText(" ");
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);
		window.comboBox("cmbOrderCategory").clearSelection();
		window.comboBox("cmbOrderStatus").clearSelection();
		window.comboBox("cmbWorker").clearSelection();
		window.button(JButtonMatcher.withName("btnFetch")).requireEnabled();
		window.comboBox("cmbOrderCategory").requireNoSelection();
		window.comboBox("cmbOrderStatus").requireNoSelection();
		window.comboBox("cmbWorker").requireNoSelection();

	}

	@Test
	public void testWhenOrderIdIsEmptyButCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndFetchButtonShouldBeDisabled() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		String orderId = "";
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12/12/20224";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
//		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
//		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		window.textBox("txtOrderId").enterText(orderId);
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
	}

	@Test
	public void testWhenSearchStringAndSearchOptionIsNotEmptyAndSearchButtonShouldBeEnabled() {
		window.textBox("txtSearchOrder").enterText("Naeem");
		window.comboBox("cmbSearchBy").selectItem(0);
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireEnabled();
	}

	@Test
	public void testWhenSearchStringAndSearchOptionIsEmptyAndSearchButtonShouldBeDisabled() {
		window.textBox("txtSearchOrder").enterText(" ");
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireDisabled();
	}

	@Test
	public void testWhenSearchStringAndSearchOptionsIsEmptyAndClearWorkerButtonShouldBeDisabled() {
		window.textBox("txtSearchOrder").enterText(" ");
		window.button(JButtonMatcher.withName("btnClearSearch")).requireDisabled();
	}

	@Test
	public void testWhenSearchStringAndSearchOptionsIsNotEmptyAndClearWorkerButtonShouldBeEnabled() {
		window.textBox("txtSearchOrder").enterText("Naeem");
		window.comboBox("cmbSearchBy").selectItem(0);
		window.button(JButtonMatcher.withName("btnClearSearch")).requireEnabled();
	}

	@Test
	public void testWhenEitherCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerAreBlankAndAddButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		JTextComponentFixture orderId = window.textBox("txtOrderId");
		JTextComponentFixture name = window.textBox("txtCustomerName");
		JTextComponentFixture address = window.textBox("txtCustomerAddress");
		JTextComponentFixture phone = window.textBox("txtCustomerPhone");
		JTextComponentFixture description = window.textBox("txtOrderDescription");
		JTextComponentFixture txtDate = window.textBox("txtSelectedDate");

		orderId.enterText("");
		name.enterText(" ");
		address.enterText(" ");
		phone.enterText(" ");
		description.enterText(" ");
		txtDate.enterText(" ");
		JComboBoxFixture category = window.comboBox("cmbOrderCategory");
		JComboBoxFixture status = window.comboBox("cmbOrderStatus");
		JComboBoxFixture worker = window.comboBox("cmbWorker");
		category.selectItem(0);
		status.selectItem(0);
		worker.selectItem(0);
		category.clearSelection();
		status.clearSelection();
		worker.clearSelection();
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
		orderId.enterText("1");
		name.enterText("name");
		address.enterText("address");
		phone.enterText("phone");
		description.enterText("description");
		txtDate.enterText("12/12/2024");
		category.selectItem(0);
		status.selectItem(0);
		worker.selectItem(0);
		category.clearSelection();
		status.clearSelection();
		worker.clearSelection();
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
		orderId.enterText("\b");
		name.enterText(" ");
		address.enterText(" ");
		phone.enterText(" ");
		description.enterText(" ");
		txtDate.enterText(" ");
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
	}

	@Test
	public void testWhenEitherOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerAreBlankAndUpdateButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		JTextComponentFixture orderId = window.textBox("txtOrderId");
		JTextComponentFixture name = window.textBox("txtCustomerName");
		JTextComponentFixture address = window.textBox("txtCustomerAddress");
		JTextComponentFixture phone = window.textBox("txtCustomerPhone");
		JTextComponentFixture description = window.textBox("txtOrderDescription");
		JTextComponentFixture txtDate = window.textBox("txtSelectedDate");

		orderId.enterText(" ");
		name.enterText(" ");
		address.enterText(" ");
		phone.enterText(" ");
		description.enterText(" ");
		txtDate.enterText(" ");
		JComboBoxFixture category = window.comboBox("cmbOrderCategory");
		JComboBoxFixture status = window.comboBox("cmbOrderStatus");
		JComboBoxFixture worker = window.comboBox("cmbWorker");
		category.clearSelection();
		status.clearSelection();
		worker.clearSelection();
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
		orderId.enterText("1");
		name.enterText("name");
		address.enterText("address");
		phone.enterText("phone");
		description.enterText("description");
		txtDate.enterText("12/12/2024");

		category.clearSelection();
		status.clearSelection();
		worker.clearSelection();
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
		orderId.enterText(" ");
		name.enterText(" ");
		address.enterText(" ");
		phone.enterText(" ");
		description.enterText(" ");
		txtDate.enterText(" ");
		category.selectItem(0);
		status.selectItem(0);
		worker.selectItem(0);
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
	}

	@Test
	public void testWhenEitherOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerAreBlankAndFetchButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN));
			orderSwingView.getWorkerListModel()
					.addElement(new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN));
			orderSwingView.resetAllFields();
		});
		JTextComponentFixture orderId = window.textBox("txtOrderId");
		JTextComponentFixture name = window.textBox("txtCustomerName");
		JTextComponentFixture address = window.textBox("txtCustomerAddress");
		JTextComponentFixture phone = window.textBox("txtCustomerPhone");
		JTextComponentFixture description = window.textBox("txtOrderDescription");
		JTextComponentFixture txtDate = window.textBox("txtSelectedDate");

		orderId.enterText(" ");
		name.enterText(" ");
		address.enterText(" ");
		phone.enterText(" ");
		description.enterText(" ");
		txtDate.enterText(" ");
		JComboBoxFixture category = window.comboBox("cmbOrderCategory");
		JComboBoxFixture status = window.comboBox("cmbOrderStatus");
		JComboBoxFixture worker = window.comboBox("cmbWorker");
		category.selectItem(0);
		status.selectItem(0);
		worker.selectItem(0);
		category.clearSelection();
		status.clearSelection();
		worker.clearSelection();
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
		orderId.enterText("1l");
		name.enterText("name");
		address.enterText("address");
		phone.enterText("phone");
		description.enterText("description");
		txtDate.enterText("12/12/2024");
		category.selectItem(0);
		status.selectItem(0);
		worker.selectItem(0);
		category.clearSelection();
		status.clearSelection();
		worker.clearSelection();
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
		orderId.enterText("\b");
		name.enterText(" ");
		address.enterText(" ");
		phone.enterText(" ");
		description.enterText(" ");
		txtDate.enterText(" ");
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
	}

	@Test
	public void testWhenWorkerIdOnlyNumbersThenFetchButtonShouldBeFetchButtonEnabled() {
		JTextComponentFixture orderId = window.textBox("txtOrderId");
		orderId.enterText("a");
		orderId.enterText("@");
		orderId.enterText(" ");
		orderId.enterText("\0");
		orderId.enterText(":");
		orderId.enterText("0");
		orderId.enterText("\b");
		orderId.enterText("9");
		orderId.enterText("\b");
		orderId.enterText("5");
		window.button(JButtonMatcher.withName("btnFetch")).requireEnabled();

	}

	@Test
	public void testWhenEitherSearchStringOrSearchOptionAreBlankThenSearchButtonShouldBeDisabled() {
		JTextComponentFixture txtSearch = window.textBox("txtSearchOrder");
		txtSearch.enterText(" ");
		JComboBoxFixture combo = window.comboBox("cmbSearchBy");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireDisabled();
		txtSearch.enterText("1l");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireDisabled();
		txtSearch.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireDisabled();
	}

	@Test
	public void testWhenEitherSearchStringOrSearchOptionAreBlankThenClearSearchButtonShouldBeDisabled() {
		JTextComponentFixture txtSearch = window.textBox("txtSearchOrder");
		txtSearch.enterText(" ");
		JComboBoxFixture combo = window.comboBox("cmbSearchBy");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnClearSearch")).requireDisabled();
		txtSearch.enterText("1l");
		combo.clearSelection();
		window.button(JButtonMatcher.withName("btnClearSearch")).requireDisabled();
		txtSearch.setText(" ");
		combo.selectItem(0);
		window.button(JButtonMatcher.withName("btnClearSearch")).requireDisabled();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAOrderIsSelected() {
		JListFixture list = window.list("listOrders");
		JButtonFixture deleteButton = window.button(JButtonMatcher.withName("btnDelete"));
		CustomerOrder order = new CustomerOrder();
		GuiActionRunner.execute(() -> {
			orderSwingView.getOrderListModel().addElement(order);
		});

		list.selectItem(0);

		deleteButton.requireEnabled();
		list.clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testsShowAllOrdersAddOrderDescriptionToTheListOrders() {
		String appointmentDate = "12-12-2024";

		CustomerOrder order1 = new CustomerOrder(1l, "Jhon", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, new Worker());
		CustomerOrder order2 = new CustomerOrder(2l, "Alic", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, new Worker());

		GuiActionRunner.execute(() -> orderSwingView.showAllOrder(Arrays.asList(order1, order2)));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());
	}

	@Test
	public void testsShowAllWorkersAddWorkerDescriptionToTheWorkerComboBox() {
		Worker worker1 = new Worker(1l, "John", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker(2l, "Bob", "3401372678", OrderCategory.ELECTRICIAN);

		GuiActionRunner.execute(() -> orderSwingView.showAllWorkers(Arrays.asList(worker1, worker2)));
		String[] listContents = window.comboBox("cmbWorker").contents();
		assertThat(listContents).containsExactly(worker1.toString(), worker2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		CustomerOrder order = new CustomerOrder();
		GuiActionRunner.execute(() -> orderSwingView.showError("error message", order));
		window.label("showError").requireText("error message: " + order);
	}

	@Test
	public void testShowSearchErrorShouldShowTheMessageInTheErrorLabel() {
		String searchText = "1l";
		GuiActionRunner.execute(() -> orderSwingView.showSearchError("error message", searchText));
		window.label("showSearchErrorLbl").requireText("error message: " + searchText);
	}

	@Test
	public void testShowNotFoundErrorShouldShowTheMessageInTheErrorLabel() {
		CustomerOrder order = new CustomerOrder();
		GuiActionRunner.execute(() -> orderSwingView.showErrorNotFound("error message", order));
		window.label("showErrorNotFoundLbl").requireText("error message: " + order);
	}

	@Test
	public void testOrderAddedShouldAddTheOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(1l);
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		GuiActionRunner.execute(() -> orderSwingView.orderAdded(order));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order.toString());
		window.label("showError").requireText(" ");
	}

	@Test
	public void testOrderModifiedShouldModifyAndAddTheOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(1l);
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		GuiActionRunner.execute(() -> orderSwingView.orderAdded(order));
		order.setOrderStatus(OrderStatus.CANCELLED);
		GuiActionRunner.execute(() -> orderSwingView.orderModified(order));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order.toString());
		window.label("showError").requireText(" ");
	}

	@Test
	public void testOrderModifiedShouldNotModifyAndAddTheOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(1l);
		order.setOrderCategory(OrderCategory.PLUMBER);
		order.setOrderStatus(OrderStatus.PENDING);
		GuiActionRunner.execute(() -> orderSwingView.orderAdded(order));
		CustomerOrder newOrder = new CustomerOrder();
		newOrder.setOrderId(2l);
		newOrder.setOrderCategory(OrderCategory.PLUMBER);
		newOrder.setOrderStatus(OrderStatus.PENDING);
		GuiActionRunner.execute(() -> orderSwingView.orderModified(newOrder));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order.toString());
		window.label("showError").requireText(" ");
	}

	@Test
	public void testOrderFetchShouldFetchTheOrderToTheFieldsAndResetTheErrorLabel() {
		Worker worker = new Worker();
		worker.setWorkerId(1l);
		worker.setWorkerName("Alic");
		worker.setWorkerCategory(OrderCategory.PLUMBER);
		CustomerOrder order = new CustomerOrder(1l, "Jhon", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, worker);
		GuiActionRunner.execute(() -> {
			orderSwingView.showAllWorkers(Arrays.asList(worker));
			orderSwingView.showFetchedOrder(order);

		});

		String orderId = window.textBox("txtOrderId").target().getText();
		String name = window.textBox("txtCustomerName").target().getText();
		String address = window.textBox("txtCustomerAddress").target().getText();
		String phone = window.textBox("txtCustomerPhone").target().getText();
		String description = window.textBox("txtOrderDescription").target().getText();
		String date = window.textBox("txtSelectedDate").target().getText();
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getSelectedItem();
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getSelectedItem();
		Worker selectedWorker = (Worker) window.comboBox("cmbWorker").target().getSelectedItem();

		assertThat(orderId).isEqualTo(order.getOrderId().toString());
		assertThat(name).isEqualTo(order.getCustomerName());
		assertThat(address).isEqualTo(order.getCustomerAddress());
		assertThat(phone).isEqualTo(order.getCustomerPhoneNumber());
		assertThat(description).isEqualTo(order.getOrderDescription());
		assertThat(date).isEqualTo(order.getAppointmentDate().toString());
		assertThat(category).isEqualTo(order.getOrderCategory());
		assertThat(status).isEqualTo(order.getOrderStatus());
		assertThat(selectedWorker).isEqualTo(order.getWorker());
		window.label("showError").requireText(" ");
	}

	@Test
	public void testOrderRemovedShouldRemoveTheOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order1 = new CustomerOrder(1l, "Jhon", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, new Worker());
		CustomerOrder order2 = new CustomerOrder(2l, "Alic", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, new Worker());

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order1);
			orderSwingView.orderAdded(order2);
		});

		GuiActionRunner.execute(() -> orderSwingView.orderRemoved(order1));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order2.toString());
		window.label("showError").requireText(" ");
	}

	@Test
	public void testOrderSearchShouldModifyAndShowOnlySearchedOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order1 = new CustomerOrder(1l, "Jhon", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, new Worker());
		CustomerOrder order2 = new CustomerOrder(2l, "Alic", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, new Worker());

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order1);
			orderSwingView.orderAdded(order2);
		});

		GuiActionRunner.execute(() -> orderSwingView.showSearchResultForOrder(Arrays.asList(order2)));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order2.toString());
		window.label("showSearchErrorLbl").requireText(" ");
	}

	@Test
	public void testAddButtonShouldDelegateToOrderControllerCreateOrUpdateOrder() {
		Worker worker = new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER);

		GuiActionRunner.execute(() -> {
			orderSwingView.showAllWorkers(Arrays.asList(worker));
		});
//		String orderId = 1l;
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);

		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);

		window.button(JButtonMatcher.withName("btnAdd")).click();

		CustomerOrder order = new CustomerOrder(null, customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, category, status, worker);
		verify(orderController).createOrUpdateOrder(order, OperationType.ADD);
	}

	@Test
	public void testUpdateButtonShouldDelegateToOrderControllerCreateOrUpdateOrder() {
		Worker worker = new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER);

		GuiActionRunner.execute(() -> {
			orderSwingView.showAllWorkers(Arrays.asList(worker));
		});
		Long orderId = 1l;
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		int workerIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);

		window.textBox("txtOrderId").enterText(orderId.toString());
		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(workerIndex);

		window.button(JButtonMatcher.withName("btnUpdate")).click();

		CustomerOrder order = new CustomerOrder(orderId, customerName, customerAddress, customerPhone, appointmentDate,
				orderDescription, category, status, worker);
		verify(orderController).createOrUpdateOrder(order, OperationType.UPDATE);
	}

	@Test
	public void testFetchButtonShouldDelegateToOrderControllerFetchOrderById() {
		Long orderId = 1l;

		window.textBox("txtOrderId").enterText(orderId.toString());

		window.button(JButtonMatcher.withName("btnFetch")).click();
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(orderId);

		verify(orderController).fetchOrderById(order);

	}

	@Test
	public void testSearchOrderButtonShouldDelegateToWorkerControllerSearchOrderByOptions() {
		Long orderId = 1l;
		int searchOptionIndex = 1;
		OrderSearchOptions searchOption = (OrderSearchOptions) window.comboBox("cmbSearchBy").target()
				.getItemAt(searchOptionIndex);

		window.textBox("txtSearchOrder").enterText(orderId.toString());
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);

		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		verify(orderController).searchOrder(orderId.toString(), searchOption);

	}

	@Test
	public void testClearSearchButtonShouldDelegateToOrderControllerShowAllOrders() {
		Long orderId = 1l;

		CustomerOrder order = new CustomerOrder(1l, "Jhon", "address", "phone", "12-12-2024", "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, new Worker());

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order);
		});
		int searchOptionIndex = 1;
		window.textBox("txtSearchOrder").enterText(orderId.toString());
		window.comboBox("cmbSearchBy").selectItem(searchOptionIndex);

		window.button(JButtonMatcher.withName("btnClearSearch")).click();
		verify(orderController).allOrders();
	}

	@Test
	public void testDeleteButtonShouldDelegateToOrderControllerRemoveOrder() {
		String appointmentDate = "12-12-2024";

		CustomerOrder order1 = new CustomerOrder(1l, "Jhon", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.PENDING, new Worker());
		CustomerOrder order2 = new CustomerOrder(2l, "Alic", "address", "phone", appointmentDate, "description",
				OrderCategory.PLUMBER, OrderStatus.COMPLETED, new Worker());

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order1);
			orderSwingView.orderAdded(order2);

		});
		window.list("listOrders").selectItem(1);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		verify(orderController).deleteOrder(order2);

	}

}
