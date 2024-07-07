package ru.isands.test.estore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "store_electro_item")
public class ElectroItem {
    /**
     * Идентификатор товара
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "electro_item_counter")
    @TableGenerator(name = "electro_item_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.models.ElectroItem", table = "counter", valueColumnName = "currentid", allocationSize = 2)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;

    /**
     * Наименование товара
     */
    @Column(name = "name", nullable = false, length = 150)
    String name;

    /**
     * Стоимость товара (P.S. почему Long, а не double?)
     */
    @Column(name = "price", nullable = false)
    Long price;

    /**
     * Общее кол-во
     */
    @Column(name = "count", nullable = false)
    int count;

    /**
     * Архив - товар недоступен
     */
    @Column(name = "archive")
    boolean archive;

    /**
     * Описание
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etypeId")
    ElectroType electroType;
}
