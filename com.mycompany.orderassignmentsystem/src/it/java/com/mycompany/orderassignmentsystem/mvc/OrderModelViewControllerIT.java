package com.mycompany.orderassignmentsystem.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycompany.orderassignmentsystem.Config;
import com.mycompany.orderassignmentsystem.configurations.DBConfig;
import com.mycompany.orderassignmentsystem.controller.OrderController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.OrderRepository;
import com.mycompany.orderassignmentsystem.repository.WorkerRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.swing.OrderSwingView;

@RunWith(GUITestRunner.class)

public class OrderModelViewControllerIT extends AssertJSwingJUnitTestCase {

	private OrderRepository orderRepository;

	private OrderSwingView orderSwingView;

	private WorkerRepository workerRepository;

	private OrderController orderController;

	private FrameFixture window;
	private EntityManagerFactory entityManagerFactory;
	private ValidationConfigurations validationConfig;
	private static DBConfig databaseConfig;

	@BeforeClass
	public static void setupServer() {
		databaseConfig = Config.getDatabaseConfig();

		databaseConfig.testAndStartDatabaseConnection();
	}

	@Override
	protected void onSetUp() throws Exception {
		entityManagerFactory = databaseConfig.getEntityManagerFactory();
		workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
		orderRepository = new OrderDatabaseRepository(entityManagerFactory);
		validationConfig = new ExtendedValidationConfigurations();

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderController = new OrderController(orderRepository, orderSwingView, workerRepository, validationConfig);
			orderSwingView.setOrderController(orderController);
			return orderSwingView;
		});

		window = new FrameFixture(robot(), orderSwingView);
		window.show();

	}

	@Override
	protected void onTearDown() {
		entityManagerFactory.close();
	}

	@Test
	public void testAddOrder() {

		String name = "Naeem";
		String phoneNumber = "3401372678";

		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());

		window.textBox("txtCustomerName").enterText(customerName);
		window.textBox("txtCustomerAddress").enterText(customerAddress);
		window.textBox("txtCustomerPhone").enterText(customerPhone);
		window.textBox("txtOrderDescription").enterText(orderDescription);
		window.textBox("txtSelectedDate").enterText(appointmentDate);
		window.comboBox("cmbOrderCategory").selectItem(categoryIndex);
		window.comboBox("cmbOrderStatus").selectItem(statusIndex);
		window.comboBox("cmbWorker").selectItem(Pattern.compile(".*" + savedWorker.getWorkerName() + ".*"));

		window.button(JButtonMatcher.withName("btnAdd")).click();
		CustomerOrder savedOrder = orderRepository.findAll().get(0);
		assertThat(orderRepository.findById(savedOrder.getOrderId())).isEqualTo(savedOrder);
	}

	// This method first save order in the database then fetch the
	// order by id and edit the customer name and then update it
	// both fetch and update are by button click
	@Test
	public void testUpdateAndFetchOrder() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(customerName);
		order.setCustomerAddress(customerAddress);
		order.setCustomerPhoneNumber(customerPhone);
		order.setOrderDescription(orderDescription);
		order.setAppointmentDate(appointmentDate);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(savedWorker);
		order = orderRepository.save(order);
		window.textBox("txtOrderId").enterText(order.getOrderId().toString());
		window.button(JButtonMatcher.withName("btnFetch")).click();
		String updatedCustomerName = "Jhon";
		window.textBox("txtCustomerName").enterText(updatedCustomerName);
		window.button(JButtonMatcher.withName("btnUpdate")).click();
		order.setCustomerName(updatedCustomerName);
		order.setOrderId(order.getOrderId());

		assertThat(orderRepository.findById(order.getOrderId())).isEqualTo(order);
	}

	@Test
	public void testDeleteOrder() {
		String name = "Naeem";
		String phoneNumber = "3401372678";
		String customerName = "Ibtihaj";
		String customerAddress = "Piazza Luigi";
		String customerPhone = "3401372678";
		String orderDescription = "Plumber Required";
		String appointmentDate = "12-12-2024";
		int categoryIndex = 0;
		int statusIndex = 0;
		OrderCategory category = (OrderCategory) window.comboBox("cmbOrderCategory").target().getItemAt(categoryIndex);
		OrderStatus status = (OrderStatus) window.comboBox("cmbOrderStatus").target().getItemAt(statusIndex);
		Worker worker = new Worker();
		worker.setWorkerName(name);
		worker.setWorkerPhoneNumber(phoneNumber);
		worker.setWorkerCategory(category);
		Worker savedWorker = workerRepository.save(worker);
		GuiActionRunner.execute(() -> orderController.allWorkers());
		CustomerOrder order = new CustomerOrder();
		order.setCustomerName(customerName);
		order.setCustomerAddress(customerAddress);
		order.setCustomerPhoneNumber(customerPhone);
		order.setOrderDescription(orderDescription);
		order.setAppointmentDate(appointmentDate);
		order.setOrderCategory(category);
		order.setOrderStatus(status);
		order.setWorker(savedWorker);
		CustomerOrder savedOrder = orderRepository.save(order);
		GuiActionRunner.execute(() -> orderController.allOrders());
		window.list("listOrders").selectItem(0);
		window.button(JButtonMatcher.withName("btnDelete")).click();
		assertThat(orderRepository.findById(savedOrder.getOrderId())).isNull();
	}
}
