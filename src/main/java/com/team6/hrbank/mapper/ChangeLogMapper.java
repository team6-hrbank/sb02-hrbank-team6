package com.team6.hrbank.mapper;

import com.team6.hrbank.dto.changeLog.ChangeLogDto;
import com.team6.hrbank.entity.ChangeLog;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

  @Mapping(source = "employee.employeeNumber", target = "employeeNumber")
  @Mapping(source = "createdAt", target = "at")
  ChangeLogDto toDto(ChangeLog changeLog);

  List<ChangeLogDto> toDtoList(List<ChangeLog> changeLogs);

}
