package com.team6.hrbank.dto.employeestats;

import java.time.LocalDate;

public record EmployeeTrendDto(
    LocalDate date,
    long count,
    long change,
    double changeRate
) {

}
