package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.PurchaseCreateDTO;
import ru.isands.test.estore.dto.PurchaseDetailsDTO;
import ru.isands.test.estore.service.PurchaseService;

import java.util.List;

@RestController
@Tag(name = "Purchase", description = "Сервис для работы с покупками")
@RequestMapping("/estore/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDetailsDTO>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortType
    ) {
        List<PurchaseDetailsDTO> purchases = purchaseService.findAllPageable(page, size, sortField, sortType);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить покупку по ее ID")
    public ResponseEntity<PurchaseDetailsDTO> findById(@PathVariable Long id) {
        PurchaseDetailsDTO purchase = purchaseService.findById(id);
        return ResponseEntity.ok(purchase);
    }

    @PostMapping
    @Operation(summary = "Создать новую покупку", description = "Новая покупка создается с датой выполнения запроса")
    public ResponseEntity<PurchaseDetailsDTO> createPurchase(@RequestBody PurchaseCreateDTO purchase) {
        PurchaseDetailsDTO detailedPurchase = purchaseService.createPurchase(purchase);
        return ResponseEntity.ok(detailedPurchase);
    }
}
