package com.mycompany.orderassignmentsystem.configurations;

import javax.persistence.EntityManagerFactory;

public interface DBConfig {
	public void testAndStartDatabaseConnection();

	public EntityManagerFactory getEntityManagerFactory();

	public String[] getArguments();
}
