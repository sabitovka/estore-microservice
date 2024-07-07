package ru.isands.test.estore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isands.test.estore.dto.ShopDTO;
import ru.isands.test.estore.exception.EntityNotFoundException;
import ru.isands.test.estore.model.Shop;
import ru.isands.test.estore.repository.ShopRepository;
import ru.isands.test.estore.util.PagingUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ShopService(ShopRepository shopRepository, ModelMapper modelMapper) {
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
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
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Не удалось обновить магазин", List.of("Не найден магазин с id=" + id)));
        shop.setName(shopDTO.getName());
        shop.setAddress(shopDTO.getAddress());
        Shop updatedShop = shopRepository.save(shop);
        return modelMapper.map(updatedShop, ShopDTO.class);
    }

    public void deleteShop(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Не удалось удалить магазин", List.of("Не найден магазин с id=" + id)));
        shopRepository.delete(shop);
    }
}
