package com.team6.hrbank.mapper;

import com.team6.hrbank.dto.employeestats.EmployeeTrendDto;
import com.team6.hrbank.entity.EmployeeStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeStatsMapper {

  @Mapping(target = "date", source = "employeeStats.statDate")
  @Mapping(target = "count", source = "employeeStats.employeeCount")
  EmployeeTrendDto toEmployeeTrendDto(EmployeeStats employeeStats, long change, double changeRate);

}
