package com.team6.hrbank.specification;

import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.entity.ChangeLog;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ChangeLogSpecifications {

  public static Specification<ChangeLog> withConditions(ChangeLogSearchCondition cond) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (cond.type() != null) {
        predicates.add(cb.equal(root.get("type"), cond.type()));
      }

      if (StringUtils.hasText(cond.employeeNumber())) {
        predicates.add(cb.like(root.get("employee").get("employeeNumber"), "%" + cond.employeeNumber() + "%"));
      }

      if (StringUtils.hasText(cond.memo())) {
        predicates.add(cb.like(cb.lower(root.get("memo")), "%" + cond.memo().toLowerCase() + "%"));
      }

      if (StringUtils.hasText(cond.ipAddress())) {
        predicates.add(cb.like(root.get("ipAddress"), "%" + cond.ipAddress() + "%"));
      }

      if (cond.atFrom() != null && cond.atTo() != null) {
        predicates.add(cb.between(root.get("createdAt"), cond.atFrom(), cond.atTo()));
      } else if (cond.atFrom() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), cond.atFrom()));
      } else if (cond.atTo() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), cond.atTo()));
      }

      if (cond.cursor() != null) {
        predicates.add(cb.lessThan(root.get("createdAt"), cond.cursor()));
      }

      if (cond.idAfter() != null && cond.idAfter() > 0) {
        predicates.add(cb.lessThan(root.get("id"), cond.idAfter()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

  }

}
