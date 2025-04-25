package com.team6.hrbank.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team6.hrbank.dto.employee.EmployeeSearchCondition;
import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.entity.QDepartment;
import com.team6.hrbank.entity.QEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Employee> searchEmployeesWithCursor(EmployeeSearchCondition condition) {
        QEmployee employee = QEmployee.employee;
        QDepartment department = QDepartment.department;

        JPAQuery<Employee> query = queryFactory
                .selectFrom(employee)
                .leftJoin(employee.department, department).fetchJoin()
                .where(buildConditions(condition, employee, department));

        if (condition.cursor() != null) {
            switch (condition.sortField()) {
                case "employeeNumber" -> {
                    if ("desc".equalsIgnoreCase(condition.sortDirection()))
                        query.where(employee.employeeNumber.lt(condition.cursor()));
                    else
                        query.where(employee.employeeNumber.gt(condition.cursor()));
                }
                case "hireDate" -> {
                    LocalDate cursorDate = LocalDate.parse(condition.cursor());
                    if ("desc".equalsIgnoreCase(condition.sortDirection()))
                        query.where(employee.hireDate.lt(cursorDate));
                    else
                        query.where(employee.hireDate.gt(cursorDate));
                }
                default -> {
                    if ("desc".equalsIgnoreCase(condition.sortDirection()))
                        query.where(employee.employeeName.lt(condition.cursor()));
                    else
                        query.where(employee.employeeName.gt(condition.cursor()));
                }
            }
        }
        if (condition.idAfter() != null) {
            query.where(employee.id.gt(condition.idAfter()));
        }

        switch (condition.sortField()) {
            case "employeeNumber" ->
                    query.orderBy("desc".equalsIgnoreCase(condition.sortDirection()) ? employee.employeeNumber.desc() : employee.employeeNumber.asc());
            case "hireDate" ->
                    query.orderBy("desc".equalsIgnoreCase(condition.sortDirection()) ? employee.hireDate.desc() : employee.hireDate.asc());
            default ->
                    query.orderBy("desc".equalsIgnoreCase(condition.sortDirection()) ? employee.employeeName.desc() : employee.employeeName.asc());
        }

        return query.limit(condition.size() != null ? condition.size() : 10).fetch();
    }

    private BooleanBuilder buildConditions(EmployeeSearchCondition condition, QEmployee employee, QDepartment department) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(condition.nameOrEmail())) {
            builder.and(employee.employeeName.containsIgnoreCase(condition.nameOrEmail())
                    .or(employee.email.containsIgnoreCase(condition.nameOrEmail())));
        }
        if (StringUtils.hasText(condition.departmentName())) {
            builder.and(department.departmentName.containsIgnoreCase(condition.departmentName()));
        }
        if (StringUtils.hasText(condition.position())) {
            builder.and(employee.employeePosition.stringValue().eq(condition.position()));
        }
        if (StringUtils.hasText(condition.employeeNumber())) {
            builder.and(employee.employeeNumber.containsIgnoreCase(condition.employeeNumber()));
        }
        if (condition.hireDateFrom() != null) {
            builder.and(employee.hireDate.goe(condition.hireDateFrom()));
        }
        if (condition.hireDateTo() != null) {
            builder.and(employee.hireDate.loe(condition.hireDateTo()));
        }
        if (StringUtils.hasText(condition.status())) {
            builder.and(employee.employeeState.stringValue().eq(condition.status()));
        }

        return builder;
    }
}
