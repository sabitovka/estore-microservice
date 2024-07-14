package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.AddElectroItemDTO;
import ru.isands.test.estore.dto.ElectroShopDTO;
import ru.isands.test.estore.dto.ShopDTO;
import ru.isands.test.estore.exception.ErrorResponse;
import ru.isands.test.estore.service.ShopService;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "Shop", description = "Сервис для выполнения операций над справочником \"Магазины\"")
@RequestMapping("/estore/api/shops")
public class ShopController {
    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить магазин по ID", responses = {
            @ApiResponse(description = "Магазин найден", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ShopDTO.class), mediaType = "application/json")
            ),
            @ApiResponse(description = "Магазин не найден", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            )
    })
    public ResponseEntity<ShopDTO> getShopById(@PathVariable Long id) {
        log.info("[Shop] Запрос магазина с id={}", id);
        ShopDTO shopDTO = shopService.getShopById(id);
        return ResponseEntity.ok(shopDTO);
    }

    @GetMapping
    @Operation(summary = "Получить все магазины", responses = {
            @ApiResponse(description = "Список магазинов", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShopDTO.class))))
    })
    public ResponseEntity<List<ShopDTO>> getAllShops(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortType
    ) {
        log.info("[Shop] Запрос всех магазинов c пагинацией - page: {}, size: {}, sortField: {}, sortType: {}", page, size, sortField, sortType);
        List<ShopDTO> shops = shopService.getAllShops(page, size, sortField, sortType);
        return ResponseEntity.ok(shops);
    }

    @PostMapping
    @Operation(summary = "Создать новый магазин", responses = {
            @ApiResponse(description = "Магазин создан", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ShopDTO.class))),
            @ApiResponse(description = "Неверный запрос", responseCode = "400")
    })
    public ResponseEntity<ShopDTO> createShop(@RequestBody ShopDTO shopDTO) {
        log.info("[Shop] Запрос на создание: {}", shopDTO);
        ShopDTO createdShop = shopService.createShop(shopDTO);
        return ResponseEntity.ok(createdShop);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить информацию о магазине", responses = {
            @ApiResponse(description = "Магазин обновлен", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ShopDTO.class))),
            @ApiResponse(description = "Магазин не найден", responseCode = "404"),
            @ApiResponse(description = "Неверный запрос", responseCode = "400")
    })
    public ResponseEntity<ShopDTO> updateShop(@PathVariable Long id, @RequestBody ShopDTO shopDTO) {
        log.info("[Shop] Запрос на обновление с id={}: {}", id, shopDTO);
        ShopDTO updatedShop = shopService.updateShop(id, shopDTO);
        return ResponseEntity.ok(updatedShop);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить магазин", responses = {
            @ApiResponse(description = "Магазин удален", responseCode = "204"),
            @ApiResponse(description = "Магазин не найден", responseCode = "404")
    })
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        log.info("[Shop] Запрос на удаление с id={}.", id);
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{shopId}/electro-items")
    @Operation(summary = "Добавить список товаров в магазин", description = "Товары будут добавлены к существующим в этом магазине")
    public ResponseEntity<Void> addElectroItemsToShop(@PathVariable Long shopId, @RequestBody List<AddElectroItemDTO> electroItems) {
        shopService.addElectroItemsToShop(shopId, electroItems);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{shopId}/electro-items")
    @Operation(summary = "Получить все товары в магазине")
    public ResponseEntity<List<ElectroShopDTO>> findAllShopElectroItems(@PathVariable Long shopId) {
        List<ElectroShopDTO> electroShopDTOS = shopService.findAllShopElectroItems(shopId);
        return ResponseEntity.ok(electroShopDTOS);
    }
}
