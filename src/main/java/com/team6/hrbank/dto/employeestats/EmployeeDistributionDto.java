package com.team6.hrbank.dto.employeestats;

public record EmployeeDistributionDto(
    String groupKey, // 직무나 부서 이름
    long count,
    double percentage
) {
}
