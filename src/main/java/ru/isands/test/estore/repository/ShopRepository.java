package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isands.test.estore.model.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
