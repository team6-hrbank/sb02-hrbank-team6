package com.team6.hrbank.repository;

import com.team6.hrbank.dto.employee.EmployeeSearchCondition;
import com.team6.hrbank.entity.Employee;

import java.util.List;

public interface EmployeeRepositoryCustom {
    List<Employee> searchEmployeesWithCursor(EmployeeSearchCondition condition);
}
