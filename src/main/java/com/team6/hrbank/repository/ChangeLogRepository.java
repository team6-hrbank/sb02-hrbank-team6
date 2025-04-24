package com.team6.hrbank.repository;

import com.team6.hrbank.entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, JpaSpecificationExecutor<ChangeLog> {

}
