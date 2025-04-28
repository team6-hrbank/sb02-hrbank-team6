package com.team6.hrbank.repository;

import com.team6.hrbank.entity.BackupHistory;
import com.team6.hrbank.entity.BackupStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
  Optional<BackupHistory> findTopByStatusOrderByStartedAtDesc(BackupStatus status);

}
