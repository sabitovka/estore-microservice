package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.isands.test.estore.model.ElectroShop;
import ru.isands.test.estore.model.ElectroShopPK;

public interface ElectroShopRepository extends JpaRepository<ElectroShop, ElectroShopPK> {

}
