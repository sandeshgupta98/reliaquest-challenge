package com.example.rqchallenge.controller;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.EmployeeByIdResponse;
import com.example.rqchallenge.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private String age;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        age = "30";
    }

    @Test
     void testGetAllEmployees_Success() {
        List<Employee> mockEmployees = Arrays.asList(
                new Employee("1", "John Doe",1000,age,""),
                new Employee("2","Jane Smith", 1200,age,"")
        );
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        ResponseEntity<Object> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmployees, response.getBody());
    }

   @Test
    void testGetEmployeesByNameSearch_Success() {
        String name = "John Doe";
        List<Employee> mockEmployees = Arrays.asList(
                new Employee("1",name, 1000,age,""),
                new Employee("2",name, 1500,age,"")
        );
        when(employeeService.getEmployeesByNameSearch(name)).thenReturn(mockEmployees);

        ResponseEntity<Object> response = employeeController.getEmployeesByNameSearch(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmployees, response.getBody());
    }

    @Test
    void testGetEmployeeById_Success() {
        String id = "1";
        Employee employee = new Employee("1", "John", 1100, age, "");
        EmployeeByIdResponse mockResponse = new EmployeeByIdResponse();
        mockResponse.setData(employee);
        when(employeeService.getEmployeeById(id)).thenReturn(mockResponse);

        ResponseEntity<Object> response = employeeController.getEmployeeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetHighestSalaryOfEmployees_Success() {
        int highestSalary = 2000;
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(highestSalary);

        ResponseEntity<Object> response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(highestSalary, response.getBody());
    }


    @Test
    void testGetTop10HighestEarningEmployeeNames_Success() {
        List<Employee> mockEmployees = Arrays.asList(
                new Employee("1", "John Doe",1000,age,""),
                new Employee("2","Jane Smith", 1200,age,"")
        );
        when(employeeService.getTopHighestEarningEmployees(10)).thenReturn(mockEmployees);

        ResponseEntity<Object> response = employeeController.getTop10HighestEarningEmployeeNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmployees, response.getBody());
    }


    @Test
    void testCreateEmployee_Success() {
        EmployeeByIdResponse employeeByIdResponse = new EmployeeByIdResponse();
        Employee newEmployee = new Employee("1", "John Doe",1000,age,"");
        employeeByIdResponse.setData(newEmployee);
        when(employeeService.createEmployee(newEmployee)).thenReturn(employeeByIdResponse);

        ResponseEntity<Object> response = employeeController.createEmployee(newEmployee);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("SUCCESS", response.getBody());
    }

    @Test
    void testDeleteEmployee_Success() {
        String id = "1";
        ResponseEntity<Object> response = employeeController.deleteEmployee(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee with id "+ id+" got deleted successfully", response.getBody());
    }

    @Test
     void testDeleteEmployee_Exception(){
        String id = "2";
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee Not Found")).when(employeeService).deleteEmployee(id);
        ResponseEntity<Object> response = employeeController.deleteEmployee(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllEmployees_Exception() {
        Mockito.doThrow(new RuntimeException("Internal server error")).when(employeeService).getAllEmployees();
        ResponseEntity<Object> response = employeeController.getAllEmployees();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetEmployeesByNameSearch_Exception() {
        String name = "John Doe";
        Mockito.doThrow(new RuntimeException("Internal server error")).when(employeeService).getEmployeesByNameSearch(name);
        ResponseEntity<Object> response = employeeController.getEmployeesByNameSearch(name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetEmployeeById_Exception() {
        String id = "1";
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found")).when(employeeService).getEmployeeById(id);
        ResponseEntity<Object> response = employeeController.getEmployeeById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetHighestSalaryOfEmployees_Exception() {
        Mockito.doThrow(new RuntimeException("Internal server error")).when(employeeService).getHighestSalaryOfEmployees();
        ResponseEntity<Object> response = employeeController.getHighestSalaryOfEmployees();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetTop10HighestEarningEmployeeNames_Exception() {
        Mockito.doThrow(new RuntimeException("Internal server error")).when(employeeService).getTopHighestEarningEmployees(10);
        ResponseEntity<Object> response = employeeController.getTop10HighestEarningEmployeeNames();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testCreateEmployee_Exception() {
        Employee employee = new Employee();
        Mockito.doThrow(new RuntimeException("Internal server error")).when(employeeService).createEmployee(employee);
        ResponseEntity<Object> response = employeeController.createEmployee(employee);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
