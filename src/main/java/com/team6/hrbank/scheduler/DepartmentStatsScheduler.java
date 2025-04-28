package com.team6.hrbank.scheduler;

import com.team6.hrbank.service.DepartmentStatsService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentStatsScheduler {
  private final DepartmentStatsService departmentStatsService;

  @Scheduled(cron = "0 22 * * * *")
  @SchedulerLock(name = "batchCreateDepartmentStats", lockAtMostFor = "10m")
  public void batchCreateDepartmentStats() {
    departmentStatsService.createTodayStats();

  }

}
