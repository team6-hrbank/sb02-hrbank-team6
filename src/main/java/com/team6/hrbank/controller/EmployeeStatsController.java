package com.team6.hrbank.controller;

import com.team6.hrbank.dto.employeestats.EmployeeTrendDto;
import com.team6.hrbank.service.EmployeeStatsService;
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
public class EmployeeStatsController {
  private final EmployeeStatsService employeeStatsService;

  @GetMapping("/trend")
  public ResponseEntity<List<EmployeeTrendDto>> getEmployeeTrend(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false, defaultValue = "MONTH") String unit) {

    return ResponseEntity.ok(employeeStatsService.getEmployeeTrend(from,to,unit));
  }
}
