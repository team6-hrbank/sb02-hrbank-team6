package com.team6.hrbank.service;

import com.team6.hrbank.dto.changeLog.ChangeLogDto;
import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.dto.changeLog.DiffDto;
import com.team6.hrbank.entity.ChangeLog;
import com.team6.hrbank.entity.ChangeLogDetail;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.ChangeLogDetailMapper;
import com.team6.hrbank.mapper.ChangeLogMapper;
import com.team6.hrbank.repository.ChangeLogDetailRepository;
import com.team6.hrbank.repository.ChangeLogRepository;
import com.team6.hrbank.specification.ChangeLogSpecifications;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeLogRepository changeLogRepository;
  private final ChangeLogMapper changeLogMapper;
  private final ChangeLogDetailRepository changeLogDetailRepository;
  private final ChangeLogDetailMapper changeLogDetailMapper;

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

  @Transactional(readOnly = true)
  @Override
  public List<DiffDto> getChangeDetail(Long changeLogId) {
    if (!changeLogRepository.existsById(changeLogId)) {
      throw new RestException(ErrorCode.CHANGE_LOG_NOT_FOUND);
    }

    List<ChangeLogDetail> changeLogDetails = changeLogDetailRepository.findAllByChangeLogId(changeLogId);
    if (changeLogDetails.isEmpty()) {
      throw new RestException(ErrorCode.CHANGE_LOG_DETAIL_NOT_FOUND);
    }

    return changeLogDetailMapper.toDtoList(changeLogDetails);
  }

}
