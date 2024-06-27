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

/**
 * The OrderWorkerAssignmentSwingApp class is the main entry point for the
 * Swing-based application that manages orders and workers.
 * 
 * - This class sets up and launches the application using command line options
 * for database configuration. - It initializes the necessary components such as
 * entity managers, repositories, controllers, and views.
 * 
 * - Command Line Options: - `--postgres-host`: Specifies the PostgreSQL host
 * (default: "localhost"). - `--postgres-database`: Specifies the PostgreSQL
 * database name (default: "orderWorkerTestDb"). - `--postgres-user`: Specifies
 * the PostgreSQL user (default: "testUser"). - `--postgres-pass`: Specifies the
 * PostgreSQL password. - `--postgres-port`: Specifies the PostgreSQL port
 * (default: "5432").
 * 
 * - Main Method: - Executes the application with the provided command line
 * arguments.
 * 
 * - Call Method: - Runs the application on the Event Dispatch Thread (EDT) to
 * ensure thread safety for Swing components.
 * 
 * Note: This application uses Hibernate for database operations and Apache
 * Log4j for logging.
 * 
 * @see OrderController
 * @see WorkerController
 * @see OrderDatabaseRepository
 * @see WorkerDatabaseRepository
 * @see OrderSwingView
 * @see WorkerSwingView
 * @see ValidationConfigurations
 * @see ExtendedValidationConfigurations
 */
@Command(mixinStandardHelpOptions = true)
public class OrderWorkerAssignmentSwingApp implements Callable<Void> {

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/** The properties. */
	private Map<String, String> properties = new HashMap<>();

	/** The host. */
	@Option(names = { "--postgres-host" }, description = "Postgresql host ")
	private String host = "localhost";

	/** The database. */
	@Option(names = { "--postgres-database" }, description = "Postgresql host database")
	private String database = "orderWorkerTestDb";

	/** The user. */
	@Option(names = { "--postgres-user" }, description = "Postgresql user")
	private String user = "testUser";

	/** The password. */
	@Option(names = { "--postgres-pass" }, description = "Postgresql pass")
	private String password;

	/** The port. */
	@Option(names = { "--postgres-port" }, description = "Postgresql port")
	private String port = "5432";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(OrderWorkerAssignmentSwingApp.class);

	/**
	 * The main method.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		new CommandLine(new OrderWorkerAssignmentSwingApp()).execute(args);

	}

	/**
	 * Call.
	 *
	 * @return the void
	 * @throws Exception the exception
	 */
	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				// Persistence Unit for postgresql database
				String persistenceUnitName = "OriginalPersistenceUnit";

				// properties of database modified based on the user input
				// from command line arguments
				String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
				properties.put("javax.persistence.jdbc.url", jdbcUrl);
				properties.put("javax.persistence.jdbc.user", user);
				properties.put("javax.persistence.jdbc.password", password);
				properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				properties.put("hibernate.hbm2ddl.auto", "update");

				// entity manager factory for database connection
				entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);

				// Database repositories
				OrderDatabaseRepository orderRepository = new OrderDatabaseRepository(entityManagerFactory);
				WorkerDatabaseRepository workerRepository = new WorkerDatabaseRepository(entityManagerFactory);

				// Forms
				WorkerSwingView workerView = new WorkerSwingView();
				OrderSwingView orderView = new OrderSwingView();

				// Validation configurations class for user input
				ValidationConfigurations validationConfigurations = new ExtendedValidationConfigurations();

				// Controller for both entities order & worker
				OrderController orderController = new OrderController(orderRepository, orderView, workerRepository,
						validationConfigurations);
				WorkerController workerController = new WorkerController(workerRepository, workerView,
						validationConfigurations);

				// Getting all workers for worker list in worker view
				workerController.getAllWorkers();

				// Setting controller to view and starting the view
				workerView.setWorkerController(workerController);
				orderView.setOrderController(orderController);
				orderView.setWorkerSwingView(workerView);
				orderView.setVisible(true);

				// Getting all orders for order list in order view
				orderController.allOrders();

				// Getting all worker for the combo box in order view
				orderController.allWorkers();
			} catch (Exception e) {
				LOGGER.error("Exception", e);
			}
		});
		return null;
	}
}