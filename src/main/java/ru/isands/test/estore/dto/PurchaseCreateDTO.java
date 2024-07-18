package ru.isands.test.estore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PurchaseCreateDTO {
    Long electroItemId;
    Long employeeId;
    Long shopId;
    Long purchaseTypeId;
}
