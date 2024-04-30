package com.example.rqchallenge.controller;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.EmployeeByIdResponse;
import com.example.rqchallenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements IEmployeeController{

    @Autowired
    EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    //localhost:8080/employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(){
        try {
            logger.debug("Request to get all employees");
            List<Employee> employees = employeeService.getAllEmployees();
            logger.info("Retrieved all employees successfully");
            return ResponseEntity.ok(employees);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //localhost:8080/employees/search?name=Garrett Winters
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@RequestParam String name) {
        try {
            logger.debug("Searching employees by name: {}", name);
            List<Employee> employees = employeeService.getEmployeesByNameSearch(name);
            logger.info("Employees search by name '{}' found {} results", name, employees.size());
            return ResponseEntity.ok(employees);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //localhost:8080/employees/10
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        try {
            logger.debug("Request to get employee by ID: {}", id);
            EmployeeByIdResponse employee = employeeService.getEmployeeById(id);
            logger.info("Employee {} with ID: {} retrieved successfully",employee.getData(), id);
            return ResponseEntity.ok(employee.getData());
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //localhost:8080/employees/highest-salary
    @GetMapping("/highest-salary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            logger.debug("Request to get highest salary among all employees");
            int highestSalary = employeeService.getHighestSalaryOfEmployees();
            logger.info("The highest salary {} retrieved successfully", highestSalary);
            return ResponseEntity.ok(highestSalary);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //localhost:8080/employees/top-10-highest-earning
    @GetMapping("/top-10-highest-earning")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            logger.debug("Request to get top 10 highest earning employees");
            List<Employee> employees = employeeService.getTopHighestEarningEmployees(10);
            List<String> employeeNames = employees.stream().map(Employee::getEmployeeName).collect(Collectors.toList());
            logger.info("Top 10 highest earning employees retrieved successfully");
            return ResponseEntity.ok(employeeNames);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

   /*
   localhost:8080/employees/create
   */
    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        try {
            logger.debug("Request to create new employee: {}", employee);
            EmployeeByIdResponse savedEmployee = employeeService.createEmployee(employee);
            logger.info("Employee created successfully: {}", savedEmployee);
            return new ResponseEntity<>(savedEmployee.getData(), HttpStatus.CREATED);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //localhost:8080/employees/1
   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
       try {
           logger.debug("Request to delete employee by ID: {}", id);
           employeeService.deleteEmployee(id);
           logger.info("Employee with ID: {} deleted successfully", id);
           return ResponseEntity.ok("Employee with id "+ id+" got deleted successfully");
       }
       catch (HttpClientErrorException | HttpServerErrorException e) {
           logger.error(e.getMessage());
           return ResponseEntity.status(e.getStatusCode()).body(null);
       }
       catch (Exception e) {
           logger.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       }
    }
}
