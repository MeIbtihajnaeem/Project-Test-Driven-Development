
package com.mycompany.orderassignmentsystem.configurations;

import static org.awaitility.Awaitility.await;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * The Class TestContainerConfig implements DBConfig interface for test
 * container configurations. This class is responsible for setting up and
 * testing the database connection using Testcontainers. It includes retry
 * mechanisms to handle database connection attempts.
 */
public class TestContainerConfig implements DBConfig {

	/** The Constant DB. */
	// The value of database for test container
	public static final String DB = "orderWorkerTestDb";

	/** The Constant PASSWORD. */
	// The value of password for test container
	public static final String PASSWORD = "test123";

	/** The Constant USER. */
	// The name of user for test container
	public static final String USER = "testUser";

	/** The Constant PERSISTENCE_UNIT_NAME. */
	// The name of the persistence unit used in
	// the persistence.xml configuration file
	public static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";

	/** The Constant MAX_RETRIES. */
	// The maximum number of retry attempts for establishing a database connection
	private static final int MAX_RETRIES = 3;

	/** The Constant RETRY_DELAY_SECONDS. */
	// The delay between each retry attempt in seconds
	private static final long RETRY_DELAY_SECONDS = 10;

	/** The Constant postgress. */
	// Define and configure the PostgreSQL test container using Testcontainers
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer postgress = new GenericContainer("postgis/postgis:16-3.4-alpine")
			.withExposedPorts(5432).withEnv("POSTGRES_USER", USER).withEnv("POSTGRES_PASSWORD", PASSWORD)
			.withEnv("POSTGRES_DB", DB).waitingFor(Wait.forListeningPort());

	/** The Constant HOST. */
	// The host address of the test container
	public static final String HOST = postgress.getHost();

	/** The Constant PORT. */
	// The port number on which the test container is running
	public static final Integer PORT = postgress.isRunning() ? postgress.getMappedPort(5432) : 5432;

	/**
	 * Test and start database connection.
	 */
	@Override
	public void testAndStartDatabaseConnection() {
		int attempt = 0;

		// Start test container if not already running
		postgress.start();
		while (attempt < MAX_RETRIES) {
			try {
				// Attempt to get the EntityManagerFactory
				EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

				// Create an EntityManager from the factory
				EntityManager entityManager = entityManagerFactory.createEntityManager();

				// Check if the EntityManager is successfully created and open
				if (entityManager != null && entityManager.isOpen()) {
					entityManager.close();
					break;
				}
			} catch (Exception i) {
				attempt++;
				if (attempt < MAX_RETRIES) {
					// Wait for the specified retry delay before the next attempt
					await().atMost(RETRY_DELAY_SECONDS, TimeUnit.SECONDS);
				}
			}

		}

	}

	/**
	 * Gets the entity manager factory.
	 *
	 * @return the entity manager factory
	 */
	// This method creates and returns an EntityManagerFactory for the specified
	// persistence unit, using the properties configured for the test container.
	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		Properties properties = new Properties();
		properties.put("javax.persistence.jdbc.url",
				"jdbc:postgresql://" + postgress.getHost() + ":" + postgress.getMappedPort(5432) + "/" + DB);
		properties.put("javax.persistence.jdbc.user", USER);
		properties.put("javax.persistence.jdbc.password", PASSWORD);
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
	}

}
