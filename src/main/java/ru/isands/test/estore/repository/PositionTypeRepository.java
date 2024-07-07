package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isands.test.estore.model.PositionType;

public interface PositionTypeRepository extends JpaRepository<PositionType, Long> {
}
