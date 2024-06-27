/*
 * BDD steps for interacting with the database in the OrderWorkerSwingApp.
 * 
 * These steps define the behavior of database operations during Cucumber BDD tests.
 * 
 * The steps include:
 * 
 * - Setting up and tearing down the database environment:
 *   - Initializing EntityManager and EntityManagerFactory with the required properties.
 *   - Handling retries for establishing database connections.
 *   - Cleaning up database connections after each test scenario.
 * 
 * - Interactions with the database:
 *   - Adding, updating, and deleting CustomerOrder and Worker entities.
 *   - Ensuring the database contains specific orders and workers based on given values.
 *   - Verifying the deletion of specific orders and workers from the database.
 * 
 * - Use of testing frameworks and tools:
 *   - Awaitility for handling retries and waiting conditions.
 *   - JPA for managing persistence and transactions.
 * 
 * The steps simulate real-world database operations by:
 * - Creating and persisting CustomerOrder and Worker entities based on test data.
 * - Managing transactions to ensure data consistency during tests.
 * - Handling detached entity instances by merging them before deletion.
 * 
 * These BDD steps ensure the application's database interactions are robust and reliable in various scenarios.
 */

package com.mycompany.orderassignmentsystem.bdd.steps;

import static org.awaitility.Awaitility.await;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.BeforeClass;

import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * The Class DatabaseSteps.
 */
public class DatabaseSteps extends ConfigSteps {

	/** The entity manager factory. */
	private static EntityManagerFactory entityManagerFactory;

	/** The entity manager. */
	private static EntityManager entityManager;

	/** The properties. */
	private static Map<String, String> properties = new HashMap<>();

	/** The Constant MAX_RETRIES. */
	private static final int MAX_RETRIES = 3;

	/** The Constant RETRY_DELAY_SECONDS. */
	private static final long RETRY_DELAY_SECONDS = 10;

	/**
	 * Setup.
	 */
	@BeforeClass
	public static void setup() {

		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				EntityManagerFactory entityManagerFactory = Persistence
						.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

				EntityManager entityManager = entityManagerFactory.createEntityManager();
				if (entityManager != null && entityManager.isOpen()) {
					entityManager.close();
					break;
				}
			} catch (Exception i) {
				attempt++;
				if (attempt < MAX_RETRIES) {
					await().atMost(RETRY_DELAY_SECONDS, TimeUnit.SECONDS);
				}
			}

		}
	}

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		String persistenceUnitName = "OriginalPersistenceUnit";
		String jdbcUrl = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
		properties.put("javax.persistence.jdbc.url", jdbcUrl);
		properties.put("javax.persistence.jdbc.user", USER);
		properties.put("javax.persistence.jdbc.password", PASSWORD);
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "create-drop");

		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		entityManager = entityManagerFactory.createEntityManager();
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

	/**
	 * The database contains the order with the following values.
	 *
	 * @param values the values
	 */
	@Given("The database contains the order with the following values")
	public void the_database_contains_the_order_with_the_following_values(List<List<String>> values) {
		values.forEach(orderValue -> {
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();

			String workerName = orderValue.get(8);
			String workerPhone = orderValue.get(9);
			OrderCategory workerCategory = OrderCategory.valueOf(orderValue.get(10));
			Worker worker = new Worker(workerName, workerPhone, workerCategory);

			worker = entityManager.merge(worker);

			Long orderId = Long.parseLong(orderValue.get(0));
			String customerName = orderValue.get(1);
			String customerPhone = orderValue.get(2);
			OrderCategory orderCategory = OrderCategory.valueOf(orderValue.get(3));
			OrderStatus orderStatus = OrderStatus.valueOf(orderValue.get(4));
			String customerAddress = orderValue.get(5);
			String appointmentDate = orderValue.get(6);
			String orderDescription = orderValue.get(7);
			CustomerOrder order = new CustomerOrder(orderId, customerName, customerAddress, customerPhone,
					appointmentDate, orderDescription, orderCategory, orderStatus, worker);

			entityManager.merge(order);
			transaction.commit();
		});

	}

	/**
	 * The database contains worker with the following values.
	 *
	 * @param values the values
	 */
	@Given("The database contains worker with the following values")
	public void the_database_contains_worker_with_the_following_values(List<List<String>> values) {
		values.forEach(workerValue -> {
			Long workerId = Long.parseLong(workerValue.get(0));
			String workerName = workerValue.get(1);
			String workerPhone = workerValue.get(2);
			OrderCategory workerCategory = OrderCategory.valueOf(workerValue.get(3));
			Worker worker = new Worker(workerId, workerName, workerPhone, workerCategory);
			EntityTransaction workerTransaction = entityManager.getTransaction();
			workerTransaction.begin();
			entityManager.merge(worker);
			workerTransaction.commit();
		});

	}

	/**
	 * The database deletes the order with the following values.
	 *
	 * @param values the values
	 */
	@Then("The database deletes the order with the following values")
	public void the_database_deletes_the_order_with_the_following_values(List<List<String>> values) {
		values.forEach(orderValue -> {

			String workerName = orderValue.get(8);
			String workerPhone = orderValue.get(9);
			OrderCategory workerCategory = OrderCategory.valueOf(orderValue.get(10));
			Worker worker = new Worker(workerName, workerPhone, workerCategory);

			Long orderId = Long.parseLong(orderValue.get(0));
			String customerName = orderValue.get(1);
			String customerPhone = orderValue.get(2);
			OrderCategory orderCategory = OrderCategory.valueOf(orderValue.get(3));
			OrderStatus orderStatus = OrderStatus.valueOf(orderValue.get(4));
			String customerAddress = orderValue.get(5);
			String appointmentDate = orderValue.get(6);
			String orderDescription = orderValue.get(7);
			CustomerOrder order = new CustomerOrder(orderId, customerName, customerAddress, customerPhone,
					appointmentDate, orderDescription, orderCategory, orderStatus, worker);

			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			// This is due to detached instance issue since the entity manager is not closed
			// therefore we have to merge the object before deleting
			entityManager.remove(entityManager.contains(order) ? order : entityManager.merge(order));
			transaction.commit();

		});
	}

	/**
	 * The database deletes the worker with the following values.
	 *
	 * @param values the values
	 */
	@Then("The database deletes the worker with the following values")
	public void the_database_deletes_the_worker_with_the_following_values(List<List<String>> values) {
		try {
			values.forEach(workerValue -> {
				Long workerId = Long.parseLong(workerValue.get(0));
				String workerName = workerValue.get(1);
				String workerPhone = workerValue.get(2);
				OrderCategory workerCategory = OrderCategory.valueOf(workerValue.get(3));
				Worker worker = new Worker(workerId, workerName, workerPhone, workerCategory);

				EntityTransaction transaction = entityManager.getTransaction();
				transaction.begin();
				// This is due to detached instance issue since the entity manager is not closed
				// therefore we have to merge the object before deleting
				entityManager.remove(entityManager.contains(worker) ? worker : entityManager.merge(worker));
				transaction.commit();

			});
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}