/*
 * Configuration steps for the OrderWorkerSwingApp BDD tests.
 * 
 * This class provides the necessary configuration constants used in the BDD test steps.
 * 
 * The configuration includes:
 * 
 * - Database connection details:
 *   - `host`: The hostname of the PostgreSQL database server.
 *   - `port`: The port number of the PostgreSQL database server.
 *   - `database`: The name of the PostgreSQL database, retrieved from system properties.
 *   - `user`: The username for the PostgreSQL database, retrieved from system properties.
 *   - `password`: The password for the PostgreSQL database, retrieved from system properties.
 * 
 * - JPA configuration:
 *   - `PERSISTENCE_UNIT_NAME`: The name of the persistence unit used for JPA.
 * 
 * These constants are used across various BDD steps to ensure consistent and correct database connections and configurations during the tests.
 */

package com.mycompany.orderassignmentsystem.bdd.steps;

/**
 * The Class ConfigSteps.
 */
public class ConfigSteps {

	/** The Constant host. */
	protected static final String host = "localhost";

	/** The Constant port. */
	protected static final String port = "5432";

	/** The Constant database. */
	protected static final String database = System.getProperty("postgres.dbName");

	/** The Constant user. */
	protected static final String user = System.getProperty("postgres.user");

	/** The Constant password. */
	protected static final String password = System.getProperty("postgres.password");

	/** The Constant PERSISTENCE_UNIT_NAME. */
	protected static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";

}