package ru.isands.test.estore.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;

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
	@TableGenerator(name = "purchase_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.models.Purchase", table = "counter", valueColumnName = "currentid", allocationSize = 1)
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
