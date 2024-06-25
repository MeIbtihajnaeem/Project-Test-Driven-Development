package com.mycompany.orderassignmentsystem.configurations;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class TestContainerConfig implements DBConfig {
	public static final String DB = "orderWorkerTestDb";
	public static final String PASSWORD = "test123";
	public static final String USER = "testUser";
	public static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";
	private static final int MAX_RETRIES = 3;
	private static final long RETRY_DELAY_SECONDS = 10;
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer postgress = new GenericContainer("postgis/postgis:16-3.4-alpine")
			.withExposedPorts(5432).withEnv("POSTGRES_USER", USER).withEnv("POSTGRES_PASSWORD", PASSWORD)
			.withEnv("POSTGRES_DB", DB).waitingFor(Wait.forListeningPort());

	public static final String HOST = postgress.getHost();
	public static final Integer PORT = postgress.isRunning() ? postgress.getMappedPort(5432) : 5432;

	@Override
	public void testAndStartDatabaseConnection() {
		int attempt = 0;

		// Start test container if not run by Maven
		postgress.start();
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
		Properties properties = new Properties();
		properties.put("javax.persistence.jdbc.url",
				"jdbc:postgresql://" + postgress.getHost() + ":" + postgress.getMappedPort(5432) + "/" + DB);
		properties.put("javax.persistence.jdbc.user", USER);
		properties.put("javax.persistence.jdbc.password", PASSWORD);
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
	}

	@Override
	public String[] getArguments() {
		String jdbcUrl = "jdbc:postgresql://" + postgress.getHost() + ":" + postgress.getMappedPort(5432) + "/" + DB;
		String[] argsArray = { "--postgres-jdbcUrl=" + jdbcUrl, "--postgres-user=" + USER,
				"--postgres-pass=" + PASSWORD };
		return argsArray;
	}

}
