package com.team6.hrbank.service;

import com.team6.hrbank.dto.employee.*;
import com.team6.hrbank.entity.EmployeeState;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    EmployeeDto create(EmployeeCreateRequest request, MultipartFile profileImage);

    EmployeeDto findById(Long id);

    CursorPageResponseEmployeeDto searchEmployees(EmployeeSearchCondition condition);

    EmployeeDto update(Long id, EmployeeUpdateRequest request, MultipartFile profileImage);

    void deleteById(Long id);

    long count(EmployeeState status, LocalDate fromDate, LocalDate toDate);
}
