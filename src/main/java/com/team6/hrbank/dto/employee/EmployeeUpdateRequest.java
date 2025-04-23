package com.team6.hrbank.dto.employee;

import com.team6.hrbank.entity.EmployeePosition;
import com.team6.hrbank.entity.EmployeeState;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
        String name,
        String email,
        Long departmentId,
        EmployeePosition position,
        LocalDate hireDate,
        EmployeeState status,
        String memo
) {
}
