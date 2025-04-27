package com.team6.hrbank.dto.employeestats;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTrendDto {
  private LocalDate date;
  private long count;
  private long change;
  private double changeRate;
}
