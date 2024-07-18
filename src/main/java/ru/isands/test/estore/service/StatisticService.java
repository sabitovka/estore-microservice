package ru.isands.test.estore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.controller.StatisticController;
import ru.isands.test.estore.dto.AmountDTO;
import ru.isands.test.estore.dto.EmployeeCompactDTO;
import ru.isands.test.estore.dto.PositionsWithOneEmployeeDTO;
import ru.isands.test.estore.exception.IllegalParamException;
import ru.isands.test.estore.repository.StatisticRepository;

import java.util.*;

import static ru.isands.test.estore.controller.StatisticController.BestEmployeesCriteria.REVENUE;

@Service
public class StatisticService {

    private final StatisticRepository statisticRepository;

    @Autowired
    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    public List<PositionsWithOneEmployeeDTO> findBestEmployeesByCriteria(StatisticController.BestEmployeesCriteria criteria) {
        boolean isPresent = Arrays.stream(StatisticController.BestEmployeesCriteria.values()).anyMatch(element -> element == criteria);
        if (!isPresent) {
            throw new IllegalParamException("Не удалось выполнить запрос", List.of("Неизвестный критерий " + criteria));
        }
        List<Object[]> obj = statisticRepository.getTopEmployeesBySalesAndRevenue();

        Map<String, Object[]> employeesPurchasesMap = new HashMap<>();
        int colIndex = criteria == REVENUE ? 5 : 6;

        for (Object[] result : obj) {
            String position = (String) result[0];
            Object[] compareObj = employeesPurchasesMap.get(position);
            if (compareObj == null) {
                employeesPurchasesMap.put(position, result);
                continue;
            }

            if ((Long) result[colIndex] > (Long) compareObj[colIndex]) {
                employeesPurchasesMap.put(position, result);
            }
        }

        List<PositionsWithOneEmployeeDTO> bestEmployees = new ArrayList<>();
        for (Object[] positionWithEmployee : employeesPurchasesMap.values()) {
            PositionsWithOneEmployeeDTO employeeDTO = new PositionsWithOneEmployeeDTO();
            employeeDTO.setPositionName((String) positionWithEmployee[0]);
            employeeDTO.setEmployeeId((Long) positionWithEmployee[1]);
            employeeDTO.setFirstName((String) positionWithEmployee[2]);
            employeeDTO.setLastName((String) positionWithEmployee[3]);
            employeeDTO.setPatronymic((String) positionWithEmployee[4]);
            employeeDTO.setNumberItemsSold((Long) positionWithEmployee[5]);
            employeeDTO.setAmountItemsSold((Long) positionWithEmployee[6]);
            bestEmployees.add(employeeDTO);
        }

        return bestEmployees;
    }

    public EmployeeCompactDTO getTopEmployeeByPositionTypeAndItemsSold(Long positionTypeId, Long itemId) {
        List<Object[]> obj = statisticRepository.getTopEmployeeBySalesOfItemId(positionTypeId, itemId);
        if (obj.size() == 0) {
            return null;
        }
        Object[] result = obj.get(0);
        EmployeeCompactDTO employeeCompactDTO = new EmployeeCompactDTO();
        employeeCompactDTO.setId((Long) result[0]);
        employeeCompactDTO.setFirstName((String) result[1]);
        employeeCompactDTO.setLastName((String) result[2]);
        employeeCompactDTO.setPatronymic((String) result[3]);
        employeeCompactDTO.setPositionName((String) result[4]);
        employeeCompactDTO.setItemName((String) result[5]);
        employeeCompactDTO.setItemsSold((Long) result[6]);
        return employeeCompactDTO;
    }

    public AmountDTO getAmountOfFundReceivedThrough(Long purchaseTypeId) {
        Object[] obj = statisticRepository.calculateAmountOfFundsReceivedThrough(purchaseTypeId);
        AmountDTO amountDTO = new AmountDTO();
        amountDTO.setAmount((Long) obj[0]);
        amountDTO.setPurchaseName((String) obj[1]);
        return amountDTO;
    }
}
