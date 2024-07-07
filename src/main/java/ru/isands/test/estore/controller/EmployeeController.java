package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.isands.test.estore.dto.EmployeeDTO;
import ru.isands.test.estore.exception.ErrorResponse;
import ru.isands.test.estore.model.Employee;
import ru.isands.test.estore.util.PageSettings;
import ru.isands.test.estore.service.EmployeeService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Tag(name = "Employee", description = "Сервис для выполнения операций над сотрудниками магазина")
@RequestMapping("/estore/api/employee")
public class EmployeeController {

	private final EmployeeService employeeService;
	private final ModelMapper modelMapper;

	@Autowired
	public EmployeeController(EmployeeService employeeService, ModelMapper modelMapper) {
		this.employeeService = employeeService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	@Operation(summary = "Получить всех сотрудников", responses = {
			@ApiResponse(responseCode = "200", description = "Список сотрудников")
	})
	public ResponseEntity<List<EmployeeDTO>> getEmployees(PageSettings pageSettings) {
		log.info("Запрошены все сотрудники с pageSettings: " + pageSettings.toString());
		List<Employee> employees = employeeService.getEmployeeList(pageSettings);
		return ResponseEntity.ok(
				employees.stream()
						.map(employee -> modelMapper.map(employee, EmployeeDTO.class))
						.collect(Collectors.toList())
			);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Создать нового сотрудника", responses = {
			@ApiResponse(responseCode = "201", description = "Сотрудник создан", content = {
					@Content(mediaType = "application/json")
			}),
			@ApiResponse(responseCode = "400", description = "Некорректный запрос. Сущность не валидна", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
			})
	})
	public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) {
		log.info("Принят запрос на создание нового сотрудника: " + employeeDTO.toString());
		Employee employee = modelMapper.map(employeeDTO, Employee.class);
		Employee employeeCreated = employeeService.createEmployee(employee);
		return modelMapper.map(employeeCreated, EmployeeDTO.class);
	}
}
