package com.team6.hrbank.repository;

import com.team6.hrbank.entity.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
  boolean existsByDepartmentName(String departmentName);

  @Query("SELECT COUNT(e.id) FROM Employee e WHERE e.department.id = :departmentId")
  Long countEmployees(@Param("departmentId") Long departmentId);
}
