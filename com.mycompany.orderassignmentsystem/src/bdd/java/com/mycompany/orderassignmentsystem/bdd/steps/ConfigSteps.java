package com.mycompany.orderassignmentsystem.bdd.steps;

public class ConfigSteps {
	protected static final String host = "localhost";
	protected static final String port = "5432";
	protected static final String database = System.getProperty("postgres.dbName");
	protected static final String user = System.getProperty("postgres.user");
	protected static final String password = System.getProperty("postgres.password");
	protected static final String PERSISTENCE_UNIT_NAME = "OriginalPersistenceUnit";

}