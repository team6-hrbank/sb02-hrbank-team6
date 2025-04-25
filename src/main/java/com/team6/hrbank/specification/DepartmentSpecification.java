// DepartmentSpecification.java
package com.team6.hrbank.specification;

import com.team6.hrbank.dto.department.DepartmentSearchCondition;
import com.team6.hrbank.entity.Department;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class DepartmentSpecification {
  public static Specification<Department> withConditions(DepartmentSearchCondition cond) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.hasText(cond.nameOrDescription())) {
        Predicate nameLike = cb.like(cb.lower(root.get("departmentName")), "%" + cond.nameOrDescription().toLowerCase() + "%");
        Predicate descLike = cb.like(cb.lower(root.get("departmentDescription")), "%" + cond.nameOrDescription().toLowerCase() + "%");
        predicates.add(cb.or(nameLike, descLike));
      }

      if ("name".equals(cond.sortField()) && cond.cursor() != null) {
        Predicate cursorPred = cond.sortDirection().equals("asc") ?
            cb.greaterThan(root.get("departmentName"), cond.cursor()) :
            cb.lessThan(root.get("departmentName"), cond.cursor());
        predicates.add(cursorPred);
      }

      if ("establishedDate".equals(cond.sortField()) && cond.cursor() != null) {
        Predicate cursorPred = cond.sortDirection().equals("asc") ?
            cb.greaterThan(root.get("departmentEstablishedDate"), LocalDate.parse(cond.cursor())) :
            cb.lessThan(root.get("departmentEstablishedDate"), LocalDate.parse(cond.cursor()));
        predicates.add(cursorPred);
      }

      if (cond.idAfter() != null) {
        Predicate idPred = cond.sortDirection().equals("asc") ?
            cb.greaterThan(root.get("id"), cond.idAfter()) :
            cb.lessThan(root.get("id"), cond.idAfter());
        predicates.add(idPred);
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
