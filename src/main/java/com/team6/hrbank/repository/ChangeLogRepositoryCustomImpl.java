package com.team6.hrbank.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.entity.ChangeLog;
import com.team6.hrbank.entity.QChangeLog;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ChangeLogRepositoryCustomImpl implements ChangeLogRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ChangeLogRepositoryCustomImpl(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public List<ChangeLog> findAllByFilter(ChangeLogSearchCondition condition, int limit) {
    QChangeLog changeLog = QChangeLog.changeLog;

    JPAQuery<ChangeLog> query = queryFactory.selectFrom(changeLog)
        .where(buildConditions(condition, changeLog));

    // ***** refactor: 커서 + idAfter 복합 쿼리로 묶어서 사용하는 게 더욱 안전
    if (condition.cursor() != null) {
      if ("at".equals(condition.sortField())) {
        Instant cursorInstant = Instant.parse(condition.cursor());

        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          query.where(changeLog.createdAt.lt(cursorInstant));
        } else {
          query.where(changeLog.createdAt.gt(cursorInstant));
        }
      }
      else if ("ipAddress".equals(condition.sortField())) {
        String cursor = condition.cursor();

        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          query.where(changeLog.ipAddress.lt(cursor));
        } else {
          query.where(changeLog.ipAddress.gt(cursor));
        }
      }
    }

    // ***** refactor: idAfter가 ipAddress 기준으로 정렬 됐을 때 의미가 없는 것 아닌지?
    if ("at".equals(condition.sortField()) && condition.idAfter() != null && condition.idAfter() > 0) {
      if ("desc".equalsIgnoreCase(condition.sortDirection())) {
        query.where(changeLog.id.lt(condition.idAfter()));
      } else {
        query.where(changeLog.id.gt(condition.idAfter()));
      }
    }

    if ("at".equals(condition.sortField())) {
      if ("desc".equalsIgnoreCase(condition.sortDirection())) {
        query.orderBy(changeLog.createdAt.desc());
      } else {
        query.orderBy(changeLog.createdAt.asc());
      }
    } else if ("ipAddress".equals(condition.sortField())) {
      if ("desc".equalsIgnoreCase(condition.sortDirection())) {
        query.orderBy(changeLog.ipAddress.desc());
      } else {
        query.orderBy(changeLog.ipAddress.asc());
      }
    }

    return query.limit(limit).fetch();
  }

  private BooleanBuilder buildConditions(ChangeLogSearchCondition condition, QChangeLog changeLog) {
    BooleanBuilder builder = new BooleanBuilder();

    if (condition.type() != null) {
      builder.and(changeLog.type.eq(condition.type()));
    }

    if (StringUtils.hasText(condition.employeeNumber())) {
      builder.and(changeLog.employee.employeeNumber.like("%" + condition.employeeNumber() + "%"));
    }

    if (StringUtils.hasText(condition.memo())) {
      builder.and(changeLog.memo.likeIgnoreCase("%" + condition.memo() + "%"));
    }

    if (StringUtils.hasText(condition.ipAddress())) {
      builder.and(changeLog.ipAddress.like("%" + condition.ipAddress() + "%"));
    }

    if (condition.atFrom() != null && condition.atTo() != null) {
      builder.and(changeLog.createdAt.between(condition.atFrom(), condition.atTo()));
    } else if (condition.atFrom() != null) {
      builder.and(changeLog.createdAt.goe(condition.atFrom()));
    } else if (condition.atTo() != null) {
      builder.and(changeLog.createdAt.loe(condition.atTo()));
    }

    return builder;
  }

}