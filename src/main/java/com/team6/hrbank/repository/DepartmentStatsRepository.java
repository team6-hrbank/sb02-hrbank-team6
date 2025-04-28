package com.team6.hrbank.repository;

import com.team6.hrbank.entity.DepartmentStats;
import com.team6.hrbank.entity.EmployeeState;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartmentStatsRepository extends JpaRepository<DepartmentStats, Long> {

  List<DepartmentStats> findAllByStatDateAndEmployeeState(LocalDate statDate, EmployeeState employeeState);

  Optional<DepartmentStats> findByStatDateAndEmployeeStateAndDepartmentName(LocalDate statDate, EmployeeState employeeState, String departmentName);

}
