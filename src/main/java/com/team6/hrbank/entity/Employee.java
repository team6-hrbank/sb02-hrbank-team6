package com.team6.hrbank.entity;

import com.team6.hrbank.dto.employee.EmployeeUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employees")

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String employeeNumber;

    @Column(length = 100, nullable = false)
    private String employeeName;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private EmployeePosition employeePosition;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeState employeeState = EmployeeState.ACTIVE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private FileMetadata profileImage;

    public void update(EmployeeUpdateRequest request, Department department, FileMetadata newProfileImage) {
        this.employeeName = request.name();
        this.email = request.email();
        this.department = department;
        this.employeePosition = request.position();
        this.hireDate = request.hireDate();
        this.employeeState = request.status();
        this.profileImage = newProfileImage;
    }

}
