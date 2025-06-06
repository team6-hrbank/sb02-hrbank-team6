package com.team6.hrbank.repository;

import com.team6.hrbank.entity.ChangeLog;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, ChangeLogRepositoryCustom {
  Long countByCreatedAtBetween(Instant fromDate, Instant toDate);
}
