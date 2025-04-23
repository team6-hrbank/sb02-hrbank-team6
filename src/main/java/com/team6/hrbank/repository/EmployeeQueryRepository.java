package com.team6.hrbank.repository;

import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.entity.EmployeePosition;
import com.team6.hrbank.entity.EmployeeState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeQueryRepository extends JpaRepository<Employee, Long> {
  long countByDepartmentIdAndEmployeeState(Long departmentId, EmployeeState employeeState);

  long countByPositionAndEmployeeState(EmployeePosition position, EmployeeState employeeState);

}
