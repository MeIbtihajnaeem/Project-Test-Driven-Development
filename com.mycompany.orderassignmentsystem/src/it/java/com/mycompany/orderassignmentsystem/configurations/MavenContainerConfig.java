package com.mycompany.orderassignmentsystem.configurations;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MavenContainerConfig implements DBConfig {
	public static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";
	private static final int MAX_RETRIES = 3;
	private static final long RETRY_DELAY_SECONDS = 10;

	@Override
	public void testAndStartDatabaseConnection() {
		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

				EntityManager entityManager = entityManagerFactory.createEntityManager();
				if (entityManager != null && entityManager.isOpen()) {
					entityManager.close();
					break;
				}
			} catch (Exception i) {
				attempt++;
				if (attempt < MAX_RETRIES) {
					try {
						TimeUnit.SECONDS.sleep(RETRY_DELAY_SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	@Override
	public String[] getArguments() {
		Map<String, Object> properties = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).getProperties();

		String jdbcUrl = (String) properties.get("javax.persistence.jdbc.url");
		String user = System.getProperty("postgres.user");
		String password = System.getProperty("postgres.password");
		String[] argsArray = { "--postgres-jdbcUrl=" + jdbcUrl, "--postgres-user=" + user,
				"--postgres-pass=" + password, };
		return argsArray;
	}

}
