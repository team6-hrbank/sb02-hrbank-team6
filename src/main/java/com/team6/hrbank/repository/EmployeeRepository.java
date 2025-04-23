package com.team6.hrbank.repository;

import com.team6.hrbank.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByEmployeeNumber(String employeeNumber);
}
