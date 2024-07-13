package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.PurchaseCreateDTO;
import ru.isands.test.estore.dto.PurchaseDetailsDTO;
import ru.isands.test.estore.exception.ElectroItemOutOfStockException;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.exception.IllegalParamException;
import ru.isands.test.estore.model.*;
import ru.isands.test.estore.repository.*;
import ru.isands.test.estore.util.PagingUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ShopRepository shopRepository;
    private final PurchaseTypeRepository purchaseTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final ElectroItemRepository electroItemRepository;
    private final ElectroShopRepository electroShopRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseService(
            PurchaseRepository purchaseRepository,
            ShopRepository shopRepository,
            PurchaseTypeRepository purchaseTypeRepository,
            EmployeeRepository employeeRepository,
            ElectroItemRepository electroItemRepository,
            ElectroShopRepository electroShopRepository,
            ModelMapper modelMapper) {
        this.purchaseRepository = purchaseRepository;
        this.shopRepository = shopRepository;
        this.purchaseTypeRepository = purchaseTypeRepository;
        this.employeeRepository = employeeRepository;
        this.electroItemRepository = electroItemRepository;
        this.electroShopRepository = electroShopRepository;
        this.modelMapper = modelMapper;
    }

    public PurchaseDetailsDTO createPurchase(PurchaseCreateDTO purchase) {
        List<String> errors = new ArrayList<>();

        Optional<Shop> shop = shopRepository.findById(purchase.getShopId());
        Optional<PurchaseType> purchaseType = purchaseTypeRepository.findById(purchase.getPurchaseTypeId());
        Optional<Employee> employee = employeeRepository.findById(purchase.getEmployeeId());
        Optional<ElectroItem> electroItem = electroItemRepository.findById(purchase.getElectroItemId());

        if (shop.isEmpty()) {
            errors.add("Не найден магазин с id=" + purchase.getShopId());
        }

        if (purchaseType.isEmpty()) {
            errors.add("Не найден тип оплаты с id=" + purchase.getPurchaseTypeId());
        }

        if (employee.isEmpty()) {
            errors.add("Не найден сотрудник с id=" + purchase.getEmployeeId());
        }

        if (electroItem.isEmpty()) {
            errors.add("Не найден товар с id=" + purchase.getElectroItemId());
        }

        if (errors.size() > 0) {
            throw new IllegalParamException("Не удалось добавить покупки", errors);
        }

        ElectroShop electroShop = electroShopRepository.findByShopIdAndElectroItemId(purchase.getShopId(), purchase.getElectroItemId())
                .orElseThrow(() -> new ElectroItemOutOfStockException(
                        "Данного товара нет в выбранном магазине",
                        List.of(String.format("Товар с id=%d в магазине с id=%d отсутствует", purchase.getElectroItemId(), purchase.getShopId()))
                ));

        if (electroShop.getCount() == 0) {
            throw new ElectroItemOutOfStockException(
                    "В выбранном магазине товар закончился",
                    List.of(String.format("Товар с id=%d в магазине с id=%d закончился", purchase.getElectroItemId(), purchase.getShopId())));
        }
        electroShop.setCount(electroShop.getCount() - 1);

        Purchase newPurchase = new Purchase();
        newPurchase.setPurchaseDate(new Date());
        newPurchase.setType(purchaseType.get());
        newPurchase.setShop(shop.get());
        newPurchase.setElectro(electroItem.get());
        newPurchase.setEmployee(employee.get());

        Purchase saved = purchaseRepository.save(newPurchase);

        return modelMapper.map(saved, PurchaseDetailsDTO.class);
    }

    public List<PurchaseDetailsDTO> findAllPageable(int page, int size, String sortField, String sortType) {
        return purchaseRepository.findAll(PagingUtil.createPageRequest(page, size, sortField, sortType, Purchase.class))
                .stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseDetailsDTO.class))
                .collect(Collectors.toList());
    }

    public PurchaseDetailsDTO findById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о покупке не найдена", List.of("Не найдена покупка с id=" + id)));
        return modelMapper.map(purchase, PurchaseDetailsDTO.class);
    }
}
