Instructions for Popcorn Palace App

This is a Spring Boot application for managing a movie ticket booking system using PostgreSQL and Docker.

---

Requirements
Before you begin, ensure you have the following installed:

- Java 21.0.5
- Maven
- Docker

---
Setup Instructions
1. Clone the repository
2. Set up PostgreSQL using Docker
The app uses PostgreSQL as the database. You can set it up using Docker with the provided 
compose.yml file.

---

Open the popcorn-palace folder on vscode or on cmd

Build and run the Docker containers:
When you're in vscode and on the popcorn-palace folder, open a new terminal and write:
docker-compose up --build
This will start the PostgreSQL container along with any other services defined in the compose.yml.

3. Configure the application
Check the application.yaml file for the db username, password and ports.

4. Build the project
Once your Docker container is running and PostgreSQL is set up, you can build the project using Maven.

You can see the DB information in src\main\resources\application.yaml.

---

Open a new terminal:

Build the project:
mvn clean install
This will download the dependencies and compile the project.

You can run
mvn clean test
to run the tests located in PopcornPalaceApplicationTests.java

Running the Application
Once the build is complete, you can run the app using the following command:
mvn spring-boot:run
This will start the application locally. The app should now be available at http://localhost:8080.

---

Stopping the Application
To stop the application, you can press CTRL+C in both terminals, for the docker you can run docker-compose down.

