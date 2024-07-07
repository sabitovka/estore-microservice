package ru.isands.test.estore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "store_shop")
public class Shop {
    /**
     * Идентификатор магазина
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "shop_counter")
    @TableGenerator(name = "shop_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.model.Shop", table = "counter", valueColumnName = "currentid", allocationSize = 2)
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Наименование магазина
     */
    @Column(name = "name", nullable = false, length = 250)
    String name;

    /**
     * Адрес магазина
     */
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    String address;
}
