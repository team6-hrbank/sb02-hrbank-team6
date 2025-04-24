package com.team6.hrbank.controller;

import com.team6.hrbank.dto.employeestats.EmployeeDistributionDto;
import com.team6.hrbank.dto.employeestats.EmployeeTrendDto;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.service.DepartmentStatsService;
import com.team6.hrbank.service.EmployeeStatsService;
import com.team6.hrbank.service.PositionStatsService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees/stats")
public class DashBoardController {

  private final EmployeeStatsService employeeStatsService;
  private final DepartmentStatsService departmentStatsService;
  private final PositionStatsService positionStatsService;

  @GetMapping("/trend")
  public ResponseEntity<List<EmployeeTrendDto>> getEmployeeTrend(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false, defaultValue = "MONTH") String unit) {

    return ResponseEntity.ok(employeeStatsService.getEmployeeTrend(from, to, unit));
  }

  @GetMapping("/distribution")
  public ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
      @RequestParam(required = false, defaultValue = "department") String groupBy,
      @RequestParam(required = false, defaultValue = "ACTIVE") EmployeeState status) {

    if (groupBy.equals("department")) {
      return ResponseEntity.ok(
          departmentStatsService.getDepartmentDistribution(status, LocalDate.now()));
    } else if (groupBy.equals("position")) {
      return ResponseEntity.ok(
          positionStatsService.getDepartmentDistribution(status, LocalDate.now()));
    } else {
      throw new RestException(ErrorCode.UNSUPPORTED_STATUS);
    }
  }
}
