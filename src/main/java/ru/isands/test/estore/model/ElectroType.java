package ru.isands.test.estore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "store_electro_type")
public class ElectroType {
    /**
     * Идентификатор типа электроники
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "electro_type_generator")
    @TableGenerator(name = "electro_type_generator", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.model.ElectroType", table = "counter", valueColumnName = "currentid", allocationSize = 1)
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Наименование типа электроники
     */
    @Column(name = "name", nullable = false, length = 150)
    String name;
}
