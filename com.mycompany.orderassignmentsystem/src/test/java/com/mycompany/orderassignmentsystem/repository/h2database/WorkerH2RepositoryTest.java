/**
 * Unit tests for the WorkerDatabaseRepository class with H2 in-memory database.
 *
 * These tests ensure that the repository methods for managing Worker entity 
 * function correctly. The tests cover various CRUD operations and querying methods using an 
 * H2 in-memory database, simulating a PostgreSQL environment.
 *
 * The setup method initialises the H2 in-memory database and the repositories. 
 * The teardown method cleans up the database resources.
 *
 * Test cases include:
 * - findAll()
 * - findById() 
 * - findByName()
 * - findByOrderCategory()
 * - findByPhoneNumber()
 * - save()
 * - delete()
 *
 * @see WorkerDatabaseRepository
 * @see Worker
 * @see OrderCategory
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
import com.mycompany.orderassignmentsystem.model.Worker;
import com.mycompany.orderassignmentsystem.repository.postgres.WorkerDatabaseRepository;

/**
 * The Class WorkerH2RepositoryTest.
 */
public class WorkerH2RepositoryTest {

	/** The entity manager factory. */
	private EntityManagerFactory entityManagerFactory;

	/** The entity manager. */
	private EntityManager entityManager;

	/** The worker data repository. */
	private WorkerDatabaseRepository workerDataRepository;

	/** The Constant PERSISTENCE_UNIT_NAME. */
	private static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";

	/** The properties. */
	private static Map<String, String> properties = new HashMap<>();

	/** The worker name 1. */
	private String WORKER_NAME_1 = "Bob";

	/** The worker phone 1. */
	private String WORKER_PHONE_1 = "3401372678";

	/** The worker category 1. */
	private OrderCategory WORKER_CATEGORY_1 = OrderCategory.PLUMBER;

	/** The worker name 2. */
	private String WORKER_NAME_2 = "Alic";

	/** The worker phone 2. */
	private String WORKER_PHONE_2 = "3401372679";

	/** The worker category 2. */
	private OrderCategory WORKER_CATEGORY_2 = OrderCategory.ELECTRICIAN;

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
		workerDataRepository = new WorkerDatabaseRepository(entityManagerFactory);
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
	 * Test find all method when empty.
	 */
	@Test
	public void testFindAllMethodWhenEmpty() {
		assertThat(workerDataRepository.findAll()).isEmpty();

	}

	/**
	 * Test find all method when not empty.
	 */
	@Test
	public void testFindAllMethodWhenNotEmpty() {
		// Setup
		Worker worker1 = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);
		Worker worker2 = new Worker(WORKER_NAME_2, WORKER_PHONE_2, WORKER_CATEGORY_2);

		// Exercise
		worker1 = workerDataRepository.save(worker1);
		worker2 = workerDataRepository.save(worker2);

