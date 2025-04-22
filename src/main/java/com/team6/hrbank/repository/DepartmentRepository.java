package com.team6.hrbank.repository;

import com.team6.hrbank.entity.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
  Department save(Department department);
  List<Department> findAll();
  Optional<Department> findById(Long id);
  void deleteById(Long id);

  boolean existsByDepartmentName(String departmentName);
  Optional<Department> findByDepartmentName(String name);
  Optional<Department> findByDepartmentDescription(String description);
}
