package com.team6.hrbank.repository;

import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.entity.EmployeePosition;
import com.team6.hrbank.entity.EmployeeState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeQueryRepository extends JpaRepository<Employee, Long> {
  long countByDepartmentIdAndEmployeeState(Long departmentId, EmployeeState employeeState);

  long countByEmployeePositionAndEmployeeState(EmployeePosition position, EmployeeState employeeState);

  // 직원 수 집계 시 퇴직한 직원 제외
  long countByEmployeeStateNot(EmployeeState employeeState);

  // 추후 총 직원 수 조회 API에 사용할 예정 (대시보드 위쪽)
  // long countByEmployeeStateAndHireDateBetween(EmployeeState employeeState, LocalDate from, LocalDate to);
}
