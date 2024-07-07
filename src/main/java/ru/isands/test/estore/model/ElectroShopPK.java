package ru.isands.test.estore.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ElectroShopPK implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *  Идентификатор магазина
	 */
	@Column(name = "shopId")
	Long shopId;

	/**
	 *  Идентификатор электротовара
	 */
	@Column(name = "electroItemId")
	Long electroItemId;
}
