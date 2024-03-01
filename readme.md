### Event Paiger Users

#### Description
Event Paiger Users is a multi-module project for managing user-related functionality.

#### Prerequisites
- Java 17
- Maven

#### Modules
- event-paiger-users-web
- event-paiger-users-api
- event-paiger-users-bo
- event-paiger-auth
- event-paiger-user-security

#### Dependencies
- [Spring Boot Starter Web](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-developing-web-applications)
- [Spring Cloud Config Server](https://spring.io/projects/spring-cloud)
- [Spring Boot Starter Test](https://spring.io/guides/gs/testing-web/)
- [Lombok](https://projectlombok.org/)
- [OWASP Dependency Check Maven Plugin](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/index.html)

#### Usage
1. Clone the repository.
2. Build the project using Maven.
3. Run individual modules as needed.

#### Configuration
- Java version: 17
- Spring version: 6.1
- Spring boot version: 3.2.3
- Spring Cloud version: 2023.0.0

#### Build
This project is built using Maven. To build the project, run the following command:
```shell
mvn clean install
```

#### Dockerize application

To create an image of the application, navigate to the project directory containing the Dockerfile and use the following command:
```shell
docker build -t event-paiger-users-image .
```
Then you can run the container from this image using command:
```shell
docker compose up
```
