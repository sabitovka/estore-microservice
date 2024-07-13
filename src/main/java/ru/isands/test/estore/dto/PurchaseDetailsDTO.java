package ru.isands.test.estore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PurchaseDetailsDTO {
    private Long id;
    private ElectroItemDTO electro;
    private EmployeeDTO employee;
    private ShopDTO shop;
    private Date purchaseDate;
    private PurchaseTypeDTO type;
}
