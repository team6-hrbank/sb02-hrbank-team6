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

        BooleanBuilder whereCondition = buildConditions(condition, employee, department);

        if (condition.cursor() != null) {
            BooleanBuilder cursorCondition = new BooleanBuilder();
            switch (condition.sortField()) {
                case "employeeNumber" -> {
                    if ("desc".equalsIgnoreCase(condition.sortDirection())) {
                        cursorCondition.and(
                                employee.employeeNumber.lt(condition.cursor())
                                        .or(employee.employeeNumber.eq(condition.cursor())
                                                .and(employee.id.lt(condition.idAfter())))
                        );
                    } else {
                        cursorCondition.and(
                                employee.employeeNumber.gt(condition.cursor())
                                        .or(employee.employeeNumber.eq(condition.cursor()))
                                        .and(employee.id.gt(condition.idAfter()))
                        );
                    }
                }
                case "hireDate" -> {
                    LocalDate cursorDate = LocalDate.parse(condition.cursor());
                    if ("desc".equalsIgnoreCase(condition.sortDirection())) {
                        cursorCondition.and(
                                employee.hireDate.lt(cursorDate)
                                        .or(employee.hireDate.eq(cursorDate)
                                                .and(employee.id.lt(condition.idAfter())))
                        );
                    } else {
                        cursorCondition.and(
                                employee.hireDate.gt(cursorDate)
                                        .or(employee.hireDate.eq(cursorDate)
                                                .and(employee.id.gt(condition.idAfter())))
                        );
                    }
                }
                case "name" -> {
                    if ("desc".equalsIgnoreCase(condition.sortDirection())) {
                        cursorCondition.and(
                                employee.employeeName.lt(condition.cursor())
                                        .or(employee.employeeName.eq(condition.cursor())
                                                .and(employee.id.lt(condition.idAfter())))
                        );
                    } else {
                        cursorCondition.and(
                                employee.employeeName.gt(condition.cursor())
                                        .or(employee.employeeName.eq(condition.cursor())
                                                .and(employee.id.gt(condition.idAfter())))
                        );
                    }
                }
            }
            whereCondition.and(cursorCondition);
        }
        JPAQuery<Employee> query = queryFactory
                .selectFrom(employee)
                .leftJoin(employee.department, department).fetchJoin()
                .where(whereCondition);

        switch (condition.sortField()) {
            case "employeeNumber" -> {
                if ("desc".equalsIgnoreCase(condition.sortDirection())) {
                    query.orderBy(employee.employeeNumber.desc(), employee.id.desc());
                } else {
                    query.orderBy(employee.employeeNumber.asc(), employee.id.asc());
                }
            }
            case "hireDate" -> {
                if ("desc".equalsIgnoreCase(condition.sortDirection())) {
                    query.orderBy(employee.hireDate.desc(), employee.id.desc());
                } else {
                    query.orderBy(employee.hireDate.asc(), employee.id.asc());
                }
            }
            case "name" -> {
                if ("desc".equalsIgnoreCase(condition.sortDirection())) {
                    query.orderBy(employee.employeeName.desc(), employee.id.desc());
                } else {
                    query.orderBy(employee.employeeName.asc(), employee.id.asc());
                }
            }
        }

        return query.limit(condition.size() != null ? condition.size() : 10).fetch();
    }

    private BooleanBuilder buildConditions(EmployeeSearchCondition condition, QEmployee employee, QDepartment
            department) {
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
