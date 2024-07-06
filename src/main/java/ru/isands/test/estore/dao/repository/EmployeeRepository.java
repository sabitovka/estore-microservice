package ru.isands.test.estore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.isands.test.estore.dao.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
