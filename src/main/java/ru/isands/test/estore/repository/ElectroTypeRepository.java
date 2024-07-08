package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isands.test.estore.model.ElectroType;

public interface ElectroTypeRepository extends JpaRepository<ElectroType, Long> {
}
