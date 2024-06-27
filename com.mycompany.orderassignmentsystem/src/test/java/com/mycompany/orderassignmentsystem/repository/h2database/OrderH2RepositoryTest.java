/*
 * Unit tests for the OrderDatabaseRepository class with H2 in-memory database.
 *
 * These tests ensure that the repository methods for managing CustomerOrder entity 
 * function correctly. The tests cover various CRUD operations and querying methods using an 
 * H2 in-memory database, simulating a PostgreSQL environment.
 *
 * The setup method initialises the H2 in-memory database and the repositories. 
 * The teardown method cleans up the database resources.
 *
 * Test cases include:
 * - findAll()
 * - save()
 * - delete()
 * - findById()
 * - findByCustomerName()
 * - findByCustomerPhoneNumber()
 * - findByDate()
 * - findByOrderCategory()
 * - findByOrderStatus()
 *
 * @see OrderDatabaseRepository
 * @see WorkerDatabaseRepository
 * @see CustomerOrder
 * @see Worker
 * @see OrderCategory
 * @see OrderStatus
 */

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

/**
 * The Class OrderH2RepositoryTest.
 */
public class OrderH2RepositoryTest {

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/** The entity manager. */
	private EntityManager entityManager;

	/** The order data repository. */
	private OrderDatabaseRepository orderDataRepository;

	/** The worker data repository. */
	private WorkerDatabaseRepository workerDataRepository;

	/** The Constant PERSISTENCE_UNIT_NAME. */
	private static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";

	/** The properties. */
	private static Map<String, String> properties = new HashMap<>();

	/** The worker id 1. */
	private long WORKER_ID_1 = 1l;

	/** The worker name 1. */
	private String WORKER_NAME_1 = "Bob";

	/** The worker phone 1. */
	private String WORKER_PHONE_1 = "3401372678";

	/** The worker category 1. */
	private OrderCategory WORKER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The customer name 1. */
	private String CUSTOMER_NAME_1 = "Jhon";

	/** The customer phone 1. */
	private String CUSTOMER_PHONE_1 = "3401372671";

	/** The customer address 1. */
	private String CUSTOMER_ADDRESS_1 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 1. */
	private String ORDER_APPOINTMENT_DATE_1 = "12-12-2024";

	/** The order description 1. */
	private String ORDER_DESCRIPTION_1 = "Please be on time";

	/** The order category 1. */
	private OrderCategory ORDER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The order status 1. */
	private OrderStatus ORDER_STATUS_1 = OrderStatus.PENDING;

	/** The customer name 2. */
	private String CUSTOMER_NAME_2 = "Alic";

	/** The customer phone 2. */
	private String CUSTOMER_PHONE_2 = "3401372672";

	/** The customer address 2. */
	private String CUSTOMER_ADDRESS_2 = "1234 Main Street , Apt 101, Springfield, USA 12345";

	/** The order appointment date 2. */
	private String ORDER_APPOINTMENT_DATE_2 = "12-12-2024";

	/** The order description 2. */
	private String ORDER_DESCRIPTION_2 = "Please bring tape";

	/** The order category 2. */
	private OrderCategory ORDER_CATEGORY_2 = OrderCategory.PLUMBER;

	/** The order status 2. */
	private OrderStatus ORDER_STATUS_2 = OrderStatus.PENDING;

	/** The worker 1. */
	private Worker worker1;

	/**
	 * On set up.
	 */
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
		worker1 = new Worker(WORKER_ID_1, WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		worker1 = workerDataRepository.save(worker1);
	}

	/**
	 * On tear down.
	 */
	@After
	public void onTearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

	/**
	 * Test find all method when database is empty.
	 */
	@Test
	public void testFindAllMethodWhenDatabaseIsEmpty() {
		// Setup & Exercise & Verify
		assertThat(orderDataRepository.findAll()).isEmpty();

	}

	/**
	 * Test find all method when database is not empty.
	 */
	@Test
	public void testFindAllMethodWhenDatabaseIsNotEmpty() {
		// Setup
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		CustomerOrder order2 = new CustomerOrder(CUSTOMER_NAME_2, CUSTOMER_ADDRESS_2, CUSTOMER_PHONE_2,
				ORDER_APPOINTMENT_DATE_2, ORDER_DESCRIPTION_2, ORDER_CATEGORY_2, ORDER_STATUS_2, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);
		order2 = orderDataRepository.save(order2);

		// Verify
		assertThat(orderDataRepository.findAll()).containsExactly(order1, order2);
	}

	/**
	 * Test save method.
	 */
	@Test
	public void testSaveMethod() {
		// Setup
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);

