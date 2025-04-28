package com.team6.hrbank.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team6.hrbank.dto.backup.BackupSearchCondition;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupHistory;
import com.team6.hrbank.entity.BackupStatus;
import com.team6.hrbank.entity.QBackupHistory;
import com.team6.hrbank.mapper.BackupMapper;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class BackupHistoryRepositoryCustomImpl implements BackupHistoryRepositoryCustom {
  private final JPAQueryFactory queryFactory;
  private final BackupMapper backupMapper;

  @Override
  public List<BackupDto> searchBackupHistoryWithCursor(BackupSearchCondition condition) {
    QBackupHistory backup = QBackupHistory.backupHistory;
    BooleanBuilder builder = buildConditions(condition, backup);

    // 커서 기반 조건
    if (condition.cursor() != null) {
      BooleanBuilder cursorBuilder = new BooleanBuilder();

      // 정렬 조건에 따라 순서 다르게 처리
      if ("startedAt".equalsIgnoreCase(condition.sortField())) {
        Instant cursorInstant = Instant.parse(condition.cursor());
        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          cursorBuilder.and(backup.startedAt.lt(cursorInstant)
              .or(backup.startedAt.eq(cursorInstant).and(backup.id.lt(condition.idAfter()))));
        } else {
          cursorBuilder.and(backup.startedAt.gt(cursorInstant)
              .or(backup.startedAt.eq(cursorInstant).and(backup.id.gt(condition.idAfter()))));
        }
        // 기본 조건
      } else if ("endedAt".equalsIgnoreCase(condition.sortField())) {
        Instant cursorInstant = Instant.parse(condition.cursor());
        if ("desc".equalsIgnoreCase(condition.sortDirection())) {
          cursorBuilder.and(backup.endedAt.lt(cursorInstant)
              .or(backup.endedAt.eq(cursorInstant).and(backup.id.lt(condition.idAfter()))));
        } else {
          cursorBuilder.and(backup.endedAt.gt(cursorInstant)
              .or(backup.endedAt.eq(cursorInstant).and(backup.id.gt(condition.idAfter()))));
        }
      }

      builder.and(cursorBuilder);
    }

    var query = queryFactory
        .selectFrom(backup)
        .where(builder);

    // 정렬 기준
    if ("startedAt".equalsIgnoreCase(condition.sortField())) {
      query.orderBy("desc".equalsIgnoreCase(condition.sortDirection())
          ? backup.startedAt.desc()
          : backup.startedAt.asc());
    } else if ("endedAt".equalsIgnoreCase(condition.sortField())) {
      query.orderBy("desc".equalsIgnoreCase(condition.sortDirection())
          ? backup.endedAt.desc()
          : backup.endedAt.asc());
    }

    // 1개 더 가져와서 hasNext 판별
    var result = query.limit(condition.size() + 1).fetch();

    // DTO 변환
    return result.stream()
        .map(backupMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public Long countBySearchCondition(BackupSearchCondition condition) {
    QBackupHistory backup = QBackupHistory.backupHistory;
    BooleanBuilder builder = buildConditions(condition, backup);

    return queryFactory
        .select(backup.count())
        .from(backup)
        .where(builder)
        .fetchOne();
  }

  private BooleanBuilder buildConditions(BackupSearchCondition condition, QBackupHistory backup) {
    BooleanBuilder builder = new BooleanBuilder();

    if (StringUtils.hasText(condition.worker())) {
      builder.and(backup.operator.containsIgnoreCase(condition.worker()));
    }

    if (StringUtils.hasText(condition.status())) {
      builder.and(backup.status.eq(BackupStatus.valueOf(condition.status())));
    }

    if (StringUtils.hasText(condition.startedAtFrom()) && StringUtils.hasText(condition.startedAtTo())) {
      Instant from = Instant.parse(condition.startedAtFrom());
      Instant to = Instant.parse(condition.startedAtTo());
      builder.and(backup.startedAt.between(from, to));
    }

    return builder;
  }
}
