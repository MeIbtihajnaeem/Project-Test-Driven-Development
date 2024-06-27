
package com.mycompany.orderassignmentsystem.configurations;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The Class MavenContainerConfig implements DBConfig interface for database
 * configurations. This class is responsible for setting up and testing the
 * database connection using real docker container. It includes retry mechanisms
 * to handle database connection attempts.
 */
public class MavenContainerConfig implements DBConfig {

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

	/**
	 * Test and start database connection. This method attempts to establish a
	 * database connection with a retry mechanism. If the connection fails, it
	 * retries up to MAX_RETRIES times with a delay of RETRY_DELAY_SECONDS between
	 * each attempt.
	 */
	@Override
	public void testAndStartDatabaseConnection() {
		int attempt = 0;
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
	// persistence unit.
	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}
}
