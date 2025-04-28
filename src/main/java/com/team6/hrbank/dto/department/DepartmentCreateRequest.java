package com.team6.hrbank.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DepartmentCreateRequest(
    @NotBlank(message = "부서 이름은 필수입니다.")
    @Size(max = 20, message = "부서 이름은 20자 이내여야 합니다.")
    String name,

    @NotBlank(message = "부서 설명은 필수입니다.")
    String description,

    @NotNull(message = "부서 설립일은 필수입니다.")
    @PastOrPresent(message = "부서 설립일은 오늘 이후일 수 없습니다.")
    LocalDate establishedDate
) {

}
