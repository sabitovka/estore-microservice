package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.isands.test.estore.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