		// Verify
		assertThat(workerDataRepository.findAll()).containsExactly(worker1, worker2);

	}

	/**
	 * Test find by id method when empty.
	 */
	@Test
	public void testFindByIdMethodWhenEmpty() {
		// Setup & Exercise & Verify
		assertThat(workerDataRepository.findById(1l)).isNull();

	}

	/**
	 * Test find by id method when exception.
	 */
	@Test
	public void testFindByIdMethodWhenException() {

		try {
			// Setup & Exercise
			workerDataRepository.findById(null);

			// Verify
			fail("Expected an exception to be thrown");

		} catch (Exception e) {

			// Verify
			assertThat(e.getMessage()).isEqualTo("failed to get worker by id.");

		}
	}

	/**
	 * Test find by id method when not empty.
	 */
	@Test
	public void testFindByIdMethodWhenNotEmpty() {
		// Setup
		Worker worker1 = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		// Exercise
		worker1 = workerDataRepository.save(worker1);

		// Verify
		assertThat(workerDataRepository.findById(worker1.getWorkerId())).isEqualTo(worker1);
	}

	/**
	 * Test find by name method when empty.
	 */
	@Test
	public void testFindByNameMethodWhenEmpty() {
		// Setup
		String searchText = WORKER_NAME_1;

		// Exercise & Verify
		assertThat(workerDataRepository.findByName(searchText)).isEmpty();
	}

	/**
	 * Test find by name method when not empty.
	 */
	@Test
	public void testFindByNameMethodWhenNotEmpty() {
		// Setup
		String searchText = WORKER_NAME_1;
		Worker worker1 = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		// Exercise
		worker1 = workerDataRepository.save(worker1);

		// Verify
		assertThat(workerDataRepository.findByName(searchText)).isEqualTo(Arrays.asList(worker1));

	}

	/**
	 * Test find by category method when empty.
	 */
	@Test
	public void testFindByCategoryMethodWhenEmpty() {
		// Setup
		OrderCategory searchText = WORKER_CATEGORY_1;

		// Exercise & Verify
		assertThat(workerDataRepository.findByOrderCategory(searchText)).isEmpty();

	}

	/**
	 * Test find by category method when not empty.
	 */
	@Test
	public void testFindByCategoryMethodWhenNotEmpty() {
		// Setup
		OrderCategory searchText = WORKER_CATEGORY_1;
		Worker worker1 = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		// Exercise
		worker1 = workerDataRepository.save(worker1);

		// Verify
		assertThat(workerDataRepository.findByOrderCategory(searchText)).isEqualTo(Arrays.asList(worker1));

	}

	/**
	 * Test find by phone number method when empty.
	 */
	@Test
	public void testFindByPhoneNumberMethodWhenEmpty() {
		// Setup
		String searchText = WORKER_PHONE_1;

		// Exercise && Verify
		assertThat(workerDataRepository.findByPhoneNumber(searchText)).isNull();

	}

	/**
	 * Test find by phone number method when not empty.
	 */
	@Test
	public void testFindByPhoneNumberMethodWhenNotEmpty() {
		// Setup
		String searchText = WORKER_PHONE_1;
		Worker worker1 = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		// Exercise
		worker1 = workerDataRepository.save(worker1);

		// Verify
		assertThat(workerDataRepository.findByPhoneNumber(searchText)).isEqualTo(worker1);
	}

	/**
	 * Test save method.
	 */
	@Test
	public void testSaveMethod() {
		// Setup
		Worker worker1 = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		// Exercise
		worker1 = workerDataRepository.save(worker1);

		// Verify
		assertThat(workerDataRepository.findAll()).containsExactly(worker1);

	}

	/**
	 * Test save method when exception.
	 */
	@Test
	public void testSaveMethodWhenException() {

		try {
			// Setup & Exercise
			workerDataRepository.save(null);

			// Verify
			fail("Expected an exception to be thrown");

		} catch (Exception e) {
			// Verify
			assertThat(e.getMessage()).isEqualTo("failed to create worker.");

		}
	}

	/**
	 * Test delete method.
	 */
	@Test
	public void testDeleteMethod() {
		// Setup
		Worker worker1 = new Worker(WORKER_NAME_1, WORKER_PHONE_1, WORKER_CATEGORY_1);

		// Exercise
		worker1 = workerDataRepository.save(worker1);
		worker1 = workerDataRepository.findById(worker1.getWorkerId());
		workerDataRepository.delete(worker1);

		// Verify
		assertThat(workerDataRepository.findAll()).isEmpty();
	}

	/**
	 * Test delete method when exception.
	 */
	@Test
	public void testDeleteMethodWhenException() {
		try {
			// Setup & Exercise
			workerDataRepository.delete(null);

			// Verify
			fail("Expected an exception to be thrown");

		} catch (Exception e) {
			// Verify
			assertThat(e.getMessage()).isEqualTo("failed to delete worker.");

		}
	}

}
