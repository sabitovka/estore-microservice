package ru.isands.test.estore.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ElectroItemDTO {
    Long id;
    @NotNull(message = "Поле name не должно отсутствовать")
    String name;

    @NotNull(message = "Поле price не должно отсутствовать")
    Long price;

    int totalCount;
    boolean archive;

    @NotNull(message = "Поле description не должно отсутствовать")
    @NotBlank(message = "Поле description не должно быть пустым")
    String description;

    @NotNull(message = "Поле electroTypeId не должно отсутствовать")
    Long electroTypeId;
}
