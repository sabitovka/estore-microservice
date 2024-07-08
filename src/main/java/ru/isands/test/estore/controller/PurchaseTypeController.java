package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.PurchaseTypeDTO;
import ru.isands.test.estore.service.PurchaseTypeService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/estore/api/purchase-types")
@Tag(name = "PurchaseType", description = "Сервис для управления типами оплаты")
public class PurchaseTypeController {
    private final PurchaseTypeService purchaseTypeService;

    @Autowired
    public PurchaseTypeController(PurchaseTypeService purchaseTypeService) {
        this.purchaseTypeService = purchaseTypeService;
    }

    @GetMapping
    @Operation(summary = "Получить все типы оплаты")
    @ApiResponse(responseCode = "200", description = "Список типов покупок", content = @Content(schema = @Schema(implementation = PurchaseTypeDTO.class)))
    public ResponseEntity<List<PurchaseTypeDTO>> getAllPurchaseTypes(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortType) {
        log.info("[PurchaseType] Запрос всех способ оплаты c пагинацией - page: {}, size: {}, sortField: {}, sortType: {}", page, size, sortField, sortType);
        List<PurchaseTypeDTO> purchaseTypes = purchaseTypeService.getAllPurchaseTypes(page, size, sortField, sortType);
        return ResponseEntity.ok(purchaseTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип оплаты по ID")
    @ApiResponse(responseCode = "200", description = "Тип оплаты найден", content = @Content(schema = @Schema(implementation = PurchaseTypeDTO.class)))
    @ApiResponse(responseCode = "404", description = "Тип оплаты не найден")
    public ResponseEntity<PurchaseTypeDTO> getPurchaseTypeById(@PathVariable Long id) {
        PurchaseTypeDTO purchaseTypeDTO = purchaseTypeService.getPurchaseTypeById(id);
        return ResponseEntity.ok(purchaseTypeDTO);
    }

    @PostMapping
    @Operation(summary = "Создать новый тип оплаты")
    @ApiResponse(responseCode = "201", description = "Тип оплаты создан", content = @Content(schema = @Schema(implementation = PurchaseTypeDTO.class)))
    public ResponseEntity<PurchaseTypeDTO> createPurchaseType(@Valid @RequestBody PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseTypeDTO createdPurchaseType = purchaseTypeService.createPurchaseType(purchaseTypeDTO);
        return ResponseEntity.status(201).body(createdPurchaseType);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить тип оплаты", description = "Обновляет существующий тип оплаты.")
    @ApiResponse(responseCode = "200", description = "Тип оплаты обновлен", content = @Content(schema = @Schema(implementation = PurchaseTypeDTO.class)))
    @ApiResponse(responseCode = "404", description = "Тип оплаты не найден")
    public ResponseEntity<PurchaseTypeDTO> updatePurchaseType(@PathVariable Long id, @Valid @RequestBody PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseTypeDTO updatedPurchaseType = purchaseTypeService.updatePurchaseType(id, purchaseTypeDTO);
        return ResponseEntity.ok(updatedPurchaseType);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тип оплаты", description = "Удаляет тип оплаты на основе указанного идентификатора.")
    @ApiResponse(responseCode = "204", description = "Тип оплаты удален")
    @ApiResponse(responseCode = "404", description = "Тип оплаты не найден")
    public ResponseEntity<Void> deletePurchaseType(@PathVariable Long id) {
        purchaseTypeService.deletePurchaseType(id);
        return ResponseEntity.noContent().build();
    }
}
