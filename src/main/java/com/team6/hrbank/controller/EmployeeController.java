package com.team6.hrbank.controller;

import com.team6.hrbank.dto.employee.*;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.service.EmployeeService;

import com.team6.hrbank.swagger.EmployeeApi;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(
            @RequestPart("employee") EmployeeCreateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage,
        HttpServletRequest httpRequest
    ) {
        EmployeeDto employeeDto = employeeService.create(request, profileImage, httpRequest.getRemoteAddr());
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findByEmployeeId(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.findById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping
    public ResponseEntity<CursorPageResponseEmployeeDto> searchEmployees(EmployeeSearchCondition condition) {
        CursorPageResponseEmployeeDto result = employeeService.searchEmployees(condition);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable Long id,
            @RequestPart("employee") EmployeeUpdateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage,
        HttpServletRequest httpRequest
    ) {
        EmployeeDto employeeDto = employeeService.update(id, request, profileImage, httpRequest.getRemoteAddr());
        return ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
        @PathVariable Long id,
        HttpServletRequest httpRequest) {
        employeeService.deleteById(id, httpRequest.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countEmployee(@RequestParam(required = false, defaultValue = "ACTIVE") EmployeeState status,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(employeeService.count(status, fromDate, toDate));
    }
}

