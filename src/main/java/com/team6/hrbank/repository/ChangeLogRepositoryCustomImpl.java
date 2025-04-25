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

    // 기본 쿼리 시작
    JPAQuery<ChangeLog> query = queryFactory.selectFrom(changeLog)
        .where(buildConditions(condition, changeLog));

    // ***** refactor: 커서 + idAfter 복합 쿼리로 묶어서 사용하는 게 더욱 안전
    // 커서 기반 페이지네이션
    if (condition.cursor() != null) {
      // 'at' 필드일 때
      if ("at".equals(condition.sortField())) {
        Instant cursorInstant = Instant.parse(condition.cursor());

        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          query.where(changeLog.createdAt.lt(cursorInstant)); // createdAt 필드와 비교
        } else {
          query.where(changeLog.createdAt.gt(cursorInstant)); // createdAt 필드와 비교
        }
      }
      // 'ipAddress' 필드일 때
      else if ("ipAddress".equals(condition.sortField())) {
        String cursor = condition.cursor(); // cursor 값은 String 타입

        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          query.where(changeLog.ipAddress.lt(cursor)); // ipAddress 필드와 비교
        } else {
          query.where(changeLog.ipAddress.gt(cursor)); // ipAddress 필드와 비교
        }
      }
    }

    // idAfter 기반 페이지네이션
    if (condition.idAfter() != null && condition.idAfter() > 0) {
      query.where(changeLog.id.lt(condition.idAfter()));
    }

    // 정렬 처리
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