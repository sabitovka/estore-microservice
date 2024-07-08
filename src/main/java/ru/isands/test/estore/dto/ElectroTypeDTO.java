package ru.isands.test.estore.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ElectroTypeDTO {
    private Long id;

    @NotBlank(message = "Поле name не должно быть пустым")
    @NotNull(message = "Поле name не должно отсутствовать")
    private String name;
}
