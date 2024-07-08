package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.PurchaseTypeDTO;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.model.PurchaseType;
import ru.isands.test.estore.repository.PurchaseTypeRepository;
import ru.isands.test.estore.util.PagingUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseTypeService {

    private final PurchaseTypeRepository purchaseTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseTypeService(PurchaseTypeRepository purchaseTypeRepository, ModelMapper modelMapper) {
        this.purchaseTypeRepository = purchaseTypeRepository;
        this.modelMapper = modelMapper;
    }

    public boolean purchaseTypeExists(Long id) {
        return purchaseTypeRepository.existsById(id);
    }

    public List<PurchaseTypeDTO> getAllPurchaseTypes(int page, int size, String sortField, String sortType) {

        return purchaseTypeRepository.findAll(
                        PagingUtil.createPageRequest(page, size, sortField, sortType, PurchaseType.class)
                ).stream()
                .map(purchaseType -> modelMapper.map(purchaseType, PurchaseTypeDTO.class))
                .collect(Collectors.toList());
    }

    public PurchaseTypeDTO getPurchaseTypeById(Long id) {
        PurchaseType purchaseType = purchaseTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Тип оплаты не найден", List.of("Не удалось найти тип оплаты с id=" + id)));
        return modelMapper.map(purchaseType, PurchaseTypeDTO.class);
    }

    public PurchaseTypeDTO createPurchaseType(PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseType purchaseType = modelMapper.map(purchaseTypeDTO, PurchaseType.class);
        PurchaseType savedPurchaseType = purchaseTypeRepository.save(purchaseType);
        return modelMapper.map(savedPurchaseType, PurchaseTypeDTO.class);
    }

    public PurchaseTypeDTO updatePurchaseType(Long id, PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseType existingPurchaseType = purchaseTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Обновление не выполнено", List.of("Не удалось найти тип оплаты с id=" + id)));
        modelMapper.map(purchaseTypeDTO, existingPurchaseType);
        PurchaseType updatedPurchaseType = purchaseTypeRepository.save(existingPurchaseType);
        return modelMapper.map(updatedPurchaseType, PurchaseTypeDTO.class);
    }

    public void deletePurchaseType(Long id) {
        if (!purchaseTypeExists(id)) {
            throw new EntityNotFoundException("Не удалось удалить тип оплаты", List.of("Не удалось найти тип оплаты с id=" + id));
        }
        purchaseTypeRepository.deleteById(id);
    }
}
