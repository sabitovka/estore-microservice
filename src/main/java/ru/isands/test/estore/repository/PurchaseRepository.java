package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.isands.test.estore.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
