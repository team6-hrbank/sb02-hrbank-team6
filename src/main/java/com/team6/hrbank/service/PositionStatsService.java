package com.team6.hrbank.service;

import com.team6.hrbank.entity.EmployeeState;

public interface PositionStatsService {
  // 배치 처리
  void createTodayStats();

  // 배치 처리 내부에서 쓰이는 상태별 저장 로직
  void createTodayStatsByState(EmployeeState state);


}
