package com.example.rqchallenge.service;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.EmployeeByIdResponse;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    List<Employee> getEmployeesByNameSearch(String name);

    EmployeeByIdResponse getEmployeeById(String id);

    int getHighestSalaryOfEmployees();

    List<Employee> getTopHighestEarningEmployees(int size);

    EmployeeByIdResponse createEmployee(Employee employee);

    void deleteEmployee(String id);
}
