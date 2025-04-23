package com.team6.hrbank.entity;

import com.team6.hrbank.dto.department.DepartmentUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departments")
@Entity
@Builder
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "department_name", nullable = false, unique = true)
  private String departmentName;

  @Column(name = "department_description", nullable = false, columnDefinition = "TEXT")
  private String departmentDescription;

  @Column(name = "department_established_date", nullable = false)
  private LocalDate departmentEstablishedDate;

  @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
  private List<Employee> employees = new ArrayList<>();

  public int getEmployeeCount() {
    return employees != null ? employees.size() : 0;
  }

  public void update(DepartmentUpdateRequest request) {
    this.departmentName = request.name();
    this.departmentDescription = request.description();
    this.departmentEstablishedDate = request.establishedDate();
  }

}
