package com.mycompany.orderAssignmentSystem.controller.utils.extensions;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.orderAssignmentSystem.controller.utils.DatabaseConfigurations;

public class ExtendedDatabaseConfigurations implements DatabaseConfigurations {
	private static final int MAX_RETRIES = 5;
	private static final long RETRY_DELAY_SECONDS = 10;
	private EntityManagerFactory entityManagerFactory;
	private static final Logger LOGGER = LogManager.getLogger(ExtendedDatabaseConfigurations.class);

	public ExtendedDatabaseConfigurations(EntityManagerFactory entityManagerFactory) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public EntityManager getConnection() {
		EntityManager entityManager = null;
		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				entityManager = entityManagerFactory.createEntityManager();
				if (entityManager != null && entityManager.isOpen()) {
					LOGGER.info("EntityManager created successfully!");
					return entityManager;
				}
			} catch (Exception e) {
				LOGGER.error("Failed to create EntityManager (Attempt {}/{}): {}", attempt + 1, MAX_RETRIES,
						e.getMessage());
			} finally {
				attempt++;
				if (attempt < MAX_RETRIES && entityManager != null && entityManager.isOpen()) {
					entityManager.close();
				}
				if (attempt < MAX_RETRIES) {
					try {
						TimeUnit.SECONDS.sleep(RETRY_DELAY_SECONDS);
					} catch (InterruptedException e) {
						LOGGER.error("Thread interrupted while waiting to retry connection: {}", e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		return entityManager;
	}

}
