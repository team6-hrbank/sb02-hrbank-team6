package com.team6.hrbank.controller;

import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.dto.changeLog.DiffDto;
import com.team6.hrbank.service.ChangeLogService;
import com.team6.hrbank.swagger.ChangeLogApi;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/change-logs")
public class ChangeLogController implements ChangeLogApi {

  private final ChangeLogService changeLogService;

  @GetMapping
  public ResponseEntity<CursorPageResponseChangeLogDto> getAllByCondition(
      ChangeLogSearchCondition condition) {
    CursorPageResponseChangeLogDto changeLogs = changeLogService.getChangeLogs(condition);
    return ResponseEntity.ok(changeLogs);
  }

  @GetMapping("/{changeLogId}/diffs")
  public ResponseEntity<List<DiffDto>> getDetailsByChangeLogId(@PathVariable Long changeLogId) {
    List<DiffDto> changeLogDetails = changeLogService.getChangeDetail(changeLogId);
    return ResponseEntity.ok(changeLogDetails);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getCount(
      @RequestParam(required = false) Instant fromDate,
      @RequestParam(required = false) Instant toDate
  ) {
    long count = changeLogService.getCount(fromDate, toDate);
    return ResponseEntity.ok(count);
  }


}
