package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.ElectroItemDTO;
import ru.isands.test.estore.service.ElectroItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "ElectroItem", description = "Сервис для выполнения операций над электро-товарами")
@RequestMapping("/estore/api/electro-items")
public class ElectroItemController {
    private final ElectroItemService electroItemService;

    @Autowired
    public ElectroItemController(ElectroItemService electroItemService) {
        this.electroItemService = electroItemService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по id", responses = {
            @ApiResponse(responseCode = "200", description = "Товар найден"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    public ResponseEntity<ElectroItemDTO> getElectroItemById(@PathVariable Long id) {
        ElectroItemDTO electroItemDTO = electroItemService.getElectroItemById(id);
        return ResponseEntity.ok(electroItemDTO);
    }

    @GetMapping
    @Operation(summary = "Получить список товаров", responses = {
            @ApiResponse(responseCode = "200", description = "Получен список товаров"),
            @ApiResponse(responseCode = "400", description = "Неправильный параметр запроса")
    })
    public ResponseEntity<List<ElectroItemDTO>> getAllElectroItems(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortType
    ) {
        return ResponseEntity.ok(electroItemService.getAllElectroItems(page, size, sortField, sortType));
    }

    @PostMapping
    @Operation(summary = "Создать новый товар", responses = {
            @ApiResponse(responseCode = "201", description = "Товар успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверное тело запроса")
    })
    public ResponseEntity<ElectroItemDTO> createElectroItem(@Valid @RequestBody ElectroItemDTO electroItemDTO) {
        ElectroItemDTO createdElectroItem = electroItemService.createElectroItem(electroItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdElectroItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить товар", responses = {
            @ApiResponse(responseCode = "200", description = "Товар обновлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в теле запроса"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    public ResponseEntity<ElectroItemDTO> updateElectroItem(@PathVariable long id, @Valid @RequestBody ElectroItemDTO electroItemDTO) {
        ElectroItemDTO updatedElectroItem = electroItemService.updateElectroItem(id, electroItemDTO);
        return ResponseEntity.ok(updatedElectroItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар", responses = {
            @ApiResponse(responseCode = "200", description = "Товар удален"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    public ResponseEntity<Void> deleteElectroItem(@PathVariable Long id) {
        electroItemService.deleteElectroItem(id);
        return ResponseEntity.noContent().build();
    }
}
