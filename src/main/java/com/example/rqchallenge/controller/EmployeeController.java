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

@RestController
@RequestMapping("/employees")
public class EmployeeController{

    @Autowired
    EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    //localhost:8080/employees
    @GetMapping
    public ResponseEntity<Object> getAllEmployees() {
        try {
            logger.debug("Request to get all employees");
            List<Employee> employees = employeeService.getAllEmployees();
            logger.info("Retrieved all employees successfully");
            return ResponseEntity.ok(employees);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return handleException(e, e.getMessage(), e.getStatusCode());
        }
        catch (Exception e) {
            return handleException(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //localhost:8080/employees/search?name=Garrett Winters
    @GetMapping("/search")
    public ResponseEntity<Object> getEmployeesByNameSearch(@RequestParam String name) {
        try {
            logger.debug("Searching employees by name: {}", name);
            List<Employee> employees = employeeService.getEmployeesByNameSearch(name);
            logger.info("Employees search by name '{}' found {} results", name, employees.size());
            return ResponseEntity.ok(employees);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return handleException(e, e.getMessage(), e.getStatusCode());
        }
        catch (Exception e) {
            return handleException(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //localhost:8080/employees/10
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String id) {
        try {
            logger.debug("Request to get employee by ID: {}", id);
            EmployeeByIdResponse employee = employeeService.getEmployeeById(id);
            logger.info("Employee {} with ID: {} retrieved successfully",employee.getData(), id);
            return ResponseEntity.ok(employee);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return handleException(e, e.getMessage(), e.getStatusCode());
        }
        catch (Exception e) {
            return handleException(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //localhost:8080/employees/highest-salary
    @GetMapping("/highest-salary")
    public ResponseEntity<Object> getHighestSalaryOfEmployees() {
        try {
            logger.debug("Request to get highest salary among all employees");
            int highestSalary = employeeService.getHighestSalaryOfEmployees();
            logger.info("The highest salary {} retrieved successfully", highestSalary);
            return ResponseEntity.ok(highestSalary);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return handleException(e, e.getMessage(), e.getStatusCode());
        }
        catch (Exception e) {
            return handleException(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //localhost:8080/employees/top-10-highest-earning
    @GetMapping("/top-10-highest-earning")
    public ResponseEntity<Object> getTop10HighestEarningEmployeeNames() {
        try {
            logger.debug("Request to get top 10 highest earning employees");
            List<Employee> employees = employeeService.getTopHighestEarningEmployees(10);
            logger.info("Top 10 highest earning employees retrieved successfully");
            return ResponseEntity.ok(employees);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return handleException(e, e.getMessage(), e.getStatusCode());
        }
        catch (Exception e) {
            return handleException(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   /*
   localhost:8080/employees/create
   */
    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employee) {
        try {
            logger.debug("Request to create new employee: {}", employee);
            EmployeeByIdResponse savedEmployee = employeeService.createEmployee(employee);
            logger.info("Employee created successfully: {}", savedEmployee);
            return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return handleException(e, e.getMessage(), e.getStatusCode());
        }
        catch (Exception e) {
            return handleException(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //localhost:8080/employees/1
   @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String id) {
       try {
           logger.debug("Request to delete employee by ID: {}", id);
           employeeService.deleteEmployee(id);
           logger.info("Employee with ID: {} deleted successfully", id);
           return ResponseEntity.ok("Employee with id "+ id+" got deleted successfully");
       }
       catch (HttpClientErrorException | HttpServerErrorException e) {
           return handleException(e, e.getMessage(), e.getStatusCode());
       }
       catch (Exception e) {
           return handleException(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    private ResponseEntity<Object> handleException(Exception e, String errorMessage, HttpStatus status) {

        logger.error(errorMessage, e);
        return ResponseEntity.status(status).body(errorMessage);
    }
}
