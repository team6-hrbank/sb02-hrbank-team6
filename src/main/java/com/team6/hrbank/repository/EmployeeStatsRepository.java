package com.team6.hrbank.repository;

import com.team6.hrbank.entity.EmployeeStats;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeStatsRepository extends JpaRepository<EmployeeStats, Long> {

  Optional<EmployeeStats> findByStatDate(LocalDate statDate);

}
