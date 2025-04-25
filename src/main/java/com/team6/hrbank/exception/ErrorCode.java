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

  CANNOT_DELETE_DEPARTMENT(HttpStatus.FORBIDDEN, "부서에 소속된 직원이 존재하여 삭제가 불가합니다."),

  //CHANGE LOG
  CHANGE_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "수정 이력을 찾을 수 없습니다."),
  CHANGE_LOG_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "수정 이력 상세 내용을 찾을 수 없습니다."),

  //EMPLOYEESTATS
  UNSUPPORTED_UNIT(HttpStatus.BAD_REQUEST, "지원하지 않는 Unit 입니다."),
  EMPLOYEE_STATS_NOT_FOUND(HttpStatus.NOT_FOUND, "직원 통계를 찾을 수 없습니다."),
  DUPLICATE_EMPLOYEESTATS(HttpStatus.CONFLICT, "해당 날짜의 직원 통계가 이미 존재합니다."),

  //EMPLOYEESTATE
  UNSUPPORTED_STATUS(HttpStatus.BAD_REQUEST, "지원하지 않는 Status 입니다."),

  //POSITIONSTATS
  DUPLICATE_POSTIONSTATS(HttpStatus.CONFLICT, "해당 날짜, 상태, 직무에 대한 직원 통계가 이미 존재합니다."),

  //DEPARTMENTSTATS
  DUPLICATE_DEPARTMENTSTATS(HttpStatus.CONFLICT, "해당 날짜, 상태, 부서에 대한 직원 통계가 이미 존재합니다."),

  //BACKUP
  LATEST_BACKUP_NOT_FOUND(HttpStatus.NOT_FOUND, "백업 데이터가 없거나 해당 상태의 최근 데이터가 없습니다.");


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
