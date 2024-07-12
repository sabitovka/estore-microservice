package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.AddElectroItemDTO;
import ru.isands.test.estore.dto.ElectroShopDTO;
import ru.isands.test.estore.dto.ShopDTO;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.exception.IllegalParamException;
import ru.isands.test.estore.model.ElectroShop;
import ru.isands.test.estore.model.Shop;
import ru.isands.test.estore.repository.ElectroShopRepository;
import ru.isands.test.estore.repository.ShopRepository;
import ru.isands.test.estore.util.PagingUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShopService {
    private final ShopRepository shopRepository;
    private final ElectroItemService electroItemService;
    private final ElectroShopRepository electroShopRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ShopService(ShopRepository shopRepository, ElectroItemService electroItemService, ElectroShopRepository electroShopRepository, ModelMapper modelMapper) {
        this.shopRepository = shopRepository;
        this.electroItemService = electroItemService;
        this.electroShopRepository = electroShopRepository;
        this.modelMapper = modelMapper;
    }

    private Shop findShopByIdThrowable(Long id, String message) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message, List.of("Не найден магазин с id=" + id)));
        return shop;
    }

    public boolean isShopExists(Long id) {
        return shopRepository.existsById(id);
    }

    public ShopDTO getShopById(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Магазин не найден", List.of("Не найден магазин с id=" + id)));
        return modelMapper.map(shop, ShopDTO.class);
    }

    public List<ShopDTO> getAllShops(int page, int size, String sortField, String sortType) {
        return shopRepository.findAll(
                        PagingUtil.createPageRequest(page, size, sortField, sortType, Shop.class)).getContent().stream()
                .map(shop -> modelMapper.map(shop, ShopDTO.class))
                .collect(Collectors.toList());
    }

    public ShopDTO createShop(ShopDTO shopDTO) {
        Shop shop = modelMapper.map(shopDTO, Shop.class);
        Shop savedShop = shopRepository.save(shop);
        return modelMapper.map(savedShop, ShopDTO.class);
    }

    public ShopDTO updateShop(Long id, ShopDTO shopDTO) {
        Shop shop = findShopByIdThrowable(id, "Не удалось обновить магазин");
        shop.setName(shopDTO.getName());
        shop.setAddress(shopDTO.getAddress());
        Shop updatedShop = shopRepository.save(shop);
        return modelMapper.map(updatedShop, ShopDTO.class);
    }

    public void deleteShop(Long id) {
        Shop shop = findShopByIdThrowable(id, "Не удалось удалить магазин");
        shopRepository.delete(shop);
    }

    public void addElectroItemsToShop(Long shopId, List<AddElectroItemDTO> electroItems) {
        findShopByIdThrowable(shopId, "Не удалось добавить товары в магазин");
        List<String> errors = new ArrayList<>();
        List<ElectroShop> electroShops = new ArrayList<>();

        for (AddElectroItemDTO electroItem : electroItems) {
            if (!electroItemService.isElectroItemExists(electroItem.getElectroItemId())) {
                errors.add(String.format("Не найден товар с id=%d", electroItem.getElectroItemId()));
            }

            // Если есть хотя бы одна ошибка, дальше объекты не создаем, т.к. все равно будет throw.
            if (errors.size() > 0) {
                continue;
            }

            ElectroShop existingElectroShop = electroShopRepository.findByShopIdAndElectroItemId(shopId, electroItem.getElectroItemId())
                    .orElseGet(() -> {
                        ElectroShop electroShop = new ElectroShop();
                        electroShop.setElectroItemId(electroItem.getElectroItemId());
                        electroShop.setShopId(shopId);
                        electroShop.setCount(0);
                        return electroShop;
                    });
            existingElectroShop.setCount(existingElectroShop.getCount() + electroItem.getCount());
            electroShops.add(existingElectroShop);
        }

        if (errors.size() > 0) {
            throw new IllegalParamException("Не удалось добавить список товаров", errors);
        }

        electroShopRepository.saveAll(electroShops);
    }

    public List<ElectroShopDTO> findAllShopElectroItems(Long shopId) {
        Shop shop = findShopByIdThrowable(shopId, "Не удалось получить список товаров магазина");
        List<ElectroShop> allElectroShops = electroShopRepository.findAllByShopId(shopId);
        return allElectroShops.stream()
                .map(electroShop -> {
                    ElectroShopDTO electroShopDTO = new ElectroShopDTO();
                    electroShopDTO.setCount(electroShop.getCount());
                    BeanUtils.copyProperties(electroShop.getElectroItem(), electroShopDTO);
                    electroShopDTO.setElectroTypeId(electroShop.getElectroItem().getElectroType().getId());
                    return electroShopDTO;
                })
                .collect(Collectors.toList());
    }
}
