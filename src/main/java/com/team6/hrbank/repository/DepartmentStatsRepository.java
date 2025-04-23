package com.team6.hrbank.repository;

import com.team6.hrbank.entity.DepartmentStats;
import com.team6.hrbank.entity.EmployeeState;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentStatsRepository extends JpaRepository<DepartmentStats, Long> {

  // 넘어온 조회 날짜 + 상태를 기준으로 조회하여 부서 별 DepartmentStats List 반환
  List<DepartmentStats> findAllByStatDate(LocalDate statDate, EmployeeState employeeState);

}
