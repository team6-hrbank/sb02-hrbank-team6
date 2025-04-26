package com.team6.hrbank.repository;

import com.team6.hrbank.dto.department.DepartmentSearchCondition;
import com.team6.hrbank.entity.Department;
import java.util.List;

public interface DepartmentRepositoryCustom {
  List<Department> searchDepartmentsWithCursor(DepartmentSearchCondition condition);
}
