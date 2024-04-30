package com.example.rqchallenge.service;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.EmployeeByIdResponse;
import com.example.rqchallenge.entity.EmployeeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final String BASE_URL = "https://dummy.restapiexample.com/api/v1";

    private final RestTemplate restTemplate;

    public EmployeeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Employee> getAllEmployees() {
            String url = BASE_URL + "/employees";
            EmployeeResponse response = restTemplate.getForObject(url, EmployeeResponse.class);
            return response != null ? response.getData() : new ArrayList<>();
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String name) {

        return getAllEmployees().stream()
                .filter(employee -> {
                    String employeeName = employee.getEmployeeName();
                    // Check if employeeName is not null and equals the specified name
                    return employeeName != null && employeeName.equals(name);
                })
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeByIdResponse getEmployeeById(String id) {
        String url = BASE_URL + "/employee/" + id;
        return restTemplate.getForObject(url, EmployeeByIdResponse.class);
    }

    @Override
    public int getHighestSalaryOfEmployees() {
        List<Employee> employeeList = getTopHighestEarningEmployees(1);
        if(employeeList.isEmpty())
            return 0;
        return employeeList.get(0).getEmployeeSalary();
    }

    public List<Employee> getTopHighestEarningEmployees(int size) {
        List<Employee> employeeList = getAllEmployees();
        employeeList.sort(Comparator.comparingInt(Employee::getEmployeeSalary).reversed());
        return employeeList.subList(0, Math.min(size, employeeList.size()));
    }

   @Override
    public EmployeeByIdResponse createEmployee(Employee employee) {
        String url = BASE_URL + "/create";
        return restTemplate.postForObject(url, employee, EmployeeByIdResponse.class);
    }

    @Override
    public void deleteEmployee(String id) {
        String url = BASE_URL + "/delete/" + id;
        restTemplate.delete(url);
    }
}
