# ReliaQuest Entry-Level Java Challenge Solution

## Overview
This is my implementation for the Entry-Level Java Challenge, which involves creating a secure REST API for managing `Employee` entities within the `com.challenge.api` package structure.

## Implementation Details

### 1. Domain Model (`EmployeeImpl.java`)
Implemented the provided `Employee` interface with a concrete class `EmployeeImpl`. It encapsulates all necessary fields (UUID, name, salary, age, job title, email, contract dates) and provides standard getter and setter methods.

### 2. Request Data Transfer Object (`EmployeeRequest.java`)
Created a dedicated DTO, `EmployeeRequest`, to handle incoming data for the `createEmployee` POST endpoint. This helps avoid direct mapping from user input to the final Domain Model and ensures unmodifiable parameters (like UUID) are safely controlled during creation.

### 3. Service Layer (`MockEmployeeService.java`)
Created an `EmployeeService` interface and its concrete implementation `MockEmployeeService` to act as a mock persistence layer.
- Uses a `ConcurrentHashMap` to store `Employee` records in-memory, ensuring thread-safe concurrency.
- The map is pre-populated with two dummy employees upon initialization to facilitate immediate testing of the GET endpoints.
- **Time Complexity:** O(1) average time complexity for retrieving a single employee by UUID and for creation. O(N) for retrieving all employees (as the map values must be aggregated into a List).
- **Space Complexity:** O(N) where N is the total number of stored employees in the internal map.

### 4. Controller (`EmployeeController.java`)
Implemented the HTTP endpoints using standard Spring Web annotations:
- `@GetMapping` for retrieving all employees without filters.
- `@GetMapping("/{uuid}")` for retrieving a specific employee utilizing `@PathVariable`.
- `@PostMapping` for creating a new employee using the mapped `@RequestBody`.
- **Security Validation:** Adhering to the "secure API" constraint outlined in the requirements, the API simulates a lightweight authentication mechanism. The endpoints require an `Authorization` header utilizing the `@RequestHeader` annotation to mimic a webhook connection originating from "Employees-R-US". Missing the header triggers a `401 Unauthorized` exception.

## Environment Prerequisites
The project build scripts are strictly configured via `project-conventions.gradle` to use **Java 17**. 
Please ensure your CLI (`JAVA_HOME`) or your IDE is configured to use JDK 17 before interacting with the Gradle scripts. You will experience an `Unsupported class file major version` error if utilizing an incompatible version.

## Running the Application

### Via Command-Line (CLI)
Navigate to the root directory `entry-level-java-challenge` and execute the formatting, building, and running tasks using the Gradle Wrapper:

1. **Format Code (Spotless):**
   ```bash
   ./gradlew spotlessApply
   ```
2. **Build Project (Tests & Checks):**
   ```bash
   ./gradlew build
   ```
3. **Run the Tomcat Server:**
   ```bash
   ./gradlew bootRun
   ```

### Via IntelliJ IDEA
1. **Open** the project.
2. Under `File > Project Structure...`, verify the **Project SDK** and **Language Level** are set to **Java 17**.
3. Under `Settings > Build, Execution, Deployment > Build Tools > Gradle`, verify the **Gradle JVM** is also set to **Java 17**.
4. In the Project pane, locate `api/src/main/java/com/challenge/api/EntryLevelJavaChallengeApplication.java`.
5. Click the green "Play" icon in the left margin next to `public static void main(...)` to start the application. 

The embedded server will start locally on port `http://localhost:8080`.

## Testing the APIs

Below are examples of how to consume the endpoints using `curl` while demonstrating the required auth header mechanism. 

*(**Windows PowerShell users:** Use `curl.exe` to avoid conflicts with the native `Invoke-WebRequest` alias, and note the escaped double quotes in the JSON POST payload).*

**1. Retrieve All Employees:**
```bash
curl.exe -H "Authorization: webhook-token-here" -X GET http://localhost:8080/api/v1/employee
```

**2. Retrieve Employee by ID:**
```bash
# (Replace the UUID below with a value generated from the Create or GetAll endpoints)
curl.exe -H "Authorization: webhook-token-here" -X GET http://localhost:8080/api/v1/employee/1c1b8096-2a05-45fa-9cf8-594f0b8456b3
```

**3. Create New Employee:**
```bash
curl.exe -X POST -H "Authorization: webhook-token-here" -H "Content-Type: application/json" -d "{\`"firstName\`":\`"Bob\`", \`"lastName\`":\`"Smith\`", \`"jobTitle\`":\`"Software Developer\`", \`"salary\`":75000}" http://localhost:8080/api/v1/employee
```
