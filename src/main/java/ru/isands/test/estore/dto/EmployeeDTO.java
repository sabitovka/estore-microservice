package ru.isands.test.estore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
public class EmployeeDTO {
    @Null
    private Long id;

    @NotBlank(message = "Поле lastName не должно быть пустым")
    @NotNull(message = "Поле lastName не должно отсутствовать")
    private String lastName;

    @NotBlank(message = "Поле firstName не должно быть пустым")
    @NotNull(message = "Поле firstName не должно отсутствовать")
    private String firstName;

    @NotBlank(message = "Поле patronymic не должно быть пустым")
    private String patronymic;

    @Past
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    @NotBlank(message = "Поле name не должно быть пустым")
    @NotNull(message = "Поле name не должно отсутствовать")
    private boolean gender;

    @NotBlank(message = "Поле name не должно быть пустым")
    @NotNull(message = "Поле name не должно отсутствовать")
    private Long positionTypeId;

    @NotBlank(message = "Поле name не должно быть пустым")
    @NotNull(message = "Поле name не должно отсутствовать")
    private Long shopId;
}
