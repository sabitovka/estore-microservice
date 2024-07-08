package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.EmployeeDTO;
import ru.isands.test.estore.exception.EntityNotValidException;
import ru.isands.test.estore.model.Employee;
import ru.isands.test.estore.repository.EmployeeRepository;
import ru.isands.test.estore.util.PagingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionTypeService positionTypeService;
    private final ShopService shopService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PositionTypeService positionTypeService, ShopService shopService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.positionTypeService = positionTypeService;
        this.shopService = shopService;
        this.modelMapper = modelMapper;
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        List<String> validationErrors = new ArrayList<>();

        boolean positionTypeExists = positionTypeService.isPositionTypeExists(employeeDTO.getPositionTypeId());
        if (!positionTypeExists) {
            validationErrors.add("Не найдена должность с id=" + employeeDTO.getPositionTypeId());
        }

        boolean shopExists = shopService.isShopExists(employeeDTO.getShopId());
        if (!shopExists) {
            validationErrors.add("Не найден магазин с id=" + employeeDTO.getShopId());
        }

        if (validationErrors.size() > 0) {
            throw new EntityNotValidException("Не удалось создать нового сотрудника", validationErrors);
        }

        Employee saved = employeeRepository.save(modelMapper.map(employeeDTO, Employee.class));
        return modelMapper.map(saved, EmployeeDTO.class);
    }

    public List<EmployeeDTO> getAllEmployees(int page, int size, String sortField, String sortType) {
        return employeeRepository.findAll(PagingUtil.createPageRequest(page, size, sortField, sortType, Employee.class)).getContent().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }
}
