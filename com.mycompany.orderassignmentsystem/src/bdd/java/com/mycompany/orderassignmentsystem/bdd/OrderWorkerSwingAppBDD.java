package com.mycompany.orderassignmentsystem.bdd;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mycompany.orderassignmentsystem.Config;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/bdd/resources", monochrome = true)
public class OrderWorkerSwingAppBDD extends Config {
	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
		getDatabaseConfig().testAndStartDatabaseConnection();
	}
}
