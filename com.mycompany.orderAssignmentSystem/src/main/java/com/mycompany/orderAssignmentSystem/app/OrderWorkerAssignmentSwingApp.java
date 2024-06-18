package com.mycompany.orderAssignmentSystem.app;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mycompany.orderAssignmentSystem.controller.OrderController;
import com.mycompany.orderAssignmentSystem.controller.WorkerController;
import com.mycompany.orderAssignmentSystem.controller.utils.ValidationConfigurations;
import com.mycompany.orderAssignmentSystem.controller.utils.extensions.ExtendedValidationConfigurations;
import com.mycompany.orderAssignmentSystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderAssignmentSystem.repository.postgres.WorkerDatabaseRepository;
import com.mycompany.orderAssignmentSystem.view.swing.OrderSwingView;
import com.mycompany.orderAssignmentSystem.view.swing.WorkerSwingView;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class OrderWorkerAssignmentSwingApp implements Callable<Void> {
	private static EntityManagerFactory entityManagerFactory;
//	private static EntityManager entityManager;
	private static Map<String, String> properties = new HashMap<>();

	@Option(names = { "--postgres-host" }, description = "Postgresql host ")
	private String host = "localhost";
	@Option(names = { "--postgres-database" }, description = "Postgresql host database")
	private String database = "orderWorkerTestDb";
	@Option(names = { "--postgres-user" }, description = "Postgresql user")
	private String user = "testUser";
	@Option(names = { "--postgres-pass" }, description = "Postgresql pass")
	private String password = "test123";
	@Option(names = { "--postgres-port" }, description = "Postgresql port")
	private String port = "5432";

	public static void main(String[] args) {
		new CommandLine(new OrderWorkerAssignmentSwingApp()).execute(args);

	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {

			try {
				System.out.println("Hello world");
				System.out.println(host);
				System.out.println(database);
				System.out.println(user);
				System.out.println(password);
				String persistenceUnitName = "OriginalPersistenceUnit";
				String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
				System.out.println(jdbcUrl);

				properties.put("javax.persistence.jdbc.url", jdbcUrl);
				properties.put("javax.persistence.jdbc.user", user);
				properties.put("javax.persistence.jdbc.password", password);
				properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				properties.put("hibernate.hbm2ddl.auto", "update");

				entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
//				entityManager = entityManagerFactory.createEntityManager();
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
				e.printStackTrace();
			}
		});
		return null;
	}
}
