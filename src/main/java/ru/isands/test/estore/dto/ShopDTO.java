package ru.isands.test.estore.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class ShopDTO {
    @Null
    private Long id;

    @NotBlank(message = "Поле name не должно быть пустым")
    @NotNull(message = "Поле name не должно отсутствовать")
    private String name;

    @NotBlank(message = "Поле address не должно быть пустым")
    @NotNull(message = "Поле address не должно отсутствовать")
    private String address;
}
