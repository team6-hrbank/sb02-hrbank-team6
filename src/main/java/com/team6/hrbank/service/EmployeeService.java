package com.team6.hrbank.service;

import com.team6.hrbank.dto.employee.EmployeeCreateRequest;
import com.team6.hrbank.dto.employee.EmployeeDto;
import com.team6.hrbank.dto.employee.EmployeeUpdateRequest;
import com.team6.hrbank.entity.EmployeeState;
import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    EmployeeDto create(EmployeeCreateRequest request, MultipartFile profileImage);
    EmployeeDto findById(Long id);
    List<EmployeeDto> findAll();
    EmployeeDto update(Long id,EmployeeUpdateRequest request, MultipartFile profileImage);
    void deleteById(Long id);
    long count(EmployeeState status, LocalDate fromDate, LocalDate toDate);
}
