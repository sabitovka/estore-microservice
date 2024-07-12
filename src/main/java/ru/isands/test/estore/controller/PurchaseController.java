package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Purchase", description = "Сервис для работы с покупками")
@RequestMapping("/estore/api/purchases")
public class PurchaseController {
}
