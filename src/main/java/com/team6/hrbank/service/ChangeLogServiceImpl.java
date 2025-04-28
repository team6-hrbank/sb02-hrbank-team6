package com.team6.hrbank.service;

import com.team6.hrbank.dto.changeLog.ChangeLogDto;
import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.dto.changeLog.DiffDto;
import com.team6.hrbank.entity.ChangeLog;
import com.team6.hrbank.entity.ChangeLogDetail;
import com.team6.hrbank.entity.ChangeType;
import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.ChangeLogDetailMapper;
import com.team6.hrbank.mapper.ChangeLogMapper;
import com.team6.hrbank.repository.ChangeLogRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeLogRepository changeLogRepository;
  private final ChangeLogMapper changeLogMapper;
  private final ChangeLogDetailMapper changeLogDetailMapper;

  @Transactional
  @Override
  public ChangeLog create(Employee before, Employee after, String memo, String ipAddress) {
    ChangeType type;
    Employee employee;

    if (before == null && after != null) {
      type = ChangeType.CREATED;
      employee = after;
    } else if (before != null && after == null) {
      type = ChangeType.DELETED;
      employee = before;
    } else if (before != null) {
      type = ChangeType.UPDATED;
      employee = before;
    } else {
      throw new RestException(ErrorCode.BAD_REQUEST);
    }

    ChangeLog changeLog = new ChangeLog(employee, type, memo, ipAddress);
    createDetails(changeLog, before, after);
    return changeLogRepository.save(changeLog);
  }

  @Transactional(readOnly = true)
  @Override
  public CursorPageResponseChangeLogDto getChangeLogs(ChangeLogSearchCondition condition) {
    List<ChangeLog> changeLogs = changeLogRepository.findAllByFilter(condition, condition.size() + 1);

    boolean hasNext = changeLogs.size() > condition.size();
    List<ChangeLog> limitedChangeLogs = hasNext ? changeLogs.subList(0, condition.size()) : changeLogs;

    List<ChangeLogDto> contents = changeLogMapper.toDtoList(limitedChangeLogs);

    Long nextIdAfter = null;
    String nextCursor = null;
    if (!contents.isEmpty()) {
      ChangeLogDto lastContent = contents.get(contents.size() - 1);
      if ("at".equals(condition.sortField())) {
        nextCursor = lastContent.at().toString();
        nextIdAfter = lastContent.id();
      } else if ("ipAddress".equals(condition.sortField())) {
        nextCursor = lastContent.ipAddress();
        nextIdAfter = lastContent.id();
      }
    }

    long totalElements = changeLogRepository.countByFilter(condition);

    return new CursorPageResponseChangeLogDto(
        contents,
        nextCursor,
        nextIdAfter,
        condition.size(),
        totalElements,
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
  public long getCount(Instant fromDate, Instant toDate) {
    Instant now = Instant.now();
    Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);

    Instant from = (fromDate != null) ? fromDate : oneWeekAgo;
    Instant to = (toDate != null) ? toDate : now;

    if (from.isAfter(to)) {
      throw new RestException(ErrorCode.INVALID_DATE_RANGE);
    }

    return changeLogRepository.countByCreatedAtBetween(from, to);
  }


  private void createDetails(ChangeLog changeLog, Employee before, Employee after) {
    addIfChanged(changeLog, "이름", get(before, Employee::getEmployeeName), get(after, Employee::getEmployeeName));
    addIfChanged(changeLog, "이메일", get(before, Employee::getEmail), get(after, Employee::getEmail));
    addIfChanged(changeLog, "직함", get(before, e -> e.getEmployeePosition() != null ? e.getEmployeePosition().getLabel() : null),
        get(after, e -> e.getEmployeePosition() != null ? e.getEmployeePosition().getLabel() : null));
    addIfChanged(changeLog, "부서", get(before, e -> e.getDepartment() != null ? e.getDepartment().getDepartmentName() : null),
        get(after, e -> e.getDepartment() != null ? e.getDepartment().getDepartmentName() : null));
    addIfChanged(changeLog, "입사일", get(before, e -> String.valueOf(e.getHireDate())),
        get(after, e -> String.valueOf(e.getHireDate())));
    addIfChanged(changeLog, "상태", get(before, e -> e.getEmployeeState() != null ? e.getEmployeeState().getLabel() : null),
        get(after, e -> e.getEmployeeState() != null ? e.getEmployeeState().getLabel() : null));
  }

  private void addIfChanged(ChangeLog changeLog, String propertyName, String before, String after) {
    if (!Objects.equals(before, after)) {
      changeLog.addDetail(new ChangeLogDetail(propertyName, before, after));
    }
  }

  private String get(Employee employee, Function<Employee, String> getter) {
    return employee == null ? null : getter.apply(employee);
  }


}
