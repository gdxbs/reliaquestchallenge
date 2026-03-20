# Challenges, Drawbacks, and Alternative Approaches

This document outlines the hurdles encountered while completing the Entry-Level Java Challenge and presents alternative implementations, detailed error handling strategies, and unit testing approaches.

---

## 1. Challenges and Drawbacks Encountered

### JDK Version Mismatch (Java 25 vs. Gradle 7.6)
**Challenge:** The local command-line environment was utilizing Java 25. However, the project's bundled Gradle Wrapper (version 7.6.4) and the Diffplug Spotless plugin do not support Java versions beyond Java 21, resulting in an `Unsupported class file major version 69` error during the CLI build process.
**Drawback:** This prevented the use of native CLI commands (`./gradlew build`) without downgrading the system's `JAVA_HOME` or relying on an IDE (like IntelliJ IDEA) configured with a Java 17 SDK.

### Vague Security Requirements
**Challenge:** The prompt requested a "protected, secure REST API for consumption by Employees-R-US web hooks." However, the project's `project-conventions.gradle` did not include a Spring Security dependency (`spring-boot-starter-security`). 
**Drawback:** Injecting full Spring Security would have been over-engineered and deviated from the constraints of the assessment. The workaround—a simulated `@RequestHeader` check—fulfills the prompt playfully but lacks cryptographic token validation (like JWT) and proper filter-chain security.

### In-Memory Data Storage
**Challenge:** The assessment indicated not to worry about an actual persistence layer, so a `ConcurrentHashMap` was used inside `MockEmployeeService`.
**Drawback:** Data is highly volatile. Any server restart wipes out created records. Furthermore, simulating database behavior in memory lacks pagination, complex querying, and true transactional integrity.

---

## 2. Alternative Implementation Approaches

### A. Implementing Spring Security
Instead of simulating security through controller headers, a more robust alternative is adding `spring-boot-starter-security`.
- **How:** Implement an `ApiKeyAuthenticationFilter` extending `OncePerRequestFilter` that intercepts all `/api/v1/employee` requests and validates an API key against a secure properties file or environment variable.
- **Trade-offs:** 
  - *Pros:* Proper separation of concerns; security logic is removed from the controller. Automatically handles CORS and CSRF if needed.
  - *Cons:* Steeper learning curve, requires more configuration boilerplate, and can complicate simple unit testing.

### B. Integrating a Real Persistence Layer (Spring Data JPA)
Instead of a `ConcurrentHashMap`, we could integrate an H2 In-Memory Database or a Postgres database.
- **How:** Add `spring-boot-starter-data-jpa` and `h2` database dependencies. Convert `EmployeeImpl` into a JPA `@Entity` mapped to an `employees` table. Replace `MockEmployeeService` with an `EmployeeRepository` interface extending `JpaRepository`.
- **Trade-offs:**
  - *Pros:* Provides out-of-the-box CRUD operations, transactional support (`@Transactional`), and data persistence that can easily migrate to an actual cloud database.
  - *Cons:* Introduces the overhead of Object-Relational Mapping (ORM) and requires schema management (e.g., Flyway or Liquibase).

### C. Upgrading the Gradle Wrapper
Instead of relying on an IDE to bypass the Java 25 mismatch, we could upgrade the project's infrastructure.
- **How:** Upgrade Gradle to version 8.11+ using `./gradlew wrapper --gradle-version 8.11.1` from a compatible JDK environment.
- **Trade-offs:** Ensures modern compatibility but might break older, deprecated plugins required by the initial challenge snapshot.

---

## 3. Error Handling Enhancements

Currently, the application throws raw `ResponseStatusException` instances which result in generic Spring Boot error JSON payloads.

**Proposed Implementation: Global Exception Handling**
- Create a `@ControllerAdvice` class (e.g., `GlobalExceptionHandler`).
- Create custom domain exceptions like `EmployeeNotFoundException` and `InvalidEmployeeRequestException`.
- Use an `@ExceptionHandler(EmployeeNotFoundException.class)` method to intercept these errors and return a standardized structure:
  ```json
  {
    "timestamp": "2026-03-20T14:00:00Z",
    "status": 404,
    "error": "Not Found",
    "message": "Employee with UUID 1234-5678 not found."
  }
  ```

**Trade-offs:**
- *Pros:* Highly consistent error contracts for the API consumers (Employees-R-US web hooks). Easier debugging and logging logic centralization.
- *Cons:* Slight increase in class count and maintenance of custom exceptions.

---

## 4. Unit Testing Strategy

While test cases were omitted per assessment instructions, production-grade applications require rigorous testing.

**Proposed Implementation:**
1. **Service Layer (Unit Tests):**
   - Use **JUnit 5** and **Mockito**.
   - Mock the data source (or mock repository if JPA was used) via `@Mock` and inject it into the service using `@InjectMocks`.
   - Assert that `createEmployee` correctly assigns default values (like `contractHireDate`) and filters out bad input.

2. **Controller Layer (Integration Tests):**
   - Use **Spring `@WebMvcTest`** with `MockMvc` to test the HTTP layer without booting the entire server.
   - Mock the `EmployeeService` using `@MockBean`.
   - Perform `.andExpect(status().isOk())` checks to verify routing, JSON serialization, and the `@RequestHeader` authorization logic. 
   - Ensure a request lacking the `Authorization` header properly returns `.andExpect(status().isUnauthorized())`.

**Trade-offs:**
- *Pros:* Prevents future regressions and serves as dynamic documentation of expected application behavior.
- *Cons:* Writing and maintaining tests can double development time initially and makes refactoring slightly more tedious due to brittle tests.
