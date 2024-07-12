package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isands.test.estore.model.ElectroItem;

public interface ElectroItemRepository extends JpaRepository<ElectroItem, Long> {
}
