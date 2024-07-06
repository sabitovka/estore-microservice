package ru.isands.test.estore.dao.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "store_purchase")
public class Purchase implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Идентификатор покупки
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "purchase_counter")
	@TableGenerator(name = "purchase_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.dao.entity.Purchase", table = "counter", valueColumnName = "currentid", allocationSize = 1)
	@Column(name = "id_", unique = true, nullable = false)
	Long id;

	/**
	 * Товар
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "electroId", nullable = false)
	ElectroItem electro;

	/**
	 * Сотрудник
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employeeId", nullable = false)
	Employee employee;

	/**
	 * Магазин
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shopId", nullable = false)
	Shop shop;

	/**
	 * Дата совершения покупки
	 */
	@Column(name = "purchaseDate", nullable = false)
	Date purchaseDate;

	/**
	 * Способ оплаты
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId", nullable = false)
	PurchaseType type;
}
