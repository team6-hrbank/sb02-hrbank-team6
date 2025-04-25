package com.team6.hrbank.dto.changeLog;

public record DiffDto(
    String propertyName,
    String before,
    String after
) {

}
