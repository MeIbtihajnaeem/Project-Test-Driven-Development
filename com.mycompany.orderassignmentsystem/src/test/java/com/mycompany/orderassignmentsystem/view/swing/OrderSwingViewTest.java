/**
 * Unit tests for the OrderSwingView class with AssertJ-Swing for GUI testing.
 *
 * These tests ensure that the OrderSwingView class correctly interacts with the user 
 * interface components and delegates actions to the OrderController.
 *
 * The setup method initialises the GUI components and mocks necessary dependencies. 
 * The teardown method cleans up resources.
 *
 * Test cases include:
 * - Initial state verifications of labels, text fields, combo boxes, and buttons.
 * - Enabling/disabling the Add, Update, Fetch, Search, Delete and Clear buttons based on user input.
 * - Delegation of user actions to the OrderController such as adding, updating, fetching, 
 *   searching, and deleting orders.
 * - Displaying orders and workers in the UI components like lists and combo boxes.
 * - Showing error messages in the error labels.
 *
 * @see OrderSwingView
 * @see OrderController
 * @see CustomerOrder
 * @see Worker
 */

package com.mycompany.orderassignmentsystem.view.swing;

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

import com.mycompany.orderassignmentsystem.controller.OrderController;
import com.mycompany.orderassignmentsystem.enumerations.OperationType;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderSearchOptions;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;

/**
 * The Class OrderSwingViewTest.
 */
@RunWith(GUITestRunner.class)

public class OrderSwingViewTest extends AssertJSwingJUnitTestCase {

	/** The window. */
	private FrameFixture window;

	/** The order swing view. */
	private OrderSwingView orderSwingView;

	/** The order controller. */
	@Mock
	private OrderController orderController;

	/** The worker view. */
	@Mock
	private WorkerSwingView workerView;

	/** The closeable. */
	private AutoCloseable closeable;

	/** The order id 1. */
	private long ORDER_ID_1 = 1l;

	/** The order id 2. */
	private long ORDER_ID_2 = 2l;

	/** The customer name 1. */
	private String CUSTOMER_NAME_1 = "Jhon";

	/** The customer phone 1. */
	private String CUSTOMER_PHONE_1 = "3401372671";

	/** The customer address 1. */
	private String CUSTOMER_ADDRESS_1 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 1. */
	private String ORDER_APPOINTMENT_DATE_1 = "12-12-2024";

	/** The order description 1. */
	private String ORDER_DESCRIPTION_1 = "Please be on time";

	/** The order category 1. */
	private OrderCategory ORDER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The order status 1. */
	private OrderStatus ORDER_STATUS_1 = OrderStatus.PENDING;

	/** The customer name 2. */
	private String CUSTOMER_NAME_2 = "Alic";

	/** The customer phone 2. */
	private String CUSTOMER_PHONE_2 = "3401372672";

	/** The customer address 2. */
	private String CUSTOMER_ADDRESS_2 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 2. */
	private String ORDER_APPOINTMENT_DATE_2 = "12-12-2024";

	/** The order description 2. */
	private String ORDER_DESCRIPTION_2 = "Please bring tape";

	/** The order category 2. */
	private OrderCategory ORDER_CATEGORY_2 = OrderCategory.PLUMBER;

	/** The order status 2. */
	private OrderStatus ORDER_STATUS_2 = OrderStatus.PENDING;

	/** The worker 1. */
	private Worker worker1 = new Worker(1l, "Jhon", "123456789", OrderCategory.PLUMBER);

	/** The worker 2. */
	private Worker worker2 = new Worker(2l, "Bob", "123456780", OrderCategory.ELECTRICIAN);

	/** The worker 3. */
	private Worker worker3 = new Worker(3l, "Alic", "123456781", OrderCategory.ELECTRICIAN);

	/** The selecting category index. */
	private int SELECTING_CATEGORY_INDEX = 0;

	/** The selecting status index. */
	private int SELECTING_STATUS_INDEX = 0;

	/** The selecting worker index. */
	private int SELECTING_WORKER_INDEX = 0;

