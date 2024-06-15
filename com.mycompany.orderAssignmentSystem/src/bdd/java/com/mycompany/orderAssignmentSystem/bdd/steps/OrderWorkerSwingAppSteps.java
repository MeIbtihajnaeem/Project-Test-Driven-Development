package com.mycompany.orderAssignmentSystem.bdd.steps;

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

public class OrderWorkerSwingAppSteps extends ConfigSteps {
	private Robot robotWithCurrentAwtHierarchy = BasicRobot.robotWithCurrentAwtHierarchy();

	private FrameFixture orderViewWindow;
	private FrameFixture workerViewWindow;

	@After
	public void tearDown() {
		if (orderViewWindow != null) {
			orderViewWindow.cleanUp();
		}
		if (workerViewWindow != null) {
			workerViewWindow.cleanUp();
		}
	}

	@When("The Order View is shown")
	public void the_Order_View_is_shown() {
		application("com.mycompany.orderAssignmentSystem.app.OrderWorkerAssignmentSwingApp")
				.withArgs("--postgres-host=" + host, "--postgres-database=" + database, "--postgres-user=" + user,
						"--postgres-pass=" + password, "--postgres-port=" + port)
				.start();

		orderViewWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Order Form".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robotWithCurrentAwtHierarchy);
	}

	@Then("The order view list contains an element with the following values")
	public void the_order_view_list_contains_an_element_with_the_following_values(List<List<String>> values) {
		values.forEach(v -> assertThat(orderViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(v.get(0), v.get(1), v.get(2), v.get(3), v.get(4), v.get(5),
						v.get(6), v.get(7), v.get(8), v.get(9), v.get(10))));

	}

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

	@When("The user clicks the order view {string} button")
	public void the_user_clicks_the_order_view_button(String buttonText) {
		orderViewWindow.button(JButtonMatcher.withText(buttonText)).click();

	}

	@Then("An error is shown in order view containing the following values")
	public void an_error_is_shown_in_order_view_containing_the_following_values(List<List<String>> values) {
		values.forEach(value -> {
			assertThat(orderViewWindow.label("showError").text()).contains(value);
		});
	}

	@Then("An seach error is shown in order view containing the following values")
	public void an_seach_error_is_shown_in_order_view_containing_the_following_values(List<List<String>> values) {
		values.forEach(value -> {
			assertThat(orderViewWindow.label("showSearchErrorLbl").text()).contains(value);
		});
	}

	@Then("An no entry found error is shown in order view containing the following values")
	public void an_no_entry_found_error_is_shown_in_order_view_containing_the_following_values(
			List<List<String>> values) {
		values.forEach(value -> {
			assertThat(orderViewWindow.label("showErrorNotFoundLbl").text()).contains(value);
		});
	}

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

	@Then("The user select order from the list")
	public void the_user_select_order_from_the_list(List<String> values) {
		orderViewWindow.list().selectItem(Pattern.compile(".*" + values.get(0) + ".*"));
	}

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

	@Then("The worker view list contains an element with the following values")
	public void the_worker_view_list_contains_an_element_with_the_following_values(List<List<String>> values) {
		values.forEach(v -> assertThat(workerViewWindow.list().contents())
				.anySatisfy(e -> assertThat(e).contains(v.get(0), v.get(1), v.get(2), v.get(3))));
	}

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

	@When("The user clicks the worker view {string} button")
	public void the_user_clicks_the_worker_view_button(String buttonText) {
		workerViewWindow.button(JButtonMatcher.withText(buttonText)).click();
	}

	@Then("An error is shown in worker view containing the following values")
	public void an_error_is_shown_in_worker_view_containing_the_following_values(List<List<String>> values) {

		values.forEach(value -> {
			assertThat(workerViewWindow.label("showErrorLbl").text()).contains(value);
		});
	}

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

	@Then("An no entry found error is shown in worker view containing the following values")
	public void an_no_entry_found_error_is_shown_in_worker_view_containing_the_following_values(
			List<List<String>> values) {

		values.forEach(value -> {
			assertThat(workerViewWindow.label("showErrorNotFoundLbl").text()).contains(value);
		});
	}

	@Then("The user select worker from the list")
	public void the_user_select_worker_from_the_list(List<String> values) {
		workerViewWindow.list().selectItem(Pattern.compile(".*" + values.get(0) + ".*"));
	}

	@Then("An search error is shown in worker view containing the following values")
	public void an_search_error_is_shown_in_worker_view_containing_the_following_values(List<List<String>> values) {
		values.forEach(value -> {
			assertThat(workerViewWindow.label("showErrorLblSearchWorker").text()).contains(value);
		});
	}

	@Then("The worker view search filter are reset")
	public void the_worker_view_search_filter_are_reset() {
		workerViewWindow.textBox("txtSearchWorker").requireText(" ");
		workerViewWindow.comboBox("cmbSearchByOptions").requireNoSelection();

	}

}
