package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.PositionTypeDTO;
import ru.isands.test.estore.service.PositionTypeService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/position-types")
@Tag(name = "PositionType", description = "Сервис для выполнения операций над типами должностей")
public class PositionTypeController {
    private final PositionTypeService positionTypeService;

    @Autowired
    public PositionTypeController(PositionTypeService positionTypeService) {
        this.positionTypeService = positionTypeService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип должности по ID", responses = {
            @ApiResponse(description = "Тип должности найден", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PositionTypeDTO.class))),
            @ApiResponse(description = "Тип должности не найден", responseCode = "404")
    })
    public ResponseEntity<PositionTypeDTO> getPositionTypeById(@PathVariable Long id) {
        PositionTypeDTO positionTypeDTO = positionTypeService.getPositionTypeById(id);
        return ResponseEntity.ok(positionTypeDTO);
    }

    @GetMapping
    @Operation(summary = "Получить все типы должностей", responses = {
            @ApiResponse(description = "Список типов должностей", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PositionTypeDTO.class))))
    })
    public ResponseEntity<List<PositionTypeDTO>> getAllPositionTypes(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortType
    ) {
        log.info("[PositionType] Запрос всех должностей c пагинацией - page: {}, size: {}, sortField: {}, sortType: {}", page, size, sortField, sortType);
        List<PositionTypeDTO> positionTypes = positionTypeService.getAllPositionTypes(page, size, sortField, sortType);
        return ResponseEntity.ok(positionTypes);
    }

    @PostMapping
    @Operation(summary = "Создать новый тип должности", responses = {
            @ApiResponse(description = "Тип должности создан", responseCode = "201",
            content = @Content(schema = @Schema(implementation = PositionTypeDTO.class))),
        @ApiResponse(description = "Неверный запрос", responseCode = "400")
    })
    public ResponseEntity<PositionTypeDTO> createPositionType(@RequestBody PositionTypeDTO positionTypeDTO) {
        PositionTypeDTO createdPositionType = positionTypeService.createPositionType(positionTypeDTO);
        return ResponseEntity.status(201).body(createdPositionType);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить информацию о типе должности", responses = {
            @ApiResponse(description = "Тип должности обновлен", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PositionTypeDTO.class))),
            @ApiResponse(description = "Тип должности не найден", responseCode = "404"),
            @ApiResponse(description = "Неверный запрос", responseCode = "400")
    })
    public ResponseEntity<PositionTypeDTO> updatePositionType(@PathVariable Long id, @RequestBody PositionTypeDTO positionTypeDTO) {
        PositionTypeDTO updatedPositionType = positionTypeService.updatePositionType(id, positionTypeDTO);
        return ResponseEntity.ok(updatedPositionType);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тип должности", responses = {
            @ApiResponse(description = "Тип должности удален", responseCode = "204"),
            @ApiResponse(description = "Тип должности не найден", responseCode = "404")
    })
    public ResponseEntity<Void> deletePositionType(@PathVariable Long id) {
        positionTypeService.deletePositionType(id);
        return ResponseEntity.noContent().build();
    }
}
