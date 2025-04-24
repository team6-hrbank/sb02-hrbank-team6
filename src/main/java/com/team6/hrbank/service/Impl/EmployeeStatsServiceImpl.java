package com.team6.hrbank.service.Impl;

import com.team6.hrbank.dto.employeestats.EmployeeTrendDto;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.entity.EmployeeStats;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.EmployeeStatsMapper;
import com.team6.hrbank.repository.EmployeeQueryRepository;
import com.team6.hrbank.repository.EmployeeStatsRepository;
import com.team6.hrbank.service.EmployeeStatsService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeStatsServiceImpl implements EmployeeStatsService {

  private final EmployeeStatsRepository employeeStatsRepository;
  private final EmployeeQueryRepository employeeQueryRepository;
  private final EmployeeStatsMapper employeeStatsMapper;


  @Override
  @Transactional
  public void createTodayStats() {
    LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
    LocalDate prevDate = currentDate.minusDays(1);
    long currentCount = employeeQueryRepository.countByEmployeeStateNot(EmployeeState.RESIGNED);
    if (employeeStatsRepository.existsByStatDate(currentDate)) {
      log.error("중복 직원 통계 데이터 생성 시도: {}", currentDate);
      throw new RestException(ErrorCode.DUPLICATE_EMPLOYEESTATS);
    }

    if (employeeStatsRepository.findByStatDate(prevDate).isEmpty()) {
      long joinedCount = currentCount;
      long leftCount = 0;
      EmployeeStats currentEmployeeStats = createEmployeeStats(currentDate, currentCount,
          joinedCount, leftCount);

      saveEmployeeStats(currentEmployeeStats);
      return;
    }

    long prevCount = employeeStatsRepository.findByStatDate(prevDate).get().getEmployeeCount();
    long change = currentCount - prevCount;
    long joinedCount = change > 0 ? change : 0;
    long leftCount = change < 0 ? -change : 0;

    EmployeeStats currentEmployeeStats = createEmployeeStats(currentDate, currentCount, joinedCount,
        leftCount);

    saveEmployeeStats(currentEmployeeStats);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EmployeeTrendDto> getEmployeeTrend(LocalDate from, LocalDate to, String unit) {
    // null일 경우 기본값 지정
    if (to == null) {
      to = LocalDate.now();
    }

    String unitUpper = unit.toUpperCase();

    if (from == null) {
      from = switch (unitUpper) {
        case "DAY" -> to.minusDays(12);
        case "WEEK" -> to.minusWeeks(12);
        case "MONTH" -> to.minusMonths(12);
        case "QUARTER" -> to.minusMonths(12 * 3);
        case "YEAR" -> to.minusYears(12);
        default -> to.minusMonths(12);
      };
    }

    // 프론트단에서 넘어온 Unit을 기준으로 statDate를 뽑아서 저장
    List<LocalDate> statDateList =
        switch (unitUpper) {
          case "DAY" -> getDailyStatDateList(from, to);
          case "WEEK" -> getWeeklyStatDateList(from, to);
          case "MONTH" -> getMonthlyStatDateList(from, to);
          case "QUARTER" -> getQuarterStatDateList(from, to);
          case "YEAR" -> getYearlyStatDateList(from, to);
          default -> throw new RestException(ErrorCode.UNSUPPORTED_UNIT);
        };

    List<EmployeeStats> employeeStatsList = employeeStatsRepository.findAllByStatDateIn(statDateList);

    // EmployeeStats 데이터를 change, changeRate를 계산하여 dto 형식에 맞춰 반환
    List<EmployeeTrendDto> employeeTrendDtoList = new ArrayList<>();
    for (int i = 0; i < employeeStatsList.size(); i++) {
      long change = 0;
      double changeRate = 0.0;

      if (i != 0) {
        long currentCount = employeeStatsList.get(i).getEmployeeCount();
        long prevCount = employeeStatsList.get(i - 1).getEmployeeCount();
        change = currentCount - prevCount;

        if (prevCount != 0) {
          changeRate = (double) change / prevCount * 100;
          changeRate = Math.round(changeRate * 10.0) / 10.0;
        }
      }
      employeeTrendDtoList.add(
          employeeStatsMapper.toEmployeeTrendDto(employeeStatsList.get(i), change, changeRate));
    }

    return employeeTrendDtoList;
  }


  private List<LocalDate> getDailyStatDateList(LocalDate from, LocalDate to) {
    List<LocalDate> statDateList = new ArrayList<>();
    LocalDate current = from;

    while (!current.isAfter(to)) {
      statDateList.add(current);
      current = current.plusDays(1);
    }

    return statDateList;
  }

  private List<LocalDate> getWeeklyStatDateList(LocalDate from, LocalDate to) {
    List<LocalDate> statDateList = new ArrayList<>();
    LocalDate current = from;

    while (!current.isAfter(to)) {
      statDateList.add(current);
      current = current.plusWeeks(1);
    }

    return statDateList;
  }

  private List<LocalDate> getMonthlyStatDateList(LocalDate from, LocalDate to) {
    List<LocalDate> statDateList = new ArrayList<>();
    LocalDate current = from;

    while (!current.isAfter(to)) {
      statDateList.add(current);
      current = current.plusMonths(1);
    }

    return statDateList;
  }

  private List<LocalDate> getQuarterStatDateList(LocalDate from, LocalDate to) {
    List<LocalDate> statDateList = new ArrayList<>();
    LocalDate current = from;

    while (!current.isAfter(to)) {
      statDateList.add(current);
      current = current.plusMonths(3);
    }

    return statDateList;
  }

  private List<LocalDate> getYearlyStatDateList(LocalDate from, LocalDate to) {
    List<LocalDate> statDateList = new ArrayList<>();
    LocalDate current = from;

    while (!current.isAfter(to)) {
      statDateList.add(current);
      current = current.plusYears(1);
    }

    return statDateList;
  }


  private void saveEmployeeStats(EmployeeStats stats) {
    try {
      // Unique 제약조건으로 인한 예외를 잡기 위해 바로 Flush
      employeeStatsRepository.saveAndFlush(stats);
    } catch (DataIntegrityViolationException e) {
      log.error("중복 직원 통계 데이터 생성 시도: {}", stats.getStatDate());
      throw new RestException(ErrorCode.DUPLICATE_EMPLOYEESTATS);
    }
  }

  private EmployeeStats createEmployeeStats(LocalDate currentDate, long currentCount,
      long joinedCount, long leftCount) {
    return EmployeeStats.builder()
        .statDate(currentDate)
        .createdAt(Instant.now())
        .employeeCount(currentCount)
        .joinedCount(joinedCount)
        .leftCount(leftCount)
        .build();
  }

}

