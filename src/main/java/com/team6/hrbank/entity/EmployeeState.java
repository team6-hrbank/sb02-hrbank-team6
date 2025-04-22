package com.team6.hrbank.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeState {
    ACTIVE("재직중"),
    LEAVE("휴직중"),
    RESIGNED("퇴사");

    private final String label;
}
