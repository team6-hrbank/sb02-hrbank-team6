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

  @Scheduled(cron = "0 50 08 * * *")
  @SchedulerLock(name = "batchCreateEmployeeStats", lockAtMostFor = "10m")
  public void batchCreateEmployeeStats() {
    employeeStatsService.createTodayStats();
  }
}
