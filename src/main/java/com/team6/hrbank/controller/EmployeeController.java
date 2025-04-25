package com.team6.hrbank.controller;

import com.team6.hrbank.dto.employee.EmployeeCreateRequest;
import com.team6.hrbank.dto.employee.EmployeeDto;
import com.team6.hrbank.dto.employee.EmployeeUpdateRequest;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.service.EmployeeService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;

  @PostMapping
  public ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("employee") EmployeeCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage
  ) {
    EmployeeDto employeeDto = employeeService.create(request, profileImage);
    return ResponseEntity.ok(employeeDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> findByEmployeeId(@PathVariable Long id) {
    EmployeeDto employeeDto = employeeService.findById(id);
    return ResponseEntity.ok(employeeDto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @RequestPart("employee") EmployeeUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage
  ) {
    EmployeeDto employeeDto = employeeService.update(id, request, profileImage);
    return ResponseEntity.ok(employeeDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/count")
  public ResponseEntity<Long> countEmployee(@RequestParam(required = false, defaultValue = "ACTIVE") EmployeeState status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
    return ResponseEntity.ok(employeeService.count(status,fromDate,toDate));
  }
}

