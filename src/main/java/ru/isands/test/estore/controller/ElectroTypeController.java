package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.ElectroTypeDTO;
import ru.isands.test.estore.service.ElectroTypeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/estore/api/electro-types")
@Tag(name = "ElectroType", description = "Сервис для управления типами электроники")
public class ElectroTypeController {
    private final ElectroTypeService electroTypeService;

    @Autowired
    public ElectroTypeController(ElectroTypeService electroTypeService) {
        this.electroTypeService = electroTypeService;
    }

    @GetMapping
    @Operation(summary = "Получить все типы электроники")
    @ApiResponse(responseCode = "200", description = "Список типов электроники", content = @Content(schema = @Schema(implementation = ElectroTypeDTO.class)))
    public ResponseEntity<List<ElectroTypeDTO>> getAllElectroTypes(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortType
    ) {
        List<ElectroTypeDTO> electroTypes = electroTypeService.getAllElectroTypes(page, size, sortField, sortType);
        return ResponseEntity.ok(electroTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип электроники по ID")
    @ApiResponse(responseCode = "200", description = "Тип электроники найден", content = @Content(schema = @Schema(implementation = ElectroTypeDTO.class)))
    @ApiResponse(responseCode = "404", description = "Тип электроники не найден")
    public ResponseEntity<ElectroTypeDTO> getElectroTypeById(@PathVariable Long id) {
        ElectroTypeDTO electroTypeDTO = electroTypeService.getElectroTypeById(id);
        return ResponseEntity.ok(electroTypeDTO);
    }

    @PostMapping
    @Operation(summary = "Создать новый тип электроники")
    @ApiResponse(responseCode = "201", description = "Тип электроники создан", content = @Content(schema = @Schema(implementation = ElectroTypeDTO.class)))
    public ResponseEntity<ElectroTypeDTO> createElectroType(@Valid @RequestBody ElectroTypeDTO electroTypeDTO) {
        ElectroTypeDTO createdElectroType = electroTypeService.createElectroType(electroTypeDTO);
        return ResponseEntity.status(201).body(createdElectroType);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить тип электроники")
    @ApiResponse(responseCode = "200", description = "Тип электроники обновлен", content = @Content(schema = @Schema(implementation = ElectroTypeDTO.class)))
    @ApiResponse(responseCode = "404", description = "Тип электроники не найден")
    public ResponseEntity<ElectroTypeDTO> updateElectroType(@PathVariable Long id, @Valid @RequestBody ElectroTypeDTO electroTypeDTO) {
        ElectroTypeDTO updatedElectroType = electroTypeService.updateElectroType(id, electroTypeDTO);
        return ResponseEntity.ok(updatedElectroType);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тип электроники")
    @ApiResponse(responseCode = "204", description = "Тип электроники удален")
    @ApiResponse(responseCode = "404", description = "Тип электроники не найден")
    public ResponseEntity<Void> deleteElectroType(@PathVariable Long id) {
        electroTypeService.deleteElectroType(id);
        return ResponseEntity.noContent().build();
    }

}
