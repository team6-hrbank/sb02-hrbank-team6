package com.team6.hrbank.service.Impl;

import com.team6.hrbank.dto.employeestats.EmployeeDistributionDto;
import com.team6.hrbank.entity.Department;
import com.team6.hrbank.entity.DepartmentStats;
import com.team6.hrbank.entity.EmployeePosition;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.entity.EmployeeStats;
import com.team6.hrbank.entity.PositionStats;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.repository.DepartmentRepository;
import com.team6.hrbank.repository.DepartmentStatsRepository;
import com.team6.hrbank.repository.EmployeeQueryRepository;
import com.team6.hrbank.repository.EmployeeStatsRepository;
import com.team6.hrbank.repository.PositionStatsRepository;
import com.team6.hrbank.service.PositionStatsService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PositionStatsServiceImpl implements PositionStatsService {
  private final PositionStatsRepository positionStatsRepository;
  private final EmployeeStatsRepository employeeStatsRepository;
  private final EmployeeQueryRepository employeeQueryRepository;

  @Override
  @Transactional
  public void createTodayStats() {
    LocalDate current = LocalDate.now(ZoneId.of("Asia/Seoul"));

    List<EmployeePosition> positionList = List.of(EmployeePosition.values());

    List<PositionStats> newStatsList = new ArrayList<>();

    for (EmployeePosition position : positionList) {
      for (EmployeeState state : EmployeeState.values()) {
        if(positionStatsRepository.findByStatDateAndEmployeeStateAndPositionName(current, state, position).isPresent()) {
          throw new RestException(ErrorCode.DUPLICATE_POSTIONSTATS);
        }

        Optional<PositionStats> positionStats = positionStatsRepository.findByStatDateAndEmployeeStateAndPositionName(
            current.minusDays(1), state, position);
        long currentCount = employeeQueryRepository.countByEmployeePositionAndEmployeeState(
            position, state);
        long prevCount = positionStats.isPresent()
            ? positionStats.get().getEmployeeCount() : 0;
        long change = currentCount - prevCount;
        long joinedCount = change > 0 ? change : 0;
        long leftCount = change < 0 ? -change : 0;

        PositionStats stats = PositionStats.builder()
            .employeeState(state)
            .positionName(position)
            .leftCount(leftCount)
            .joinedCount(joinedCount)
            .employeeCount(currentCount)
            .statDate(current)
            .build();

        newStatsList.add(stats);
      }
    }
    positionStatsRepository.saveAll(newStatsList);
    log.info("총 {}개의 직무별 분포 데이터 생성 완료",newStatsList.size());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EmployeeDistributionDto> getDepartmentDistribution(EmployeeState status, LocalDate statDate) {
    List<PositionStats> positionStatsList = positionStatsRepository.findAllByStatDateAndEmployeeState(
        statDate, status);
    Optional<EmployeeStats> todayEmployeeStats = employeeStatsRepository.findByStatDate(statDate);

    long totalEmployeeCount;

    if (todayEmployeeStats.isEmpty() || positionStatsList.isEmpty()) {
      totalEmployeeCount = employeeStatsRepository.findByStatDate(statDate.minusDays(1))
          .orElseThrow(() -> {
            log.error("직무 별 직원 분포 조회 실패: {}", statDate);
            return new RestException(ErrorCode.EMPLOYEE_STATS_NOT_FOUND);
          })
          .getEmployeeCount();
      positionStatsList = positionStatsRepository.findAllByStatDateAndEmployeeState(
          statDate.minusDays(1), status);
    } else {
      totalEmployeeCount = todayEmployeeStats.get().getEmployeeCount();
    }

    List<EmployeeDistributionDto> distributionDtoList = positionStatsList.stream()
        .map(positionStats -> {
          long count = positionStats.getEmployeeCount();
          double percentage = (double) count / totalEmployeeCount * 100;
          percentage = Math.round(percentage * 10.0) / 10.0;
          return new EmployeeDistributionDto(positionStats.getPositionName().name(), count,
              percentage);
        })
        .toList();

    return distributionDtoList;
  }

}
