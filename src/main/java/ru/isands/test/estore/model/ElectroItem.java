package ru.isands.test.estore.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

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
     * Стоимость товара (P.S. почему на схеме Long, а не double?)
     */
    @Column(name = "price", nullable = false)
    Long price;

    /**
     * Общий остаток товара
     */
    @Formula("(SELECT COALESCE(SUM(e.count_), 0) FROM store_eshop e WHERE e.electroitemid = id_)")
    int totalCount;

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
