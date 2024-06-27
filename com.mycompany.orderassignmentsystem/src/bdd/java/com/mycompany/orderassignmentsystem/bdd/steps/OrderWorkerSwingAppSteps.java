/*
 * BDD steps for the OrderWorkerSwingApp.
 * 
 * These steps define the behavior of the application during Cucumber BDD tests.
 * 
 * The steps include:
 * 
 * - Setting up and tearing down the test environment:
 *   - Initializing FrameFixture objects for the Order and Worker views.
 *   - Cleaning up the GUI components after each test scenario.
 * 
 * - Interactions with the Order view:
 *   - Launching the application and displaying the Order view.
 *   - Verifying the contents of the order list to ensure it displays the correct information.
 *   - Entering values into the Order view's fields and selecting items from combo boxes.
 *   - Clicking buttons to perform actions such as adding, updating, searching, and deleting orders.
 *   - Ensuring error messages are shown when expected, and verifying the specific content of those messages.
 *   - Resetting the Order view fields after operations.
 * 
 * - Interactions with the Worker view:
 *   - Displaying the Worker view and verifying its contents.
 *   - Entering values into the Worker view's fields and selecting items from combo boxes.
 *   - Clicking buttons to perform actions such as adding, updating, searching, and deleting workers.
 *   - Ensuring error messages are shown when expected, and verifying the specific content of those messages.
 *   - Resetting the Worker view search filters after operations.
 * 
 * The steps simulate user interactions with the GUI components and verify the expected outcomes:
 * - Application of assertions to ensure the GUI state matches the expected state after each operation.
 * - Use of patterns to select and verify items in combo boxes and lists.
 * - Handling of dynamic content and ensuring the correct data is displayed and managed in the application.
 * 
 * These BDD steps provide a comprehensive way to test the application's behavior in different scenarios, ensuring robustness and reliability.
 */

package com.mycompany.orderassignmentsystem.bdd.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;

import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * The Class OrderWorkerSwingAppSteps.
 */
public class OrderWorkerSwingAppSteps extends ConfigSteps {

	/** The robot with current awt hierarchy. */
	private Robot robotWithCurrentAwtHierarchy = BasicRobot.robotWithCurrentAwtHierarchy();

	/** The order view window. */
	private FrameFixture orderViewWindow;

