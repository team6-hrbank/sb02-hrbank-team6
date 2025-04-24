package com.team6.hrbank.service;

import com.team6.hrbank.dto.employeestats.EmployeeDistributionDto;
import com.team6.hrbank.entity.EmployeeState;
import java.time.LocalDate;
import java.util.List;

public interface PositionStatsService {

  void createTodayStats();

  List<EmployeeDistributionDto> getDepartmentDistribution(EmployeeState status, LocalDate statDate);

}