		// Verify
		assertThat(orderDataRepository.findAll()).containsExactly(order1);

	}

	/**
	 * Test save method with managed order.
	 */
	@Test
	public void testSaveMethodWithManagedOrder() {
		// Setup
		CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		entityManager.getTransaction().begin();
		entityManager.persist(order);
		entityManager.getTransaction().commit();
		order = orderDataRepository.save(order);

		// Verify
		assertThat(orderDataRepository.findAll()).containsExactly(order);
	}

	/**
	 * Test save method when exception.
	 */
	@Test
	public void testSaveMethodWhenException() {
		try {
			// Setup
			Worker worker = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);

			CustomerOrder order = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
					ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker);

			// Exercise
			orderDataRepository.save(order);

			// Verify
			fail("Expected an exception to be thrown");

		} catch (Exception e) {

			// Verify
			assertThat(e.getMessage()).isEqualTo("failed to create order.");

		}
	}

	/**
	 * Test delete method.
	 */
	@Test
	public void testDeleteMethod() {
		// Setup
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);
		orderDataRepository.delete(order1);

		// Verify
		assertThat(orderDataRepository.findAll()).isEmpty();

	}

	/**
	 * Test delete method when exception.
	 */
	@Test
	public void testDeleteMethodWhenException() {

		try {
			// Setup & Exercise
			orderDataRepository.delete(null);

			// Verify
			fail("Expected an exception to be thrown");

		} catch (IllegalArgumentException e) {
			// Verify
			assertThat(e.getMessage()).isEqualTo("failed to delete order.");

		}

	}

	/**
	 * Test find by id method when found.
	 */
	@Test
	public void testFindByIdMethodWhenFound() {
		// Setup
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);

		// Verify
		assertThat(orderDataRepository.findById(order1.getOrderId())).isEqualTo(order1);
	}

	/**
	 * Test find by id method when not found.
	 */
	@Test
	public void testFindByIdMethodWhenNotFound() {
		// Setup & Exercise & Verify
		assertThat(orderDataRepository.findById(1l)).isNull();

	}

	/**
	 * Test find by customer name method when found.
	 */
	@Test
	public void testFindByCustomerNameMethodWhenFound() {
		// Setup
		String searchText = CUSTOMER_NAME_1;
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);

		// Verify
		assertThat(orderDataRepository.findByCustomerName(searchText)).isEqualTo(Arrays.asList(order1));
	}

	/**
	 * Test find by customer name method when not found.
	 */
	@Test
	public void testFindByCustomerNameMethodWhenNotFound() {
		// Setup
		String searchText = CUSTOMER_NAME_1;

		// Exercise & Verify
		assertThat(orderDataRepository.findByCustomerName(searchText)).isEmpty();

	}

	/**
	 * Test find by customer phone number method when found.
	 */
	@Test
	public void testFindByCustomerPhoneNumberMethodWhenFound() {
		// Setup
		String searchText = CUSTOMER_PHONE_1;
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);

		// Verify
		assertThat(orderDataRepository.findByCustomerPhoneNumber(searchText)).isEqualTo(Arrays.asList(order1));
	}

	/**
	 * Test find by customer phone number method when not found.
	 */
	@Test
	public void testFindByCustomerPhoneNumberMethodWhenNotFound() {
		// Setup
		String searchText = CUSTOMER_PHONE_1;

		// Exercise & Verify
		assertThat(orderDataRepository.findByCustomerPhoneNumber(searchText)).isEmpty();

	}

	/**
	 * Test find by order method when date found.
	 */
	@Test
	public void testFindByOrderMethodWhenDateFound() {
		// Setup
		String searchText = ORDER_APPOINTMENT_DATE_1;
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);

		// Verify
		assertThat(orderDataRepository.findByDate(searchText)).isEqualTo(Arrays.asList(order1));
	}

	/**
	 * Test find by order date method when not found.
	 */
	@Test
	public void testFindByOrderDateMethodWhenNotFound() {
		// Setup
		String searchText = ORDER_APPOINTMENT_DATE_1;

		// Exercise & Verify
		assertThat(orderDataRepository.findByDate(searchText)).isEmpty();

	}

	/**
	 * Test find by order order method when category found.
	 */
	@Test
	public void testFindByOrderOrderMethodWhenCategoryFound() {
		// Setup
		OrderCategory searchText = ORDER_CATEGORY_1;
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);

		// Verify
		assertThat(orderDataRepository.findByOrderCategory(searchText)).isEqualTo(Arrays.asList(order1));
	}

	/**
	 * Test find by order category method when not found.
	 */
	@Test
	public void testFindByOrderCategoryMethodWhenNotFound() {
		// Setup
		OrderCategory searchText = ORDER_CATEGORY_1;

		// Exercise & Verify
		assertThat(orderDataRepository.findByOrderCategory(searchText)).isEmpty();

	}

	/**
	 * Test find by order method when order status found.
	 */
	@Test
	public void testFindByOrderMethodWhenOrderStatusFound() {
		// Setup
		OrderStatus searchText = ORDER_STATUS_1;
		CustomerOrder order1 = new CustomerOrder(CUSTOMER_NAME_1, CUSTOMER_ADDRESS_1, CUSTOMER_PHONE_1,
				ORDER_APPOINTMENT_DATE_1, ORDER_DESCRIPTION_1, ORDER_CATEGORY_1, ORDER_STATUS_1, worker1);

		// Exercise
		order1 = orderDataRepository.save(order1);

		// Verify
		assertThat(orderDataRepository.findByOrderStatus(searchText)).isEqualTo(Arrays.asList(order1));
	}

	/**
	 * Test find by order status method when not found.
	 */
	@Test
	public void testFindByOrderStatusMethodWhenNotFound() {
		// Setup
		OrderStatus searchText = ORDER_STATUS_1;

		// Exercise & Verify
		assertThat(orderDataRepository.findByOrderStatus(searchText)).isEmpty();

	}

}
