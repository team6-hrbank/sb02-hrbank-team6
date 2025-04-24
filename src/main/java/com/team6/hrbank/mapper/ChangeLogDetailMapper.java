package com.team6.hrbank.mapper;

import com.team6.hrbank.dto.changeLog.DiffDto;
import com.team6.hrbank.entity.ChangeLogDetail;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogDetailMapper {

  @Mapping(source = "beforeValue", target = "before")
  @Mapping(source = "afterValue", target = "after")
  DiffDto toDto(ChangeLogDetail changeLogDetail);

  List<DiffDto> toDtoList(List<ChangeLogDetail> changeLogDetails);


}
