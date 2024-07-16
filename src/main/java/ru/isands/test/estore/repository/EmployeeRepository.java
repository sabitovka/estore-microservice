package ru.isands.test.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isands.test.estore.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
