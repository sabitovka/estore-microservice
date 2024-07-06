package ru.isands.test.estore.dao.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "store_position_type")
public class PositionType {

    /**
     * Идентификатор типа должности
     */
    @Id
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Наименование типа должности
     */
    @Column(name = "name", nullable = false, length = 150)
    String name;

}
