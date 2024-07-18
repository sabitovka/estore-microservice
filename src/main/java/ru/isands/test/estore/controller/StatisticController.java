package ru.isands.test.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.AmountDTO;
import ru.isands.test.estore.dto.EmployeeCompactDTO;
import ru.isands.test.estore.dto.PositionsWithOneEmployeeDTO;
import ru.isands.test.estore.service.StatisticService;

import java.util.List;

@RestController
@Tag(name = "Statistic", description = "Сервис для работы со статисткой")
@RequestMapping("/estore/api/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    public enum BestEmployeesCriteria {
        SALES, REVENUE
    }

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/best-employees-by-position-type")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Информации о лучших сотрудниках в зависимости от занимаемой должности по критериям",
            description = "Критерии:\n" +
                    " - sales - Количество проданных товаров за последний год.\n" +
                    " - amount - Сумма проданных товаров.",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PositionsWithOneEmployeeDTO.class)))
    }
    )
    public List<PositionsWithOneEmployeeDTO> findBestEmployeesByCriteria(
            @RequestParam(name = "criteria") @Parameter(description = "Критерий поиска")
            BestEmployeesCriteria criteria) {
        return statisticService.findBestEmployeesByCriteria(criteria);
    }

    @GetMapping("/best-employee-position-type-sold")
    @Operation(
            summary = "Вывод лучшего сотрудника, продавшего больше всех определенного типа товаров",
            description = "Например, можно найти лучшего младшего продавца-консультанта, продавшего больше всех смарт-часов"
    )
    public ResponseEntity<EmployeeCompactDTO> findBestJuniorWhoSoldMostItems(
            @RequestParam @Parameter(description = "Id должности")
            Long positionTypeId,
            @RequestParam @Parameter(description = "Id типа предмета")
            Long itemId) {
        EmployeeCompactDTO bestEmployee =  statisticService.getTopEmployeeByPositionTypeAndItemsSold(positionTypeId, itemId);
        if (bestEmployee == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bestEmployee);
    }

    @GetMapping("/amount-of-funds-received-through")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Подсчет суммы денежных средств, полученных определенным способом"
    )
    public AmountDTO getAmountOfFundReceivedThrough(
            @RequestParam @Parameter(description = "Id типа оплаты")
            Long purchaseTypeId
    ) {
        return statisticService.getAmountOfFundReceivedThrough(purchaseTypeId);
    }
}
