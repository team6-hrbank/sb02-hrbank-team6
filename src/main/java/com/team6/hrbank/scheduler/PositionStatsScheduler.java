package com.team6.hrbank.scheduler;

import com.team6.hrbank.service.PositionStatsService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionStatsScheduler {
  private final PositionStatsService positionStatsService;

  @Scheduled(cron = "0 05 08 * * *")
  @SchedulerLock(name = "batchCreatePositionStats", lockAtMostFor = "10m")
  public void batchCreatePositionStats() {
    positionStatsService.createTodayStats();
  }

}
