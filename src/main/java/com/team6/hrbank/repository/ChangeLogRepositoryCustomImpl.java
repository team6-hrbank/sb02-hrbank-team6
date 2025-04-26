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

    BooleanBuilder whereCondition = buildConditions(condition, changeLog);

    // 커서 조건 (페이지네이션)
    if (condition.cursor() != null) {
      String sortField = condition.sortField();
      String sortDirection = condition.sortDirection();
      Integer idAfter = condition.idAfter();

      BooleanBuilder cursorCondition = new BooleanBuilder();

      if ("at".equals(sortField)) {
        Instant cursorAt = Instant.parse(condition.cursor());

        if ("desc".equalsIgnoreCase(sortDirection)) {
          cursorCondition.and(
              changeLog.createdAt.lt(cursorAt)
                  .or(changeLog.createdAt.eq(cursorAt)
                      .and(changeLog.id.lt(idAfter)))
          );
        } else {
          cursorCondition.and(
              changeLog.createdAt.gt(cursorAt)
                  .or(changeLog.createdAt.eq(cursorAt)
                      .and(changeLog.id.gt(idAfter)))
          );
        }
      } else if ("ipAddress".equals(sortField)) {
        String cursorIp = condition.cursor();

        if ("desc".equalsIgnoreCase(sortDirection)) {
          cursorCondition.and(
              changeLog.ipAddress.lt(cursorIp)
                  .or(changeLog.ipAddress.eq(cursorIp)
                      .and(changeLog.id.lt(idAfter)))
          );
        } else {
          cursorCondition.and(
              changeLog.ipAddress.gt(cursorIp)
                  .or(changeLog.ipAddress.eq(cursorIp)
                      .and(changeLog.id.gt(idAfter)))
          );
        }
      }

      // 필터 조건 + 커서 조건 조합
      whereCondition.and(cursorCondition);
    }

    // 쿼리 생성
    JPAQuery<ChangeLog> query = queryFactory
        .selectFrom(changeLog)
        .where(whereCondition);

    // 정렬 조건 (id까지 포함한 복합 정렬)
    if ("at".equals(condition.sortField())) {
      if ("desc".equalsIgnoreCase(condition.sortDirection())) {
        query.orderBy(changeLog.createdAt.desc(), changeLog.id.desc());
      } else {
        query.orderBy(changeLog.createdAt.asc(), changeLog.id.asc());
      }
    } else if ("ipAddress".equals(condition.sortField())) {
      if ("desc".equalsIgnoreCase(condition.sortDirection())) {
        query.orderBy(changeLog.ipAddress.desc(), changeLog.id.desc());
      } else {
        query.orderBy(changeLog.ipAddress.asc(), changeLog.id.asc());
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