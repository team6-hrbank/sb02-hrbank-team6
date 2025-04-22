package com.team6.hrbank.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeePosition {
    SENIOR_DEVELOPER("시니어 개발자"),
    INTERN("인턴"),
    PRODUCT_MANAGER("프로덕트 매니저"),
    OPERATIONS_MANAGER("운영 매니저"),
    HR_MANAGER("HR 매니저"),
    JUNIOR_DEVELOPER("주니어 개발자"),
    MARKETER("마케터"),
    EMPLOYEE("직원");

    private final String label;

}
