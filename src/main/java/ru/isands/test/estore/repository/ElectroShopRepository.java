package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.isands.test.estore.model.ElectroShop;
import ru.isands.test.estore.model.ElectroShopPK;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElectroShopRepository extends JpaRepository<ElectroShop, ElectroShopPK> {
    List<ElectroShop> findAllByShopId(Long shopId);
    Optional<ElectroShop> findByShopIdAndElectroItemId(Long shopId, Long electroItemId);
}