	/** The selecting search option index. */
	private int SELECTING_SEARCH_OPTION_INDEX = 1;

	/**
	 * On set up.
	 *
	 * @throws Exception the exception
	 */
	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderSwingView.setOrderController(orderController);
			orderSwingView.setWorkerSwingView(workerView);

			return orderSwingView;
		});
		window = new FrameFixture(robot(), orderSwingView);
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

	/**
	 * Test when order id is empty customer name customer address customer phone
	 * order description appointment date order category order status worker is not
	 * empty and add button should be enable.
	 */
	@Test
	public void testWhenOrderIdIsEmptyCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndAddButtonShouldBeEnable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
			orderSwingView.resetAllFields();
		});
		window.textBox("txtOrderId").enterText(" "); // Make sure orderId is empty
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);
		window.button(JButtonMatcher.withName("btnAdd")).requireEnabled();
	}

	/**
	 * Test when order id customer name customer address customer phone order
	 * description appointment date order category order status worker is not empty
	 * and add button should be disable.
	 */
	@Test
	public void testWhenOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndAddButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
			orderSwingView.resetAllFields();
		});
		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
	}

	/**
	 * Test when order id customer name customer address customer phone order
	 * description appointment date order category order status worker is not empty
	 * and update button should be enable.
	 */
	@Test
	public void testWhenOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndUpdateButtonShouldBeEnable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
			orderSwingView.resetAllFields();
		});
		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);
		window.button(JButtonMatcher.withName("btnUpdate")).requireEnabled();
	}

	/**
	 * Test when order id is empty but customer name customer address customer phone
	 * order description appointment date order category order status worker is not
	 * empty and update button should be disable.
	 */
	@Test
	public void testWhenOrderIdIsEmptyButCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndUpdateButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
			orderSwingView.resetAllFields();
		});
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);
		window.button(JButtonMatcher.withName("btnUpdate")).requireDisabled();
	}

	/**
	 * Test when order id is not empty but customer name customer address customer
	 * phone order description appointment date order category order status worker
	 * is empty and fetch button should be enable.
	 */
	@Test
	public void testWhenOrderIdIsNotEmptyButCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsEmptyAndFetchButtonShouldBeEnable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
			orderSwingView.resetAllFields();
		});
		Long orderId = 1l;
		window.textBox("txtOrderId").enterText(orderId.toString());
		window.textBox("txtCustomerName").enterText("a");
		window.textBox("txtCustomerAddress").enterText("a");
		window.textBox("txtCustomerPhone").enterText("a");
		window.textBox("txtOrderDescription").enterText("a");
		window.textBox("txtSelectedDate").enterText("a");

		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);
		window.textBox("txtCustomerName").deleteText();
		window.textBox("txtCustomerAddress").deleteText();
		window.textBox("txtCustomerPhone").deleteText();
		window.textBox("txtOrderDescription").deleteText();
		window.textBox("txtSelectedDate").deleteText();
		window.comboBox("cmbOrderCategory").clearSelection();
		window.comboBox("cmbOrderStatus").clearSelection();
		window.comboBox("cmbWorker").clearSelection();
		window.button(JButtonMatcher.withName("btnFetch")).requireEnabled();

		window.textBox("txtCustomerName").requireText("");
		window.textBox("txtCustomerAddress").requireText("");
		window.textBox("txtCustomerPhone").requireText("");
		window.textBox("txtOrderDescription").requireText("");
		window.textBox("txtSelectedDate").requireText("");
		window.comboBox("cmbOrderCategory").requireNoSelection();
		window.comboBox("cmbOrderStatus").requireNoSelection();
		window.comboBox("cmbWorker").requireNoSelection();

	}

	/**
	 * Test when order id is empty but customer name customer address customer phone
	 * order description appointment date order category order status worker is not
	 * empty and fetch button should be disabled.
	 */
	@Test
	public void testWhenOrderIdIsEmptyButCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndFetchButtonShouldBeDisabled() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
			orderSwingView.resetAllFields();
		});

		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);
		window.button(JButtonMatcher.withName("btnFetch")).requireDisabled();
	}

	/**
	 * Test when search string and search option is not empty and search button
	 * should be enabled.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionIsNotEmptyAndSearchButtonShouldBeEnabled() {
		window.textBox("txtSearchOrder").enterText("Naeem");
		window.comboBox("cmbSearchBy").selectItem(0);
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireEnabled();
	}

	/**
	 * Test when search string and search option is empty and search button should
	 * be disabled.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionIsEmptyAndSearchButtonShouldBeDisabled() {
		window.textBox("txtSearchOrder").enterText(" ");
		window.button(JButtonMatcher.withName("btnSearchOrder")).requireDisabled();
	}

	/**
	 * Test when search string and search options is empty and clear worker button
	 * should be disabled.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionsIsEmptyAndClearWorkerButtonShouldBeDisabled() {
		window.textBox("txtSearchOrder").enterText(" ");
		window.button(JButtonMatcher.withName("btnClearSearch")).requireDisabled();
	}

	/**
	 * Test when search string and search options is not empty and clear worker
	 * button should be enabled.
	 */
	@Test
	public void testWhenSearchStringAndSearchOptionsIsNotEmptyAndClearWorkerButtonShouldBeEnabled() {
		window.textBox("txtSearchOrder").enterText("Naeem");
		window.comboBox("cmbSearchBy").selectItem(0);
		window.button(JButtonMatcher.withName("btnClearSearch")).requireEnabled();
	}

	/**
	 * Test when either customer name customer address customer phone order
	 * description appointment date order category order status worker are blank and
	 * add button should be disable.
	 */
	@Test
	public void testWhenEitherCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerAreBlankAndAddButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
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

	/**
	 * Test when either order id customer name customer address customer phone order
	 * description appointment date order category order status worker are blank and
	 * update button should be disable.
	 */
	@Test
	public void testWhenEitherOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerAreBlankAndUpdateButtonShouldBeDisable() {
		// sadf
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);

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

		orderId.enterText("\b");
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

	/**
	 * Test when either order id customer name customer address customer phone order
	 * description appointment date order category order status worker are blank and
	 * fetch button should be disable.
	 */
	@Test
	public void testWhenEitherOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerAreBlankAndFetchButtonShouldBeDisable() {
		GuiActionRunner.execute(() -> {
			orderSwingView.getWorkerListModel().addElement(worker1);
			orderSwingView.getWorkerListModel().addElement(worker2);
			orderSwingView.getWorkerListModel().addElement(worker3);
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

	/**
	 * Test when worker id only numbers then fetch button should be fetch button
	 * enabled.
	 */
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

	/**
	 * Test when either search string or search option are blank then search button
	 * should be disabled.
	 */
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

	/**
	 * Test when either search string or search option are blank then clear search
	 * button should be disabled.
	 */
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

	/**
	 * Test delete button should be enabled only when A order is selected.
	 */
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

	/**
	 * Tests show all orders add order description to the list orders.
	 */
	@Test
	public void testsShowAllOrdersAddOrderDescriptionToTheListOrders() {

		CustomerOrder order1 = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		CustomerOrder order2 = new CustomerOrder(ORDER_ID_2, CUSTOMER_NAME_2, CUSTOMER_PHONE_2, CUSTOMER_ADDRESS_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker2);

		GuiActionRunner.execute(() -> orderSwingView.showAllOrder(Arrays.asList(order1, order2)));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());
	}

	/**
	 * Tests show all workers add worker description to the worker combo box.
	 */
	@Test
	public void testsShowAllWorkersAddWorkerDescriptionToTheWorkerComboBox() {

		GuiActionRunner.execute(() -> orderSwingView.showAllWorkers(Arrays.asList(worker1, worker2)));
		String[] listContents = window.comboBox("cmbWorker").contents();
		assertThat(listContents).containsExactly(worker1.toString(), worker2.toString());
	}

	/**
	 * Test show error should show the message in the error label.
	 */
	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		CustomerOrder order = new CustomerOrder();
		GuiActionRunner.execute(() -> orderSwingView.showError("error message", order));
		window.label("showError").requireText("error message: " + order);
	}

	/**
	 * Test show search error should show the message in the error label.
	 */
	@Test
	public void testShowSearchErrorShouldShowTheMessageInTheErrorLabel() {
		String searchText = "1l";
		GuiActionRunner.execute(() -> orderSwingView.showSearchError("error message", searchText));
		window.label("showSearchErrorLbl").requireText("error message: " + searchText);
	}

	/**
	 * Test show not found error should show the message in the error label.
	 */
	@Test
	public void testShowNotFoundErrorShouldShowTheMessageInTheErrorLabel() {
		CustomerOrder order = new CustomerOrder();
		GuiActionRunner.execute(() -> orderSwingView.showErrorNotFound("error message", order));
		window.label("showErrorNotFoundLbl").requireText("error message: " + order);
	}

	/**
	 * Test order added should add the order to the list and reset the error label.
	 */
	@Test
	public void testOrderAddedShouldAddTheOrderToTheListAndResetTheErrorLabel() {

		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		GuiActionRunner.execute(() -> orderSwingView.orderAdded(order));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order.toString());
		window.label("showError").requireText(" ");
	}

	/**
	 * Test order modified should modify and add the order to the list and reset the
	 * error label.
	 */
	@Test
	public void testOrderModifiedShouldModifyAndAddTheOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		GuiActionRunner.execute(() -> orderSwingView.orderAdded(order));
		order.setOrderStatus(OrderStatus.CANCELLED);
		GuiActionRunner.execute(() -> orderSwingView.orderModified(order));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order.toString());
		window.label("showError").requireText(" ");
	}

	/**
	 * Test order modified should not modify and add the order to the list and reset
	 * the error label.
	 */
	@Test
	public void testOrderModifiedShouldNotModifyAndAddTheOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

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

	/**
	 * Test order fetch should fetch the order to the fields and reset the error
	 * label.
	 */
	@Test
	public void testOrderFetchShouldFetchTheOrderToTheFieldsAndResetTheErrorLabel() {

		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		GuiActionRunner.execute(() -> {
			orderSwingView.showAllWorkers(Arrays.asList(worker1));
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

	/**
	 * Test order removed should remove the order to the list and reset the error
	 * label.
	 */
	@Test
	public void testOrderRemovedShouldRemoveTheOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order1 = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		CustomerOrder order2 = new CustomerOrder(ORDER_ID_2, CUSTOMER_NAME_2, CUSTOMER_PHONE_2, CUSTOMER_ADDRESS_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker2);

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order1);
			orderSwingView.orderAdded(order2);
		});

		GuiActionRunner.execute(() -> orderSwingView.orderRemoved(order1));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order2.toString());
		window.label("showError").requireText(" ");
	}

	/**
	 * Test order search should modify and show only searched order to the list and
	 * reset the error label.
	 */
	@Test
	public void testOrderSearchShouldModifyAndShowOnlySearchedOrderToTheListAndResetTheErrorLabel() {
		CustomerOrder order1 = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		CustomerOrder order2 = new CustomerOrder(ORDER_ID_2, CUSTOMER_NAME_2, CUSTOMER_PHONE_2, CUSTOMER_ADDRESS_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker2);

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order1);
			orderSwingView.orderAdded(order2);
		});

		GuiActionRunner.execute(() -> orderSwingView.showSearchResultForOrder(Arrays.asList(order2)));
		String[] listContents = window.list("listOrders").contents();
		assertThat(listContents).containsExactly(order2.toString());
		window.label("showSearchErrorLbl").requireText(" ");
	}

	/**
	 * Test manage worker button shows worker swing view.
	 */
	@Test
	public void testManageWorkerButtonShowsWorkerSwingView() {
		window.button(JButtonMatcher.withName("btnManageWorker")).click();
		verify(workerView).setVisible(true);
	}

	/**
	 * Test add button should delegate to order controller create or update order.
	 */
	@Test
	public void testAddButtonShouldDelegateToOrderControllerCreateOrUpdateOrder() {

		GuiActionRunner.execute(() -> {
			orderSwingView.showAllWorkers(Arrays.asList(worker1));
		});

		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);

		window.button(JButtonMatcher.withName("btnAdd")).click();

		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		verify(orderController).createOrUpdateOrder(order, OperationType.ADD);
	}

	/**
	 * Test update button should delegate to order controller create or update
	 * order.
	 */
	@Test
	public void testUpdateButtonShouldDelegateToOrderControllerCreateOrUpdateOrder() {

		GuiActionRunner.execute(() -> {
			orderSwingView.showAllWorkers(Arrays.asList(worker1));
		});

		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));
		window.textBox("txtCustomerName").enterText(CUSTOMER_NAME_1);
		window.textBox("txtCustomerAddress").enterText(CUSTOMER_ADDRESS_1);
		window.textBox("txtCustomerPhone").enterText(CUSTOMER_PHONE_1);
		window.textBox("txtOrderDescription").enterText(ORDER_DESCRIPTION_1);
		window.textBox("txtSelectedDate").enterText(ORDER_APPOINTMENT_DATE_1);
		window.comboBox("cmbOrderCategory").selectItem(SELECTING_CATEGORY_INDEX);
		window.comboBox("cmbOrderStatus").selectItem(SELECTING_STATUS_INDEX);
		window.comboBox("cmbWorker").selectItem(SELECTING_WORKER_INDEX);

		window.button(JButtonMatcher.withName("btnUpdate")).click();

		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);
		verify(orderController).createOrUpdateOrder(order, OperationType.UPDATE);
	}

	/**
	 * Test fetch button should delegate to order controller fetch order by id.
	 */
	@Test
	public void testFetchButtonShouldDelegateToOrderControllerFetchOrderById() {

		window.textBox("txtOrderId").enterText(Long.toString(ORDER_ID_1));

		window.button(JButtonMatcher.withName("btnFetch")).click();
		CustomerOrder order = new CustomerOrder();
		order.setOrderId(ORDER_ID_1);

		verify(orderController).fetchOrderById(order);

	}

	/**
	 * Test search order button should delegate to worker controller search order by
	 * options.
	 */
	@Test
	public void testSearchOrderButtonShouldDelegateToWorkerControllerSearchOrderByOptions() {
		OrderSearchOptions searchOption = (OrderSearchOptions) window.comboBox("cmbSearchBy").target()
				.getItemAt(SELECTING_SEARCH_OPTION_INDEX);

		window.textBox("txtSearchOrder").enterText(Long.toString(ORDER_ID_1));
		window.comboBox("cmbSearchBy").selectItem(SELECTING_SEARCH_OPTION_INDEX);

		window.button(JButtonMatcher.withName("btnSearchOrder")).click();

		verify(orderController).searchOrder(Long.toString(ORDER_ID_1), searchOption);

	}

	/**
	 * Test clear search button should delegate to order controller show all orders.
	 */
	@Test
	public void testClearSearchButtonShouldDelegateToOrderControllerShowAllOrders() {

		CustomerOrder order = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order);
		});
		window.textBox("txtSearchOrder").enterText(Long.toString(ORDER_ID_1));
		window.comboBox("cmbSearchBy").selectItem(SELECTING_SEARCH_OPTION_INDEX);

		window.button(JButtonMatcher.withName("btnClearSearch")).click();
		verify(orderController).allOrders();
	}

	/**
	 * Test delete button should delegate to order controller remove order.
	 */
	@Test
	public void testDeleteButtonShouldDelegateToOrderControllerRemoveOrder() {

		CustomerOrder order1 = new CustomerOrder(ORDER_ID_1, CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		CustomerOrder order2 = new CustomerOrder(ORDER_ID_2, CUSTOMER_NAME_2, CUSTOMER_PHONE_2, CUSTOMER_ADDRESS_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker2);

		GuiActionRunner.execute(() -> {
			orderSwingView.orderAdded(order1);
			orderSwingView.orderAdded(order2);

		});
		window.list("listOrders").selectItem(1);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		verify(orderController).deleteOrder(order2);

	}

}
