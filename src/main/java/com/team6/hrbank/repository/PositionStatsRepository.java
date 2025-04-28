package com.team6.hrbank.repository;

import com.team6.hrbank.entity.EmployeePosition;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.entity.PositionStats;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionStatsRepository extends JpaRepository<PositionStats, Long> {

  // 넘어온 조회 날짜 + 상태를 기준으로 조회하여 직무 별 PositionStats List 반환
  List<PositionStats> findAllByStatDateAndEmployeeState(LocalDate statDate, EmployeeState employeeState);

  Optional<PositionStats> findByStatDateAndEmployeeStateAndPositionName(LocalDate statDate, EmployeeState employeeState, EmployeePosition employeePosition);
}
