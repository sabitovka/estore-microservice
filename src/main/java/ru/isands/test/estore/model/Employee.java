package ru.isands.test.estore.model;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "store_employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Идентификатор сотрудника
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "employee_counter")
	@TableGenerator(name = "employee_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.models.Employee", table = "counter", valueColumnName = "currentid", allocationSize = 2)
	@Column(name = "id_", unique = true, nullable = false)
	Long id;

	/**
	 * Фамилия сотрудника
	 */
	@Column(name = "lastname", nullable = false, length = 100)
	String lastName;

	/**
	 * Имя сотрудника
	 */
	@Column(name = "firstname", nullable = false, length = 100)
	String firstName;

	/**
	 * Отчество сотрудника
	 */
	@Column(name = "patronymic", length = 100)
	String patronymic;

	/**
	 * Дата рождения сотрудника
	 */
	@Column(name = "birthDate", nullable = false)
	Date birthDate;

	/**
	 * Пол сотрудника (true - мужской, false - женский)
	 */
	@Column(name = "gender", nullable = false)
	boolean gender;

	/**
	 * Ссылка на должность сотрудника
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "positionId")
	PositionType positionType;

	/**
	 * Ссылка на прикрепленный магазин
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shopId")
	Shop shop;

	/**
	 * Ссылки на прикрепленные типы электроники
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "store_electro_employee",
			joinColumns = @JoinColumn(name = "employeeId"),
			inverseJoinColumns = @JoinColumn(name = "electroTypeId")
	)
	Set<ElectroType> electroTypes;
}
