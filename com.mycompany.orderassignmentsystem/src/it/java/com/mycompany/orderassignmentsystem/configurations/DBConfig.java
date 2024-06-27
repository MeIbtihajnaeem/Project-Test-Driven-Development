
package com.mycompany.orderassignmentsystem.configurations;

import javax.persistence.EntityManagerFactory;

/**
 * The DBConfig interface defines the contract for database configurations.
 * Implementing classes are responsible for testing and starting the database
 * connection, as well as providing the EntityManagerFactory for creating
 * EntityManager instances.
 * 
 * 
 * 1. If the test is initiated via Eclipse, it defaults to using TestContainer
 * for the database and starts the PostgreSQL Docker test container.
 * 
 * 2. If the test is initiated via "maven" command, it uses the real container
 * for the database and starts the PostgreSQL Docker container as defined in the
 * pom.xml with the "integration-test-profile" profile.
 *
 */
public interface DBConfig {

	/**
	 * Test and start database connection. This method is responsible for attempting
	 * to establish a database connection, with potential retry mechanisms.
	 */
	public void testAndStartDatabaseConnection();

	/**
	 * This method provides an EntityManagerFactory for the configured persistence
	 * unit, allowing the creation of EntityManager instances for database
	 * operations.
	 * 
	 * @return the entity manager factory
	 */
	public EntityManagerFactory getEntityManagerFactory();
}
