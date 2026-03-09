package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImpl;
import com.challenge.api.request.EmployeeRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MockEmployeeService implements EmployeeService {

    private final Map<UUID, Employee> employeeStore = new ConcurrentHashMap<>();

    public MockEmployeeService() {
        // Initialize with some mock data just in case
        UUID id1 = UUID.randomUUID();
        Employee emp1 = new EmployeeImpl(
                id1, "John", "Doe", "John Doe", 80000, 30, "Software Engineer", "john.doe@example.com", Instant.now(), null);
        employeeStore.put(id1, emp1);
        
        UUID id2 = UUID.randomUUID();
        Employee emp2 = new EmployeeImpl(
                id2, "Jane", "Smith", "Jane Smith", 90000, 28, "Product Manager", "jane.smith@example.com", Instant.now(), null);
        employeeStore.put(id2, emp2);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeStore.values());
    }

    @Override
    public Employee getEmployeeByUuid(UUID uuid) {
        Employee employee = employeeStore.get(uuid);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        return employee;
    }

    @Override
    public Employee createEmployee(EmployeeRequest request) {
        if (request == null || request.getFirstName() == null || request.getLastName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employee data");
        }
        
        UUID newUuid = UUID.randomUUID();
        Employee newEmployee = new EmployeeImpl();
        newEmployee.setUuid(newUuid);
        newEmployee.setFirstName(request.getFirstName());
        newEmployee.setLastName(request.getLastName());
        newEmployee.setFullName(request.getFullName() != null ? request.getFullName() : request.getFirstName() + " " + request.getLastName());
        newEmployee.setSalary(request.getSalary());
        newEmployee.setAge(request.getAge());
        newEmployee.setJobTitle(request.getJobTitle());
        newEmployee.setEmail(request.getEmail());
        newEmployee.setContractHireDate(request.getContractHireDate() != null ? request.getContractHireDate() : Instant.now());
        newEmployee.setContractTerminationDate(request.getContractTerminationDate());
        
        employeeStore.put(newUuid, newEmployee);
        return newEmployee;
    }
}
