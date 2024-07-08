package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.ElectroTypeDTO;
import ru.isands.test.estore.dto.EmployeeDTO;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.exception.EntityNotValidException;
import ru.isands.test.estore.model.ElectroType;
import ru.isands.test.estore.model.Employee;
import ru.isands.test.estore.repository.EmployeeRepository;
import ru.isands.test.estore.util.PagingUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionTypeService positionTypeService;
    private final ShopService shopService;
    private final ElectroTypeService electroTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PositionTypeService positionTypeService, ShopService shopService, ElectroTypeService electroTypeService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.positionTypeService = positionTypeService;
        this.shopService = shopService;
        this.electroTypeService = electroTypeService;
        this.modelMapper = modelMapper;
    }

    private void validateEmployeeReferences(EmployeeDTO employeeDTO) {
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
            throw new EntityNotValidException("Не удалось создать/обновить сотрудника", validationErrors);
        }
    }

    public Employee findEmployeeByIdThrowable(Long id, String message) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(message, List.of("Не найден сотрудник с id=" + id)));
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = findEmployeeByIdThrowable(id, "Не удалось найти сотрудника");
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        validateEmployeeReferences(employeeDTO);
        Employee saved = employeeRepository.save(modelMapper.map(employeeDTO, Employee.class));
        return modelMapper.map(saved, EmployeeDTO.class);
    }

    public List<EmployeeDTO> getAllEmployees(int page, int size, String sortField, String sortType) {
        return employeeRepository.findAll(PagingUtil.createPageRequest(page, size, sortField, sortType, Employee.class)).getContent().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = findEmployeeByIdThrowable(id, "Не удалось обновить сотрудника");
        validateEmployeeReferences(employeeDTO);
        BeanUtils.copyProperties(employeeDTO, existingEmployee);
        Employee employeeSaved = employeeRepository.save(existingEmployee);
        return modelMapper.map(employeeSaved, EmployeeDTO.class);
    }

    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeByIdThrowable(id, "Не удалось удалить сотрудника");
        employeeRepository.delete(employee);
    }

    public Set<ElectroTypeDTO> getEmployeeElectroTypes(Long id) {
        Employee employee = findEmployeeByIdThrowable(id, "Не удалось получить назначения сотрудника");
        Set<ElectroType> electroTypes = employee.getElectroTypes();
        return electroTypes.stream()
                .map(electroType -> modelMapper.map(electroType, ElectroTypeDTO.class))
                .collect(Collectors.toSet());
    }

    public void addElectroTypeToEmployee(Long id, Long electroTypeDTO) {
        Employee employee = findEmployeeByIdThrowable(id, "Не удалось добавить назначение сотрудника");
        ElectroType electroType = electroTypeService.findElectroTypeByIdThrowable(electroTypeDTO, "Не удалось добавить назначение сотрудника");
        boolean isAdded = employee.getElectroTypes().add(electroType);
        if (!isAdded) {
            throw new EntityNotValidException("Не удалось добавить назначение сотрудника", List.of("Вероятно, такое назначение уже есть"));
        }
    }

    public void removeElectroTypeFromEmployee(Long employeeId, Long electroTypeId) {
        Employee employee = findEmployeeByIdThrowable(employeeId, "Не удалось удалить назначение сотрудника");
        ElectroType electroType = electroTypeService.findElectroTypeByIdThrowable(electroTypeId, "Не удалось удалить назначение сотрудника");
        employee.getElectroTypes().remove(electroType);
    }
}
