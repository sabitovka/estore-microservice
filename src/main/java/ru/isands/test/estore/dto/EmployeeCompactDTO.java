package ru.isands.test.estore.dto;

import lombok.Data;

@Data
public class EmployeeCompactDTO {
    Long id;
    String firstName;
    String lastName;
    String patronymic;
    String positionName;
    Long itemsSold;
    String itemName;
}
