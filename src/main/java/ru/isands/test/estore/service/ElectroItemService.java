package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.ElectroItemDTO;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.exception.EntityNotValidException;
import ru.isands.test.estore.model.ElectroItem;
import ru.isands.test.estore.repository.ElectroItemRepository;
import ru.isands.test.estore.util.PagingUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ElectroItemService {
    private final ElectroItemRepository electroItemRepository;
    private final ElectroTypeService electroTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public ElectroItemService(ElectroItemRepository electroItemRepository, ElectroTypeService electroTypeService, ModelMapper modelMapper) {
        this.electroItemRepository = electroItemRepository;
        this.electroTypeService = electroTypeService;
        this.modelMapper = modelMapper;
    }

    private void validateEItemReferences(ElectroItemDTO electroItemDTO) {
        List<String> errors = new ArrayList<>();
        if (!electroTypeService.isElectroTypeExists(electroItemDTO.getElectroTypeId())) {
            errors.add("Не найден тип электроники с id = " + electroItemDTO.getElectroTypeId());
        }
        if (errors.size() > 0) {
            throw new EntityNotValidException("Не удалось создать/обновить товар", errors);
        }
    }

    public ElectroItem findElectroItemByIdThrowable(Long id, String message) {
        return electroItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(message, List.of("Не удалось найти товар с id = " + id)));
    }

    public List<ElectroItemDTO> getAllElectroItems(int page, int size, String sortField, String sortType) {
        return electroItemRepository.findAll(
                PagingUtil.createPageRequest(page, size, sortField, sortType, ElectroItem.class)).getContent().stream()
                .map(electroItem -> modelMapper.map(electroItem, ElectroItemDTO.class))
                .collect(Collectors.toList());
    }

    public ElectroItemDTO getElectroItemById(Long id) {
        ElectroItem electroItem = findElectroItemByIdThrowable(id, "Не удалось найти товар");
        return modelMapper.map(electroItem, ElectroItemDTO.class);
    }

    public ElectroItemDTO createElectroItem(ElectroItemDTO electroItem) {
        validateEItemReferences(electroItem);
        ElectroItem saved = electroItemRepository.save(modelMapper.map(electroItem, ElectroItem.class));
        return modelMapper.map(saved, ElectroItemDTO.class);
    }

    public ElectroItemDTO updateElectroItem(Long id, ElectroItemDTO electroItem) {
        ElectroItem existingElectroItem = findElectroItemByIdThrowable(id, "Не удалось обновить товар");
        validateEItemReferences(electroItem);
        BeanUtils.copyProperties(electroItem, existingElectroItem);
        electroItemRepository.save(existingElectroItem);
        return modelMapper.map(existingElectroItem, ElectroItemDTO.class);
    }

    public void deleteElectroItem(Long id) {
        if (!electroItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Не удалось удалить товар", List.of("Не найден товар с id = " + id));
        }
        electroItemRepository.deleteById(id);
    }
}