	/** The worker view window. */
	private FrameFixture workerViewWindow;

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		if (orderViewWindow != null) {
			orderViewWindow.cleanUp();
		}
		if (workerViewWindow != null) {
			workerViewWindow.cleanUp();
		}
	}

	/**
	 * The order view is shown.
	 */
	@When("The Order View is shown")
	public void the_Order_View_is_shown() {
		application("com.mycompany.orderassignmentsystem.app.OrderWorkerAssignmentSwingApp")
				.withArgs("--postgres-host=" + HOST, "--postgres-database=" + DATABASE, "--postgres-user=" + USER,
						"--postgres-pass=" + PASSWORD, "--postgres-port=" + PORT)
				.start();

		orderViewWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Order Form".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robotWithCurrentAwtHierarchy);
	}

	/**
	 * The order view list contains an element with the following values.
	 *
	 * @param values the values
	 */
	@Then("The order view list contains an element with the following values")
	public void the_order_view_list_contains_an_element_with_the_following_values(List<List<String>> values) {
		values.forEach(v -> assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(v.get(0), v.get(1), v.get(2), v.get(3), v.get(4), v.get(5),
						v.get(6), v.get(7), v.get(8), v.get(9), v.get(10))));

	}

	/**
	 * The user enters the following values in the order view.
	 *
	 * @param values the values
	 */
	@Then("The user enters the following values in the order view")
	public void the_user_enters_the_following_values_in_the_order_view(List<Map<String, String>> values) {
		String[] textFields = { "OrderId", "CustomerName", "CustomerPhone", "CustomerAddress", "SelectedDate",
				"SearchOrder", "OrderDescription" };
		String[] comboBox = { "OrderCategory", "OrderStatus", "SearchBy" };
		values.stream().flatMap(m -> m.entrySet().stream()).forEach(e -> {
			boolean isTextField = Arrays.asList(textFields).contains(e.getKey());
			boolean isComboBox = Arrays.asList(comboBox).contains(e.getKey());

			if (isTextField) {
				orderViewWindow.textBox("txt" + e.getKey()).enterText(e.getValue());
			} else if (isComboBox) {
				orderViewWindow.comboBox("cmb" + e.getKey()).selectItem(Pattern.compile(".*" + e.getValue() + ".*"));
			} else if (e.getKey().equals("worker_phone")) {
				orderViewWindow.comboBox("cmbWorker").selectItem(Pattern.compile(".*" + e.getValue() + ".*"));

			}
		});
	}

	/**
	 * The user clicks the order view button.
	 *
	 * @param buttonText the button text
	 */
	@When("The user clicks the order view {string} button")
	public void the_user_clicks_the_order_view_button(String buttonText) {
		orderViewWindow.button(JButtonMatcher.withText(buttonText)).click();

	}

	/**
	 * An error is shown in order view containing the following values.
	 *
	 * @param values the values
	 */
	@Then("An error is shown in order view containing the following values")
	public void an_error_is_shown_in_order_view_containing_the_following_values(List<List<String>> values) {
		values.forEach(value -> {
			assertThat(orderViewWindow.label("showError").text()).contains(value);
		});
	}

	/**
	 * An seach error is shown in order view containing the following values.
	 *
	 * @param values the values
	 */
	@Then("An seach error is shown in order view containing the following values")
	public void an_seach_error_is_shown_in_order_view_containing_the_following_values(List<List<String>> values) {
		values.forEach(value -> {
			assertThat(orderViewWindow.label("showSearchErrorLbl").text()).contains(value);
		});
	}

	/**
	 * An no entry found error is shown in order view containing the following
	 * values.
	 *
	 * @param values the values
	 */
	@Then("An no entry found error is shown in order view containing the following values")
	public void an_no_entry_found_error_is_shown_in_order_view_containing_the_following_values(
			List<List<String>> values) {
		values.forEach(value -> {
			assertThat(orderViewWindow.label("showErrorNotFoundLbl").text()).contains(value);
		});
	}

	/**
	 * The order view fields contains an element with the following values.
	 *
	 * @param values the values
	 */
	@Then("The order view fields contains an element with the following values")
	public void the_order_view_fields_contains_an_element_with_the_following_values(List<Map<String, String>> values) {
		String[] textFields = { "OrderId", "CustomerName", "CustomerPhone", "CustomerAddress", "SelectedDate",
				"OrderDescription" };
		String[] comboBox = { "OrderCategory", "OrderStatus" };
		values.stream().flatMap(m -> m.entrySet().stream()).forEach(e -> {
			boolean isTextField = Arrays.asList(textFields).contains(e.getKey());
			boolean isComboBox = Arrays.asList(comboBox).contains(e.getKey());

			String expectedValue = e.getValue();
			if (isTextField) {
				orderViewWindow.textBox("txt" + e.getKey()).requireText(expectedValue);
			} else if (isComboBox) {
				orderViewWindow.comboBox("cmb" + e.getKey())
						.requireSelection(Pattern.compile(".*" + expectedValue + ".*"));
			} else if (e.getKey().equals("worker_phone")) {
				orderViewWindow.comboBox("cmbWorker").requireSelection(Pattern.compile(".*" + expectedValue + ".*"));
			}
		});
	}

	/**
	 * The user select order from the list.
	 *
	 * @param values the values
	 */
	@Then("The user select order from the list")
	public void the_user_select_order_from_the_list(List<String> values) {
		orderViewWindow.list().selectItem(Pattern.compile(".*" + values.get(0) + ".*"));
	}

	/**
	 * The order view fields are reset.
	 */
	@Then("The order view fields are reset")
	public void the_order_view_fields_are_reset() {
		orderViewWindow.textBox("txtOrderId").requireText("\b");
		orderViewWindow.textBox("txtCustomerName").requireText("");
		orderViewWindow.textBox("txtCustomerAddress").requireText("");
		orderViewWindow.textBox("txtCustomerPhone").requireText("");
		orderViewWindow.textBox("txtOrderDescription").requireText("");
		orderViewWindow.textBox("txtSelectedDate").requireText("");
		orderViewWindow.textBox("txtSearchOrder").requireText(" ");
		orderViewWindow.comboBox("cmbOrderCategory").requireNoSelection();
		orderViewWindow.comboBox("cmbOrderStatus").requireNoSelection();
		orderViewWindow.comboBox("cmbWorker").requireNoSelection();
		orderViewWindow.comboBox("cmbSearchBy").requireNoSelection();
	}

	/// ------------ Worker View Methods

	/**
	 * The worker view is shown.
	 */
	@When("The Worker View is shown")
	public void the_Worker_View_is_shown() {
		the_Order_View_is_shown();
		orderViewWindow.button("btnManageWorker").click();
		workerViewWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Worker View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robotWithCurrentAwtHierarchy);
	}

	/**
	 * The worker view list contains an element with the following values.
	 *
	 * @param values the values
	 */
	@Then("The worker view list contains an element with the following values")
	public void the_worker_view_list_contains_an_element_with_the_following_values(List<List<String>> values) {
		values.forEach(v -> assertThat(workerViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(v.get(0), v.get(1), v.get(2), v.get(3))));
	}

	/**
	 * The user enters the following values in the worker view.
	 *
	 * @param values the values
	 */
	@Then("The user enters the following values in the worker view")
	public void the_user_enters_the_following_values_in_the_worker_view(List<Map<String, String>> values) {
		String[] textFields = { "WorkerId", "WorkerName", "WorkerPhone", "SearchWorker" };
		String[] comboBox = { "WorkerCategory", "SearchByOptions" };
		values.stream().flatMap(m -> m.entrySet().stream()).forEach(e -> {
			boolean isTextField = Arrays.asList(textFields).contains(e.getKey());
			boolean isComboBox = Arrays.asList(comboBox).contains(e.getKey());

			if (isTextField) {
				workerViewWindow.textBox("txt" + e.getKey()).enterText(e.getValue());
			} else if (isComboBox) {
				workerViewWindow.comboBox("cmb" + e.getKey()).selectItem(Pattern.compile(".*" + e.getValue() + ".*"));
			}
		});
	}

	/**
	 * The user clicks the worker view button.
	 *
	 * @param buttonText the button text
	 */
	@When("The user clicks the worker view {string} button")
	public void the_user_clicks_the_worker_view_button(String buttonText) {
		workerViewWindow.button(JButtonMatcher.withText(buttonText)).click();
	}

	/**
	 * An error is shown in worker view containing the following values.
	 *
	 * @param values the values
	 */
	@Then("An error is shown in worker view containing the following values")
	public void an_error_is_shown_in_worker_view_containing_the_following_values(List<List<String>> values) {

		values.forEach(value -> {
			assertThat(workerViewWindow.label("showErrorLbl").text()).contains(value);
		});
	}

	/**
	 * The worker view fields contains an element with the following values.
	 *
	 * @param values the values
	 */
	@Then("The worker view fields contains an element with the following values")
	public void the_worker_view_fields_contains_an_element_with_the_following_values(List<Map<String, String>> values) {
		String[] textFields = { "WorkerId", "WorkerName", "WorkerPhone" };
		String[] comboBox = { "WorkerCategory", };
		values.stream().flatMap(m -> m.entrySet().stream()).forEach(e -> {
			boolean isTextField = Arrays.asList(textFields).contains(e.getKey());
			boolean isComboBox = Arrays.asList(comboBox).contains(e.getKey());

			String expectedValue = e.getValue();
			if (isTextField) {
				workerViewWindow.textBox("txt" + e.getKey()).requireText(expectedValue);
			} else if (isComboBox) {
				workerViewWindow.comboBox("cmb" + e.getKey())
						.requireSelection(Pattern.compile(".*" + expectedValue + ".*"));
			}
		});

	}

	/**
	 * An no entry found error is shown in worker view containing the following
	 * values.
	 *
	 * @param values the values
	 */
	@Then("An no entry found error is shown in worker view containing the following values")
	public void an_no_entry_found_error_is_shown_in_worker_view_containing_the_following_values(
			List<List<String>> values) {

		values.forEach(value -> {
			assertThat(workerViewWindow.label("showErrorNotFoundLbl").text()).contains(value);
		});
	}

	/**
	 * The user select worker from the list.
	 *
	 * @param values the values
	 */
	@Then("The user select worker from the list")
	public void the_user_select_worker_from_the_list(List<String> values) {
		workerViewWindow.list().selectItem(Pattern.compile(".*" + values.get(0) + ".*"));
	}

	/**
	 * An search error is shown in worker view containing the following values.
	 *
	 * @param values the values
	 */
	@Then("An search error is shown in worker view containing the following values")
	public void an_search_error_is_shown_in_worker_view_containing_the_following_values(List<List<String>> values) {
		values.forEach(value -> {
			assertThat(workerViewWindow.label("showErrorLblSearchWorker").text()).contains(value);
		});
	}

	/**
	 * The worker view search filter are reset.
	 */
	@Then("The worker view search filter are reset")
	public void the_worker_view_search_filter_are_reset() {
		workerViewWindow.textBox("txtSearchWorker").requireText(" ");
		workerViewWindow.comboBox("cmbSearchByOptions").requireNoSelection();

	}

}