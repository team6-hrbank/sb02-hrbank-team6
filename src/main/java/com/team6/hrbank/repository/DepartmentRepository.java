package com.team6.hrbank.repository;

import com.team6.hrbank.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>,
    JpaSpecificationExecutor<Department>{
  boolean existsByDepartmentName(String departmentName);

  @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
  Long countEmployeesByDepartmentId(@Param("departmentId") Long departmentId);
}
