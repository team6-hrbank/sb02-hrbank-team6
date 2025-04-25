package com.team6.hrbank.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team6.hrbank.dto.changeLog.ChangeLogDto;
import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.dto.changeLog.DiffDto;
import com.team6.hrbank.entity.ChangeLog;
import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.ChangeLogDetailMapper;
import com.team6.hrbank.mapper.ChangeLogMapper;
import com.team6.hrbank.repository.ChangeLogRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeLogRepository changeLogRepository;
  private final ChangeLogMapper changeLogMapper;
  private final ChangeLogDetailMapper changeLogDetailMapper;
  private final JPAQueryFactory queryFactory;

  @Autowired
  public ChangeLogServiceImpl(ChangeLogRepository changeLogRepository,
      ChangeLogMapper changeLogMapper,
      ChangeLogDetailMapper changeLogDetailMapper,
      EntityManager entityManager) {
    this.changeLogRepository = changeLogRepository;
    this.changeLogMapper = changeLogMapper;
    this.changeLogDetailMapper = changeLogDetailMapper;
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Transactional
  @Override
  public ChangeLog create(Employee before, Employee after, String memo, String ipAddress) {
//    ChangeType type;
//    Employee employee;
//
//    if (before == null && after != null) {
//      type = ChangeType.CREATED;
//      employee = after;
//    } else if (before != null && after == null) {
//      type = ChangeType.DELETED;
//      employee = before;
//    } else if (before != null && after != null) {
//      type = ChangeType.UPDATED;
//      employee = before;
//    } else {
//      throw new RestException(ErrorCode.BAD_REQUEST);
//    }
//
//    ChangeLog changeLog = new ChangeLog(employee, type, memo, ipAddress);
//    createDetails(changeLog, before, after);
//    return changeLogRepository.save(changeLog);
    return null;
  }

  @Transactional(readOnly = true)
  @Override
  public CursorPageResponseChangeLogDto getChangeLogs(ChangeLogSearchCondition condition) {
    List<ChangeLog> changeLogs = changeLogRepository.findAllByFilter(condition, condition.size() + 1);
    boolean hasNext = changeLogs.size() > condition.size();

    List<ChangeLog> limitedChangeLogs = hasNext ? changeLogs.subList(0, condition.size()) : changeLogs;
    List<ChangeLogDto> contents = changeLogMapper.toDtoList(limitedChangeLogs);

    Long nextIdAfter = contents.isEmpty() ? null : contents.get(contents.size() - 1).id();
    String nextCursor = null;
    if (!contents.isEmpty()) {
      ChangeLogDto lastContent = contents.get(contents.size() - 1);
      if ("at".equals(condition.sortField())) {
        nextCursor = lastContent.at().toString();
      } else if ("ipAddress".equals(condition.sortField())) {
        nextCursor = lastContent.ipAddress();
      }
    }

    return new CursorPageResponseChangeLogDto(
        contents,
        nextCursor,
        nextIdAfter,
        condition.size(),
        contents.size(),
        hasNext
    );
  }

  @Transactional(readOnly = true)
  @Override
  public List<DiffDto> getChangeDetail(Long changeLogId) {
    ChangeLog changeLog = changeLogRepository.findById(changeLogId)
        .orElseThrow(() -> new RestException(ErrorCode.CHANGE_LOG_NOT_FOUND));

    return changeLogDetailMapper.toDtoList(changeLog.getDetails());
  }

  @Transactional(readOnly = true)
  @Override
  public Long getCount(Instant fromDate, Instant toDate) {
    // 날짜 validation 추가

    Instant now = Instant.now();
    Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);

    Instant from = (fromDate != null) ? fromDate : oneWeekAgo;
    Instant to = (toDate != null) ? toDate : now;

    return changeLogRepository.countByCreatedAtBetween(from, to);
  }


//  private List<ChangeLogDetail> createDetails(ChangeLog changeLog, Employee before, Employee after) {
//    addIfChanged(changeLog, "이름", get(before, Employee::getEmployeeName), get(after, Employee::getEmployeeName));
//    addIfChanged(changeLog, "이메일", get(before, Employee::getEmail), get(after, Employee::getEmail));
//    addIfChanged(changeLog, "직함", get(before, e -> String.valueOf(e.getEmployeePosition())), get(after, e -> String.valueOf(e.getEmployeePosition())));
//    addIfChanged(changeLog, "부서", get(before, e -> e.getDepartment() != null ? e.getDepartment().getDepartmentName() : null),
//        get(after, e -> e.getDepartment() != null ? e.getDepartment().getDepartmentName() : null));
//    addIfChanged(changeLog, "입사일", get(before, e -> e.getHireDate() != null ? e.getHireDate().toString() : null),
//        get(after, e -> e.getHireDate() != null ? e.getHireDate().toString() : null));
//    addIfChanged(changeLog, "상태", get(before, e -> e.getEmployeeState() != null ? e.getEmployeeState().name() : null),
//        get(after, e -> e.getEmployeeState() != null ? e.getEmployeeState().name() : null));
//
//    return changeLog.getDetails();
//  }
//
//  private String get(Employee employee, Function<Employee, String> getter) {
//    return employee == null ? null : getter.apply(employee);
//  }
//
//  private void addIfChanged(ChangeLog changeLog, String propertyName, String before, String after) {
//    if (!Objects.equals(before, after)) {
//      changeLog.addDetail(new ChangeLogDetail(propertyName, before, after));
//    }
//  }

}
