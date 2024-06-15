package com.mycompany.orderAssignmentSystem.bdd.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.mycompany.orderAssignmentSystem.enumerations.OrderCategory;
import com.mycompany.orderAssignmentSystem.enumerations.OrderStatus;
import com.mycompany.orderAssignmentSystem.model.CustomerOrder;
import com.mycompany.orderAssignmentSystem.model.Worker;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class DatabaseSteps extends ConfigSteps {
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;
	private static Map<String, String> properties = new HashMap<>();

	@Before
	public void setUp() {
		String persistenceUnitName = "test_myPersistenceUnit";
		String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
		System.out.println(jdbcUrl);

		properties.put("javax.persistence.jdbc.url", jdbcUrl);
		properties.put("javax.persistence.jdbc.user", user);
		properties.put("javax.persistence.jdbc.password", password);
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "create-drop");

		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		entityManager = entityManagerFactory.createEntityManager();
	}

	@After
	public void tearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

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
