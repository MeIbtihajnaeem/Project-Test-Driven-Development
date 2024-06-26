package com.mycompany.orderassignmentsystem.app;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderassignmentsystem.controller.OrderController;
import com.mycompany.orderassignmentsystem.controller.WorkerController;
import com.mycompany.orderassignmentsystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderassignmentsystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderassignmentsystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderassignmentsystem.view.swing.OrderSwingView;
import com.mycompany.orderassignmentsystem.view.swing.WorkerSwingView;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class OrderWorkerAssignmentSwingApp implements Callable<Void> {
	private EntityManagerFactory entityManagerFactory;
	private Map<String, String> properties = new HashMap<>();

	@Option(names = { "--postgres-host" }, description = "Postgresql host ")
	private String host = "localhost";
	@Option(names = { "--postgres-database" }, description = "Postgresql host database")
	private String database = "orderWorkerTestDb";
	@Option(names = { "--postgres-user" }, description = "Postgresql user")
	private String user = "testUser";
	@Option(names = { "--postgres-pass" }, description = "Postgresql pass")
	private String password;
	@Option(names = { "--postgres-port" }, description = "Postgresql port")
	private String port = "5432";
	private static final Logger LOGGER = LogManager.getLogger(OrderWorkerAssignmentSwingApp.class);

	public static void main(String[] args) {
		new CommandLine(new OrderWorkerAssignmentSwingApp()).execute(args);

	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				String persistenceUnitName = "OriginalPersistenceUnit";
				String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
				properties.put("javax.persistence.jdbc.url", jdbcUrl);
				properties.put("javax.persistence.jdbc.user", user);
				properties.put("javax.persistence.jdbc.password", password);
				properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				properties.put("hibernate.hbm2ddl.auto", "update");
				entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
				OrderDatabaseRepository orderRepository = new OrderDatabaseRepository(entityManagerFactory);
				WorkerDatabaseRepository workerRepository = new WorkerDatabaseRepository(entityManagerFactory);
				WorkerSwingView workerView = new WorkerSwingView();
				OrderSwingView orderView = new OrderSwingView();
				ValidationConfigurations validationConfigurations = new ExtendedValidationConfigurations();
				OrderController orderController = new OrderController(orderRepository, orderView, workerRepository,
						validationConfigurations);
				WorkerController workerController = new WorkerController(workerRepository, workerView,
						validationConfigurations);
				workerController.getAllWorkers();
				workerView.setWorkerController(workerController);
				orderView.setOrderController(orderController);
				orderView.setWorkerSwingView(workerView);
				orderView.setVisible(true);
				orderController.allOrders();
				orderController.allWorkers();
			} catch (Exception e) {
				LOGGER.error("Exception", e);
			}
		});
		return null;
	}
}