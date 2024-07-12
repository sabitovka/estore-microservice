package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.ElectroTypeDTO;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.model.ElectroType;
import ru.isands.test.estore.repository.ElectroTypeRepository;
import ru.isands.test.estore.util.PagingUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectroTypeService {
    private final ElectroTypeRepository electroTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ElectroTypeService(ElectroTypeRepository electroTypeRepository, ModelMapper modelMapper) {
        this.electroTypeRepository = electroTypeRepository;
        this.modelMapper = modelMapper;
    }

    public boolean isElectroTypeExists(Long id) {
        return electroTypeRepository.existsById(id);
    }

    public ElectroType findElectroTypeByIdThrowable(Long id, String message) {
        return electroTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(message, List.of("Не удалось найти тип электроники c id=" + id)));
    }

    public List<ElectroTypeDTO> getAllElectroTypes(int page, int size, String sortField, String sortType) {
        return electroTypeRepository.findAll(
                        PagingUtil.createPageRequest(page, size, sortField, sortType, ElectroType.class)
                ).stream()
                .map(electroType -> modelMapper.map(electroType, ElectroTypeDTO.class))
                .collect(Collectors.toList());
    }

    public ElectroTypeDTO getElectroTypeById(Long id) {
        ElectroType electroType = findElectroTypeByIdThrowable(id, "Тип электроники не найден");
        return modelMapper.map(electroType, ElectroTypeDTO.class);
    }

    public ElectroTypeDTO createElectroType(ElectroTypeDTO electroTypeDTO) {
        ElectroType electroType = modelMapper.map(electroTypeDTO, ElectroType.class);
        ElectroType savedElectroType = electroTypeRepository.save(electroType);
        return modelMapper.map(savedElectroType, ElectroTypeDTO.class);
    }

    public ElectroTypeDTO updateElectroType(Long id, ElectroTypeDTO electroTypeDTO) {
        ElectroType existingElectroType = findElectroTypeByIdThrowable(id, "Тип электроники не найден");
        modelMapper.map(electroTypeDTO, existingElectroType);
        ElectroType updatedElectroType = electroTypeRepository.save(existingElectroType);
        return modelMapper.map(updatedElectroType, ElectroTypeDTO.class);
    }

    public void deleteElectroType(Long id) {
        if (!electroTypeRepository.existsById(id)) {
            throw new EntityNotFoundException("Не удалось удалить тип электроники", List.of("Не удалось найти тип электроники c id=" + id));
        }
        electroTypeRepository.deleteById(id);
    }
}
