package com.mycompany.orderassignmentsystem.repository.h2database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mycompany.orderassignmentsystem.enumerations.OrderCategory;
import com.mycompany.orderassignmentsystem.enumerations.OrderStatus;
import com.mycompany.orderassignmentsystem.model.CustomerOrder;
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.postgres.OrderDatabaseRepository;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;

public class OrderH2RepositoryTest {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private OrderDatabaseRepository orderDataRepository;
	private WorkerDatabaseRepository workerDataRepository;
	private static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";
	private static Map<String, String> properties = new HashMap<>();

	@Before
	public void onSetUp() {
		properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
		properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
		properties.put("javax.persistence.jdbc.user", "sa");
		properties.put("javax.persistence.jdbc.password", "");
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "create-drop");
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
		entityManager = entityManagerFactory.createEntityManager();
		orderDataRepository = new OrderDatabaseRepository(entityManagerFactory);
		workerDataRepository = new WorkerDatabaseRepository(entityManagerFactory);
	}

	@After
	public void onTearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(orderDataRepository.findAll()).isEmpty();

	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		Worker worker1 = new Worker(1L, "Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		CustomerOrder order2 = new CustomerOrder("Alic", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		order2 = orderDataRepository.save(order2);

		assertThat(orderDataRepository.findAll()).containsExactly(order1, order2);
	}

	@Test
	public void testSave() {
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		assertThat(orderDataRepository.findAll()).containsExactly(order1);

	}

	@Test
	public void testSaveWithManagedOrder() {
		Worker worker = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker = workerDataRepository.save(worker);

		CustomerOrder order = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker);

		entityManager.getTransaction().begin();
		entityManager.persist(order);
		entityManager.getTransaction().commit();

		order = orderDataRepository.save(order);

		assertThat(orderDataRepository.findAll()).containsExactly(order);
	}

	@Test
	public void testSaveOrderWhenException() {
		try {
			Worker worker = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);

			CustomerOrder order = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
					"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker);

			orderDataRepository.save(order);
			fail("Expected an exception to be thrown");

		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("failed to create order.");

		}
	}

	@Test
	public void testDelete() {
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		orderDataRepository.delete(order1);
		assertThat(orderDataRepository.findAll()).isEmpty();

	}

	@Test
	public void testDeleteWithException() {

		try {
			orderDataRepository.delete(null);

			fail("Expected an exception to be thrown");

		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("failed to delete order.");

		}

	}

	@Test
	public void testFindByIdFound() {
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		assertThat(orderDataRepository.findById(order1.getOrderId())).isEqualTo(order1);
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(orderDataRepository.findById(1l)).isNull();

	}

	@Test
	public void testFindByCustomerNameFound() {
		String searchText = "Jhon";
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		assertThat(orderDataRepository.findByCustomerName(searchText)).isEqualTo(Arrays.asList(order1));
	}

	@Test
	public void testFindByCustomerNameNotFound() {
		String searchText = "Jhon";
		assertThat(orderDataRepository.findByCustomerName(searchText)).isEmpty();

	}

	@Test
	public void testFindByCustomerPhoneNumberFound() {
		String searchText = "3401372678";
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		assertThat(orderDataRepository.findByCustomerPhoneNumber(searchText)).isEqualTo(Arrays.asList(order1));
	}

	@Test
	public void testFindByCustomerPhoneNumberNotFound() {
		String searchText = "3401372678";

		assertThat(orderDataRepository.findByCustomerPhoneNumber(searchText)).isEmpty();

	}

	@Test
	public void testFindByOrderDateFound() {
		String searchText = "12-12-2024";
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		assertThat(orderDataRepository.findByDate(searchText)).isEqualTo(Arrays.asList(order1));
	}

	@Test
	public void testFindByOrderDateNotFound() {
		String searchText = "12-12-2024";

		assertThat(orderDataRepository.findByDate(searchText)).isEmpty();

	}

	@Test
	public void testFindByOrderOrderCategoryFound() {
		OrderCategory searchText = OrderCategory.PLUMBER;
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		assertThat(orderDataRepository.findByOrderCategory(searchText)).isEqualTo(Arrays.asList(order1));
	}

	@Test
	public void testFindByOrderCategoryNotFound() {
		OrderCategory searchText = OrderCategory.PLUMBER;
		assertThat(orderDataRepository.findByOrderCategory(searchText)).isEmpty();

	}

	@Test
	public void testFindByOrderOrderStatusFound() {
		OrderStatus searchText = OrderStatus.PENDING;
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		CustomerOrder order1 = new CustomerOrder("Jhon", "Piazza Luigi Dalla", "3401372678", "12-12-2024",
				"No description", OrderCategory.PLUMBER, OrderStatus.PENDING, worker1);
		order1 = orderDataRepository.save(order1);
		assertThat(orderDataRepository.findByOrderStatus(searchText)).isEqualTo(Arrays.asList(order1));
	}

	@Test
	public void testFindByOrderStatusNotFound() {
		OrderStatus searchText = OrderStatus.PENDING;

		assertThat(orderDataRepository.findByOrderStatus(searchText)).isEmpty();

	}

}
