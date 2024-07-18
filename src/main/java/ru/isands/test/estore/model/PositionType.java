package ru.isands.test.estore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "store_position_type")
public class PositionType {

    /**
     * Идентификатор типа должности
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "position_counter")
    @TableGenerator(name = "position_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.model.PositionType", table = "counter", valueColumnName = "currentid", allocationSize = 1)
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Наименование типа должности
     */
    @Column(name = "name", nullable = false, length = 150)
    String name;

}
