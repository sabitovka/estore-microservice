package ru.isands.test.estore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Date birthDate;
    private boolean gender;
    private Long positionTypeId;
    private Long shopId;
}
