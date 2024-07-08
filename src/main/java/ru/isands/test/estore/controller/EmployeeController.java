package ru.isands.test.estore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.isands.test.estore.dto.ElectroTypeDTO;
import ru.isands.test.estore.dto.EmployeeDTO;
import ru.isands.test.estore.dto.SimpleIdDTO;
import ru.isands.test.estore.model.Employee;
import ru.isands.test.estore.service.EmployeeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@Tag(name = "Employee", description = "Сервис для выполнения операций над сотрудниками магазина")
@RequestMapping("/estore/api/employee")
public class EmployeeController {
	private final EmployeeService employeeService;

	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Получить сотрудника по id", responses = {
			@ApiResponse(responseCode = "200", description = "Сотрудник")
	})
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
		log.info("Принят запрос на получение сотрудника по id" + id);
		EmployeeDTO employee = employeeService.getEmployeeById(id);
		return ResponseEntity.ok(employee);
	}

	@GetMapping
	@Operation(summary = "Получить всех сотрудников", responses = {
			@ApiResponse(responseCode = "200", description = "Список сотрудников")
	})
	public ResponseEntity<List<EmployeeDTO>> getEmployees(
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false) String sortField,
			@RequestParam(required = false, defaultValue = "asc") String sortType
	) {
		log.info("[Employee] Запрос всех сотрудников c пагинацией - page: {}, size: {}, sortField: {}, sortType: {}", page, size, sortField, sortType);
		return ResponseEntity.ok(employeeService.getAllEmployees(page, size, sortField, sortType));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Создать нового сотрудника", responses = {
			@ApiResponse(responseCode = "201", description = "Сотрудник создан"),
			@ApiResponse(responseCode = "400", description = "Некорректный запрос. Сущность не валидна")
	})
	public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
		log.info("Принят запрос на создание нового сотрудника: " + employeeDTO.toString());
		EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Обновить сотрудника", responses = {
			@ApiResponse(responseCode = "200", description = "Сотрудник обновлен"),
			@ApiResponse(responseCode = "404", description = "Сотрудник не найден")
	})
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
		log.info("Принят запрос на обновление сотрудника: " + employeeDTO.toString());
		EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Удалить сотрудника", responses = {
			@ApiResponse(responseCode = "200", description = "Сотрудник удален"),
			@ApiResponse(responseCode = "404", description = "Сотрудник не найден")
	})
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		log.info("Принят запрос на удаление сотрудника с id = {}", id);
		employeeService.deleteEmployee(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/electro-types")
	@Operation(summary = "Получить все назначения на электронику сотрудника")
	public ResponseEntity<Set<ElectroTypeDTO>> getEmployeeElectroTypes(@PathVariable Long id) {
		log.info("Запрос назначений на электронику сотрудника c id = {}", id);
		Set<ElectroTypeDTO> electroTypes = employeeService.getEmployeeElectroTypes(id);
		return ResponseEntity.ok(electroTypes);
	}

	@PostMapping("/{id}/electro-types")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Добавить назначение сотрудника к типу электроники")
	public ResponseEntity<Void> addElectroTypeToEmployee(
			@PathVariable Long id,
			@RequestBody SimpleIdDTO electroTypeIdDTO) {
		log.info("Запрос на добавление типа электроники к пользователю с id = {}", id);
		employeeService.addElectroTypeToEmployee(id, electroTypeIdDTO.getId());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}/electro-types")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Удалить назначение типа электроники у сотрудника")
	public ResponseEntity<Void> removeElectroTypeFromEmployee(
			@PathVariable Long id,
			@RequestBody SimpleIdDTO electroTypeIdDTO) {
		employeeService.removeElectroTypeFromEmployee(id, electroTypeIdDTO.getId());
		return ResponseEntity.ok().build();
	}
}
