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

    public List<ElectroTypeDTO> getAllElectroTypes(int page, int size, String sortField, String sortType) {
        return electroTypeRepository.findAll(
                        PagingUtil.createPageRequest(page, size, sortField, sortType, ElectroType.class)
                ).stream()
                .map(electroType -> modelMapper.map(electroType, ElectroTypeDTO.class))
                .collect(Collectors.toList());
    }

    public ElectroTypeDTO getElectroTypeById(Long id) {
        ElectroType electroType = electroTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Тип электроники не найден", List.of("Не удалось найти тип электроники c id=" + id)));
        return modelMapper.map(electroType, ElectroTypeDTO.class);
    }

    public ElectroTypeDTO createElectroType(ElectroTypeDTO electroTypeDTO) {
        ElectroType electroType = modelMapper.map(electroTypeDTO, ElectroType.class);
        ElectroType savedElectroType = electroTypeRepository.save(electroType);
        return modelMapper.map(savedElectroType, ElectroTypeDTO.class);
    }

    public ElectroTypeDTO updateElectroType(Long id, ElectroTypeDTO electroTypeDTO) {
        ElectroType existingElectroType = electroTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не удалось обновить тип электроники", List.of("Не удалось найти тип электроники c id=" + id)));
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
