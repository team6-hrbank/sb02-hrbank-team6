package com.team6.hrbank.dto.employee;

import com.team6.hrbank.entity.EmployeePosition;

import java.time.LocalDate;

public record EmployeeCreateRequest(
        String name,
        String email,
        Long departmentId,
        EmployeePosition position,
        LocalDate hireDate,
        String memo
) {
}
