package com.example.rqchallenge.entity;

import lombok.*;

@Setter
@Getter
public class EmployeeByIdResponse {

    private String status;
    private Employee data;
    private String message;
}
