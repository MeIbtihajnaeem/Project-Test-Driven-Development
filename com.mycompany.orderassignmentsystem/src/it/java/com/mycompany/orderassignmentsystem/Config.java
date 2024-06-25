package com.mycompany.orderassignmentsystem;

import com.mycompany.orderassignmentsystem.configurations.DBConfig;
import com.mycompany.orderassignmentsystem.configurations.MavenContainerConfig;
import com.mycompany.orderassignmentsystem.configurations.TestContainerConfig;

public class Config {
	public static DBConfig databaseConfig;

	public static DBConfig getDatabaseConfig() {
		String runningServerFrom = System.getProperty("postgres.server");
		System.out.println("----------server type-------");
		System.out.println(runningServerFrom);
		if (runningServerFrom.equals("maven")) {
			databaseConfig = new MavenContainerConfig();
		} else {
			databaseConfig = new TestContainerConfig();
		}
		return databaseConfig;
	}

}
