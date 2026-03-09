package com.challenge.api.request;

import java.time.Instant;

public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String fullName;
    private Integer salary;
    private Integer age;
    private String jobTitle;
    private String email;
    private Instant contractHireDate;
    private Instant contractTerminationDate;

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return fullName; }
    public Integer getSalary() { return salary; }
    public Integer getAge() { return age; }
    public String getJobTitle() { return jobTitle; }
    public String getEmail() { return email; }
    public Instant getContractHireDate() { return contractHireDate; }
    public Instant getContractTerminationDate() { return contractTerminationDate; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setSalary(Integer salary) { this.salary = salary; }
    public void setAge(Integer age) { this.age = age; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setEmail(String email) { this.email = email; }
    public void setContractHireDate(Instant contractHireDate) { this.contractHireDate = contractHireDate; }
    public void setContractTerminationDate(Instant contractTerminationDate) { this.contractTerminationDate = contractTerminationDate; }
}
