package com.team6.hrbank.service;

import com.team6.hrbank.dto.changeLog.ChangeLogDto;
import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.entity.ChangeLog;
import com.team6.hrbank.mapper.ChangeLogMapper;
import com.team6.hrbank.repository.ChangeLogRepository;
import com.team6.hrbank.specification.ChangeLogSpecifications;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeLogRepository changeLogRepository;
  private final ChangeLogMapper changeLogMapper;

  @Transactional(readOnly = true)
  @Override
  public CursorPageResponseChangeLogDto getChangeLogs(ChangeLogSearchCondition condition) {
    Pageable pageable = PageRequest.of(0, condition.size(), Sort.by(
        Sort.Direction.fromString(condition.sortDirection()),
        condition.sortField().equals("at") ? "createdAt" : condition.sortField()
    ));

    Specification<ChangeLog> spec = ChangeLogSpecifications.withConditions(condition);
    Page<ChangeLog> page = changeLogRepository.findAll(spec, pageable);

    List<ChangeLogDto> content = changeLogMapper.toDtoList(page.getContent());

    Long nextIdAfter = content.isEmpty() ? null : content.get(content.size() - 1).id();
    Instant nextCursor = content.isEmpty() ? null : content.get(content.size() - 1).at();

    return new CursorPageResponseChangeLogDto(
        content,
        nextCursor != null ? nextCursor.toString() : null,
        nextIdAfter,
        condition.size(),
        page.getTotalElements(),
        page.hasNext()
    );
  }

}
