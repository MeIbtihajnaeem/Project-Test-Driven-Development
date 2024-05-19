package com.mycompany.orderAssignmentSystem.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.orderAssignmentSystem.controller.OrderController;
import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;

@RunWith(GUITestRunner.class)

public class OrderSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private OrderSwingView orderSwingView;
	@Mock
	private OrderController orderController;

	private AutoCloseable closeable;

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

		// verify date components
		window.textBox("txtDatePicker").requireEnabled();
		window.button(JButtonMatcher.withText("Select Date")).requireEnabled();

		// verify combo box
		window.comboBox("cmbOrderCategory").requireEnabled();
		window.comboBox("cmbOrderStatus").requireEnabled();
		window.comboBox("cmbWorker").requireEnabled();

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
	public void testWhenOrderIdCustomerNameCustomerAddressCustomerPhoneOrderDescriptionAppointmentDateOrderCategoryOrderStatusWorkerIsNotEmptyAndAddButtonShouldBeDisable() {
		String orderId = "1l";
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		int categoryIndex = 0;
		int statusIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
	}

	@Test
	public void testWhenWorkerIdWorkerNameWorkerPhoneNumberWorkerCategoryIsNotEmptyAndAddButtonShouldBeDisable() {
		window.textBox("txtWorkerId").enterText("1");
		window.textBox("txtWorkerName").enterText("Naeem");
		window.textBox("txtWorkerPhone").enterText("3401372678");
		window.comboBox("cmbWorkerCategory").selectItem(0);
		window.button(JButtonMatcher.withName("btnAdd")).requireDisabled();
	}

}
