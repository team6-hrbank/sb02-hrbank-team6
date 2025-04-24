package com.team6.hrbank.scheduler;


import com.team6.hrbank.service.EmployeeStatsService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeStatsScheduler {
  private final EmployeeStatsService employeeStatsService;

  // 오전 12시 5분에 배치를 날려서, 바로 직전 날의 데이터 반영
  @Scheduled(cron = "0 00 00 * * *")
  @SchedulerLock(name = "batchCreateEmployeeStats", lockAtMostFor = "10m")
  public void batchCreateEmployeeStats() {
    employeeStatsService.createTodayStats();
  }
}
