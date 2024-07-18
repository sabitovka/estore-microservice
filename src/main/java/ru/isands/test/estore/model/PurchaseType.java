package ru.isands.test.estore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "store_purchase_type")
public class PurchaseType {

    /**
     * Идентификатор типа покупки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "purchase_type_counter")
    @TableGenerator(name = "purchase_type_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.model.PurchaseType", table = "counter", valueColumnName = "currentid", allocationSize = 1)
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Наименование типа покупки
     */
    @Column(name = "name", nullable = false, length = 150)
    String name;
}
