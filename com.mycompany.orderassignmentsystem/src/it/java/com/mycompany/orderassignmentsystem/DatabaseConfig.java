package com.mycompany.orderassignmentsystem;

import com.mycompany.orderassignmentsystem.configurations.DBConfig;
import com.mycompany.orderassignmentsystem.configurations.MavenContainerConfig;
import com.mycompany.orderassignmentsystem.configurations.TestContainerConfig;

public class DatabaseConfig {
	public static DBConfig databaseConfig;

	public static DBConfig getDatabaseConfig() {
		String runningServerFrom = System.getProperty("postgres.server");
		if (runningServerFrom == null) {
			databaseConfig = new TestContainerConfig();
		} else if (runningServerFrom.equals("maven")) {
			databaseConfig = new MavenContainerConfig();
		}
		return databaseConfig;
	}

}
