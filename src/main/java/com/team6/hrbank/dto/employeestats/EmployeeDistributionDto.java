package com.team6.hrbank.dto.employeestats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDistributionDto {
    private String groupKey; // 직무나 부서 이름
    private long count;
    private double percentage;
}
