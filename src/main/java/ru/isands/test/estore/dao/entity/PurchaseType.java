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
@Table(name = "store_purchase_type")
public class PurchaseType {

    /**
     * Идентификатор типа покупки
     */
    @Id
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Наименование типа покупки
     */
    @Column(name = "name", nullable = false, length = 150)
    String name;
}
