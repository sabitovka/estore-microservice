package ru.isands.test.estore.dao.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "store_shop")
public class Shop {
    /**
     * Идентификатор магазина
     */
    @Id
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
