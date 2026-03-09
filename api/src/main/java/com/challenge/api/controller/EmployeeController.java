package com.challenge.api.controller;

import com.challenge.api.model.Employee;
import com.challenge.api.request.EmployeeRequest;
import com.challenge.api.service.EmployeeService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fill in the missing aspects of this Spring Web REST Controller. Don't forget to add a Service layer.
 */
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee models as necessary.
     * Simulated secure API by requiring an Authorization header (e.g., Bearer token or API Key from Employees-R-US webhook).
     * @return One or more Employees.
     */
    @GetMapping
    public List<Employee> getAllEmployees(@RequestHeader(value = "Authorization", required = false) String authToken) {
        verifyAuthToken(authToken);
        return employeeService.getAllEmployees();
    }

    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee model as necessary.
     * @param uuid Employee UUID
     * @return Requested Employee if exists
     */
    @GetMapping("/{uuid}")
    public Employee getEmployeeByUuid(@PathVariable UUID uuid, @RequestHeader(value = "Authorization", required = false) String authToken) {
        verifyAuthToken(authToken);
        return employeeService.getEmployeeByUuid(uuid);
    }

    /**
     * @implNote Need not be concerned with an actual persistence layer.
     * @param requestBody hint!
     * @return Newly created Employee
     */
    @PostMapping
    public Employee createEmployee(@RequestBody EmployeeRequest requestBody, @RequestHeader(value = "Authorization", required = false) String authToken) {
        verifyAuthToken(authToken);
        return employeeService.createEmployee(requestBody);
    }

    /**
     * Helper method to simulate a secure API by validating the web hook's token.
     */
    private void verifyAuthToken(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization token");
        }
        // In a real application, we would validate the token here.
    }
}
