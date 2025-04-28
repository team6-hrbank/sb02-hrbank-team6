package com.team6.hrbank.dto.employee;

import com.team6.hrbank.entity.EmployeeState;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        LocalDate hireDate,
        EmployeeState status,
        String memo
) {
}
