package com.team6.hrbank.controller;

import com.team6.hrbank.dto.department.CursorPageResponseDepartmentDto;
import com.team6.hrbank.dto.department.DepartmentCreateRequest;
import com.team6.hrbank.dto.department.DepartmentDto;
import com.team6.hrbank.dto.department.DepartmentSearchCondition;
import com.team6.hrbank.dto.department.DepartmentUpdateRequest;
import com.team6.hrbank.service.DepartmentService;
import com.team6.hrbank.swagger.DepartmentApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController implements DepartmentApi {
  private final DepartmentService departmentService;

  @PostMapping
  public ResponseEntity<DepartmentDto> create(@Valid @RequestBody DepartmentCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.create(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> find(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    departmentService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseDepartmentDto> getDepartments(DepartmentSearchCondition condition) {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.getDepartments(condition));
  }

}
