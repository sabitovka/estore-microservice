package ru.isands.test.estore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isands.test.estore.repository.EntityManagerRepository;

@Service
public class DatabaseTruncationService {

    // TODO: 14.07.2024 Исправить получение всех таблиц напрямую из БД
    private final String[] TABLE_NAMES = {
            "counter",
            "store_purchase",
            "store_eshop",
            "store_electro_employee",
            "store_employee",
            "store_electro_item",
            "store_electro_type",
            "store_position_type",
            "store_purchase_type",
            "store_shop"
    };
    private final EntityManagerRepository entityManagerRepository;

    public DatabaseTruncationService(EntityManagerRepository entityManagerRepository) {
        this.entityManagerRepository = entityManagerRepository;
    }

    @Transactional
    public void truncateAllTables() {
        entityManagerRepository.truncateTable(TABLE_NAMES);
    }
}
