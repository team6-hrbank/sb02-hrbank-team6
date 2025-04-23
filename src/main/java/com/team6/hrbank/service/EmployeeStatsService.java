package com.team6.hrbank.service;

import com.team6.hrbank.dto.employeestats.EmployeeTrendDto;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeStatsService {
  // 배치 처리
  void createTodayStats();

  List<EmployeeTrendDto> getEmployeeTrend(LocalDate from, LocalDate to, String unit);
}
