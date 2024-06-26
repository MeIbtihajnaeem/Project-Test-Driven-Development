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

public class WorkerH2RepositoryTest {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
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
		workerDataRepository = new WorkerDatabaseRepository(entityManagerFactory);
	}

	@After
	public void onTearDown() {
		entityManagerFactory.close();
		entityManager.close();
	}

	@Test
	public void testFindAllWhenEmpty() {
		assertThat(workerDataRepository.findAll()).isEmpty();

	}

	@Test
	public void testFindAllWhenNotEmpty() {
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		Worker worker2 = new Worker("Alic", "3401372679", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		worker2 = workerDataRepository.save(worker2);
		assertThat(workerDataRepository.findAll()).containsExactly(worker1, worker2);

	}

	@Test
	public void testFindByIdWhenEmpty() {
		assertThat(workerDataRepository.findById(1l)).isNull();

	}

	@Test
	public void testFindByIdWhenException() {

		try {
			Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
			worker1.setWorkerId(1L);
			workerDataRepository.findById(null);
			fail("Expected an exception to be thrown");

		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("failed to get worker by id.");

		}
	}

	@Test
	public void testFindByIdWhenNotEmpty() {
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		assertThat(workerDataRepository.findById(worker1.getWorkerId())).isEqualTo(worker1);

	}

	@Test
	public void testFindByNameWhenEmpty() {
		String searchText = "Bob";
		assertThat(workerDataRepository.findByName(searchText)).isEmpty();
	}

	@Test
	public void testFindByNameWhenNotEmpty() {
		String searchText = "Bob";
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		assertThat(workerDataRepository.findByName(searchText)).isEqualTo(Arrays.asList(worker1));

	}

	@Test
	public void testFindByCategoryWhenEmpty() {
		OrderCategory searchText = OrderCategory.PLUMBER;
		assertThat(workerDataRepository.findByOrderCategory(searchText)).isEmpty();

	}

	@Test
	public void testFindByCategoryWhenNotEmpty() {
		OrderCategory searchText = OrderCategory.PLUMBER;
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		assertThat(workerDataRepository.findByOrderCategory(searchText)).isEqualTo(Arrays.asList(worker1));

	}

	@Test
	public void testFindByPhoneNumberWhenEmpty() {
		String searchText = "3401372678";

		assertThat(workerDataRepository.findByPhoneNumber(searchText)).isNull();

	}

	@Test
	public void testFindByPhoneNumberWhenNotEmpty() {
		String searchText = "3401372678";
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		assertThat(workerDataRepository.findByPhoneNumber(searchText)).isEqualTo(worker1);
	}

	@Test
	public void testSave() {
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);

		assertThat(workerDataRepository.findAll()).containsExactly(worker1);

	}

	@Test
	public void testSaveWhenException() {

		try {

			workerDataRepository.save(null);
			fail("Expected an exception to be thrown");

		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("failed to create worker.");

		}
	}

	@Test
	public void testDelete() {
		Worker worker1 = new Worker("Bob", "3401372678", OrderCategory.PLUMBER);
		worker1 = workerDataRepository.save(worker1);
		worker1 = workerDataRepository.findById(worker1.getWorkerId());
		workerDataRepository.delete(worker1);
		assertThat(workerDataRepository.findAll()).isEmpty();
	}

	@Test
	public void testDeleteWhenException() {
		try {

			workerDataRepository.delete(null);
			fail("Expected an exception to be thrown");

		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("failed to delete worker.");

		}
	}

}
