package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.PositionTypeDTO;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.model.PositionType;
import ru.isands.test.estore.repository.PositionTypeRepository;
import ru.isands.test.estore.util.PagingUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionTypeService {

    private final PositionTypeRepository positionTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PositionTypeService(PositionTypeRepository positionTypeRepository, ModelMapper modelMapper) {
        this.positionTypeRepository = positionTypeRepository;
        this.modelMapper = modelMapper;
    }

    public boolean isPositionTypeExists(Long id) {
        return positionTypeRepository.existsById(id);
    }

    public PositionTypeDTO getPositionTypeById(Long id) {
        PositionType positionType = positionTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Должность не найдена", List.of("Не найдена должность с id=" + id)));
        return modelMapper.map(positionType, PositionTypeDTO.class);
    }

    public List<PositionTypeDTO> getAllPositionTypes(int page, int elemsPerPage, String sortField, String sortType) {
        return positionTypeRepository.findAll(
                PagingUtil.createPageRequest(page, elemsPerPage, sortField, sortType, PositionType.class)).getContent().stream()
                .map(positionType -> modelMapper.map(positionType, PositionTypeDTO.class))
                .collect(Collectors.toList());
    }

    public PositionTypeDTO createPositionType(PositionTypeDTO positionTypeDTO) {
        PositionType positionType = modelMapper.map(positionTypeDTO, PositionType.class);
        PositionType savedPositionType = positionTypeRepository.save(positionType);
        return modelMapper.map(savedPositionType, PositionTypeDTO.class);
    }

    public PositionTypeDTO updatePositionType(Long id, PositionTypeDTO positionTypeDTO) {
        PositionType positionType = positionTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не удалось обновить должность", List.of("Не найдена должность с id=" + id)));
        positionType.setName(positionTypeDTO.getName());
        PositionType updatedPositionType = positionTypeRepository.save(positionType);
        return modelMapper.map(updatedPositionType, PositionTypeDTO.class);
    }

    public void deletePositionType(Long id) {
        PositionType positionType = positionTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не удалось удалить должность", List.of("Не найдена должность с id=" + id)));
        positionTypeRepository.delete(positionType);
    }

}
