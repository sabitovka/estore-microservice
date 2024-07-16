package ru.isands.test.estore.dto;

import lombok.Data;

@Data
public class PositionsWithOneEmployeeDTO {
    String positionName;
    Long employeeId;
    String firstName;
    String lastName;
    String patronymic;
    Long numberItemsSold;
    Long amountItemsSold;
}
