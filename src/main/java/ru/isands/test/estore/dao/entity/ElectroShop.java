package ru.isands.test.estore.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@IdClass(ElectroShopPK.class)
@Table(name = "store_eshop")
public class ElectroShop {

	/**
	 * Идентификатор магазина
	 */
	@Id
	@Column(name = "shopId", nullable = false)
	Long shopId;

	/**
	 * Идентификатор электротовара
	 */
	@Id
	@Column(name = "electroItemId", nullable = false)
	Long electroItemId;

	/**
	 * Оставшееся количество
	 */
	@Column(name = "count_", nullable = false)
	int count;

	/**
	 * Магазин
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("shopId")
	@JoinColumn(name = "shopId")
	Shop shop;

	/**
	 * Товар
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("electroItemId")
	@JoinColumn(name = "electroItemId")
	ElectroItem electroItem;
}
