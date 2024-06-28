# Order Assignment System

[![JAVA CI with Maven in Linux](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/maven.yaml/badge.svg?branch=main)](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/maven.yaml)  [![JAVA CI Unit tests with Maven in Windows & MacOS](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/mac_and_windows_workflow_for_unit_tests.yaml/badge.svg)](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/mac_and_windows_workflow_for_unit_tests.yaml)  [![JAVA IT with Maven in Linux](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/ubuntu_workflow_flow_for_integration_and_end2end_tests.yaml/badge.svg)](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/ubuntu_workflow_flow_for_integration_and_end2end_tests.yaml)  [![Coverage Status](https://coveralls.io/repos/github/MeIbtihajnaeem/Project-Test-Driven-Development/badge.svg?branch=main)](https://coveralls.io/github/MeIbtihajnaeem/Project-Test-Driven-Development?branch=main) [![Java CI with Maven, Docker and SonarCloud in Linux](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/java_ci_maven_docker_sonarcloud.yaml/badge.svg)](https://github.com/MeIbtihajnaeem/Project-Test-Driven-Development/actions/workflows/java_ci_maven_docker_sonarcloud.yaml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=MeIbtihajnaeem_Project-Test-Driven-Development&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=MeIbtihajnaeem_Project-Test-Driven-Development)

## Overview
The Order Assignment System is a Java-based application designed to manage and assign orders to workers. This project follows Test-Driven Development (TDD) practices and includes unit tests, integration tests, and end-to-end tests.

## Project Structure

- **src/main/java**: Contains the main application source code.
  - `com.mycompany.orderassignmentsystem`: Main package for the application.
    - `app`: Contains the main application class.
    - `controller`: Contains the controllers for handling requests.
    - `repository`: Contains repository classes for database interactions.
    - `enumerations`: Contains enumeration classes used in the application.
    - `view`: Contains the classes for the application's GUI.

- **src/test/java**: Contains the unit tests for the application .
  - `com.mycompany.orderassignmentsystem`: Test package mirroring the main application structure.

- **src/it/java**: Contains the integration tests and configuration files for database docker containers.
  - `com.mycompany.orderassignmentsystem`: Integration test package.

- **src/e2e/java**: Contains the end-to-end tests.
  - `com.mycompany.orderassignmentsystem`: End-to-end test package.

- **src/bdd/java**: Contains the end-to-end tests using cucumber.
  - `com.mycompany.orderassignmentsystem`: End-to-end test package.

- **.github/workflows**: Contains GitHub Actions workflows for CI/CD.
  - `maven.yaml`: Workflow for running unit tests, coverage, mutation testing using maven on linux along with coversall report.
  - `java_ci_maven_docker_sonarcloud.yaml`: Workflow for CI with Maven, Docker, and SonarCloud analysis.
  - `mac_and_windows_workflow_for_unit_tests.yaml`: Workflow for running unit tests on Mac and Windows.
  - `ubuntu_workflow_flow_for_integration_and_end2end_tests.yaml`: Workflow for running integration and end-to-end tests on Ubuntu.

## Requirements

- Java 8 or higher

## Profiles

- `jacoco`: This profile when activate only runs unit tests along with coverage using jacoco plugin 

- `mutation-testing-with-coverage`: This profile when activate only runs unit tests along with coverage using jacoco plugin & mutation testing using PIT Mutation plugin also with this profile we can upload the coverage report to coversall.

Here is the rephrased text:

- `integration-test-profile`: When activated, this profile skips unit tests executed by the maven-surefire-plugin and instead starts a PostgreSQL Docker container using the docker-maven-plugin for running integration tests, end-to-end tests, and end-to-end tests with Cucumber via the maven-failsafe-plugin. To activate this profile, use the following command: `mvn verify -Pintegration-test-profile -Dpostgres.user=$USER -Dpostgres.password=$PASSWORD -Dpostgres.dbName=$DATABASE -Dpostgres.server=maven`. Ensure that `postgres.server` is set to `maven`; otherwise, the Docker container will fail to establish a connection, and the tests will start Testcontainers in parallel for testing.
