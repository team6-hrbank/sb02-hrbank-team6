package com.team6.hrbank.service.Impl;

import com.team6.hrbank.dto.employeestats.EmployeeDistributionDto;
import com.team6.hrbank.entity.Department;
import com.team6.hrbank.entity.DepartmentStats;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.entity.EmployeeStats;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.repository.DepartmentRepository;
import com.team6.hrbank.repository.DepartmentStatsRepository;
import com.team6.hrbank.repository.EmployeeQueryRepository;
import com.team6.hrbank.repository.EmployeeStatsRepository;
import com.team6.hrbank.service.DepartmentStatsService;
import java.time.LocalDate;
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
public class DepartmentStatsServiceImpl implements DepartmentStatsService {

  private final DepartmentStatsRepository departmentStatsRepository;
  private final EmployeeStatsRepository employeeStatsRepository;
  private final EmployeeQueryRepository employeeQueryRepository;
  private final DepartmentRepository departmentRepository;

  @Override
  @Transactional
  public void createTodayStats() {
    // Department 별로 EmployeeState에 따라 3가지의 통계 테이블이 존재
    LocalDate current = LocalDate.now();

    List<Department> departmentList = departmentRepository.findAll();

    List<DepartmentStats> newStatsList = new ArrayList<>();

    for (Department department : departmentList) {
      for (EmployeeState state : EmployeeState.values()) {
        Optional<DepartmentStats> departmentStats = departmentStatsRepository.findByStatDateAndEmployeeStateAndDepartmentName(
            current.minusDays(1), state, department.getDepartmentName());
        long currentCount = employeeQueryRepository.countByDepartmentIdAndEmployeeState(
            department.getId(), state);
        long prevCount = departmentStats.isPresent()
            ? departmentStats.get().getEmployeeCount() : 0;
        long change = currentCount - prevCount;
        long joinedCount = change > 0 ? change : 0;
        long leftCount = change < 0 ? -change : 0;

        DepartmentStats stats = DepartmentStats.builder()
            .departmentName(department.getDepartmentName())
            .employeeState(state)
            .statDate(current)
            .employeeCount(currentCount)
            .joinedCount(joinedCount)
            .leftCount(leftCount)
            .build();

        newStatsList.add(stats);
      }
    }
    // 하나씩 save하는 것보다 saveAll이 성능이 더 좋음
    // saveAll() 같은 경우는 Bean 객체의 내부함수를 호출하기 때문에 save() 호출마다 트랜잭션이 생성되거나 참여하는 프록시 로직을 전혀 타지 않고, 단순한 메소드 호출만하기 때문에 프록시 진입 비용이 들지 않는다.
    departmentStatsRepository.saveAll(newStatsList);
    log.info("총 {}개의 부서별 분포 데이터 생성 완료",newStatsList.size());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EmployeeDistributionDto> getDepartmentDistribution(EmployeeState status, LocalDate statDate) {
    List<DepartmentStats> departmentStatsList = departmentStatsRepository.findAllByStatDateAndEmployeeState(
        statDate, status);
    Optional<EmployeeStats> todayEmployeeStats = employeeStatsRepository.findByStatDate(statDate);

    long totalEmployeeCount;

    // 오늘 EmployeeStats나 departmentStats가 아직 배치 등록이 안되었다면, 잠깐 동안 이전의 데이터 조회
    if (todayEmployeeStats.isEmpty() || departmentStatsList.isEmpty()) {
      totalEmployeeCount = employeeStatsRepository.findByStatDate(statDate.minusDays(1))
          .orElseThrow(() -> {
            // 이전 데이터도 없다면, 예외 상황
            log.error("부서 별 직원 분포 조회 실패: {}", statDate);
            return new RestException(ErrorCode.EMPLOYEE_STATS_NOT_FOUND);
          })
          .getEmployeeCount();
      departmentStatsList = departmentStatsRepository.findAllByStatDateAndEmployeeState(
          statDate.minusDays(1), status);
    } else {
      totalEmployeeCount = todayEmployeeStats.get().getEmployeeCount();
    }

    List<EmployeeDistributionDto> distributionDtoList = departmentStatsList.stream()
        .map(departmentStats -> {
          long count = departmentStats.getEmployeeCount();
          double percentage = (double) count / totalEmployeeCount * 100;
          percentage = Math.round(percentage * 10.0) / 10.0;
          return new EmployeeDistributionDto(departmentStats.getDepartmentName(), count,
              percentage);
        })
        .toList();

    return distributionDtoList;
  }
}
