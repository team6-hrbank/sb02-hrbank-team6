package com.team6.hrbank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "department-stats",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_department_state_date",
            columnNames = {"stat_date", "department_name", "employee_state"}
        )
    })
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DepartmentStats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "employee_count", nullable = false)
  private long employeeCount;

  @Column(name = "joined_employee_count", nullable = false)
  private long joinedCount;

  @Column(name = "left_employee_count", nullable = false)
  private long leftCount;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "stat_date", nullable = false)
  private LocalDate statDate;

  @Column(name = "department_name", length = 100, nullable = false)
  private String departmentName;

  @Enumerated(EnumType.STRING)
  @Column(name = "employee_state", nullable = false)
  private EmployeeState employeeState;


}
