package com.team6.hrbank.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team6.hrbank.dto.department.DepartmentSearchCondition;
import com.team6.hrbank.entity.Department;
import com.team6.hrbank.entity.QDepartment;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryCustomImpl implements DepartmentRepositoryCustom {

  public final JPAQueryFactory queryFactory;

  @Override
  public List<Department> searchDepartmentsWithCursor(DepartmentSearchCondition condition) {
    QDepartment department = QDepartment.department;
    BooleanBuilder whereCondition = buildConditions(condition, department);

    if (condition.cursor() != null) {
      BooleanBuilder cursorCondition = new BooleanBuilder();

      if ("name".equals(condition.sortField())) {
        String cursor = condition.cursor();
        Long idAfter = condition.idAfter();

        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          cursorCondition.and(department.departmentName.lt(cursor)
              .or(department.departmentName.eq(cursor).and(department.id.lt(idAfter))));
        } else {
          cursorCondition.and(department.departmentName.gt(cursor)
              .or(department.departmentName.eq(cursor).and(department.id.gt(idAfter))));
        }
      } else if ("establishedDate".equalsIgnoreCase(condition.sortField())) {
        LocalDate cursor = LocalDate.parse(condition.cursor());
        Long idAfter = condition.idAfter();

        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          cursorCondition.and(department.departmentEstablishedDate.lt(cursor)
              .or(department.departmentEstablishedDate.eq(cursor).and(department.id.lt(idAfter))));
        } else {
          cursorCondition.and(department.departmentEstablishedDate.gt(cursor)
              .or(department.departmentEstablishedDate.eq(cursor).and(department.id.gt(idAfter))));
        }
      }

      whereCondition.and(cursorCondition);
    }

    JPAQuery<Department> query = queryFactory
        .select(department)
        .from(department)
        .where(whereCondition);

    if ("name".equals(condition.sortField())) {
      query.orderBy(
          "desc".equalsIgnoreCase(condition.sortDirection())
              ? department.departmentName.desc() : department.departmentName.asc()
      );
    } else if ("establishedDate".equals(condition.sortField())) {
      query.orderBy(
          "desc".equalsIgnoreCase(condition.sortDirection())
              ? department.departmentEstablishedDate.desc()
              : department.departmentEstablishedDate.asc()
      );
    }

    return query.limit(condition.size() + 1).fetch();
  }

  public BooleanBuilder buildConditions(DepartmentSearchCondition condition,
      QDepartment department) {
    BooleanBuilder builder = new BooleanBuilder();

    if (StringUtils.hasText(condition.nameOrDescription())) {
      builder.and(department.departmentName.containsIgnoreCase(condition.nameOrDescription())
          .or(department.departmentDescription.containsIgnoreCase(condition.nameOrDescription())));
    }

    return builder;
  }
}

