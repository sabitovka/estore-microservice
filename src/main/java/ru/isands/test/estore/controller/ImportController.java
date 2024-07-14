package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.service.DatabaseTruncationService;
import ru.isands.test.estore.service.ImportService;

@RestController
@Tag(name = "Import", description = "Сервис для импортирования информации")
@RequestMapping("/estore/api/import")
public class ImportController {
    private final ImportService importService;
    private final DatabaseTruncationService databaseTruncationService;

    @Autowired
    public ImportController(ImportService importService, DatabaseTruncationService databaseTruncationService) {
        this.importService = importService;
        this.databaseTruncationService = databaseTruncationService;
    }

    @PostMapping(value = "zip-with-csv", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Импортирует данные из Zip-архива с файлами csv",
            description = "Файлы в архиве должны лежать плоско и соответствовать структуре.\nВнимание! Импорт сотрет все данные в БД!")
    public ResponseEntity<Void> importFromZipWithCsv(@RequestPart(value = "file") MultipartFile zip) {
        databaseTruncationService.truncateAllTables();
        importService.importDataFromZipWithCsv(zip);
        return ResponseEntity.ok().build();
    }
}
