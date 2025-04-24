package com.team6.hrbank.repository;

import com.team6.hrbank.entity.ChangeLog;
import com.team6.hrbank.entity.ChangeLogDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogDetailRepository extends JpaRepository<ChangeLogDetail, Long> {

  List<ChangeLogDetail> findAllByChangeLogId(Long changeLogId);

}
