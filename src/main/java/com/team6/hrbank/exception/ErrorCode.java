package com.team6.hrbank.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCode implements Code {

  NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

  //EMPLOYEE
  EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "직원을 찾을 수 없습니다."),
  DUPLICATE_EMPLOYEE(HttpStatus.CONFLICT, "이미 존재하는 직원입니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),


  //DEPARTMENT
  DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "부서를 찾을 수 없습니다."),
  DUPLICATE_DEPARTMENT(HttpStatus.CONFLICT, "이미 존재하는 부서입니다."),

  //FILE METADATA
  FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),

  CANNOT_DELETE_DEPARTMENT(HttpStatus.FORBIDDEN, "부서에 소속된 직원이 존재하여 삭제가 불가합니다.");


  private final HttpStatus status;
  private final String message;

  @Override
  public HttpStatus getStatus() {
    return this.status;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

}
