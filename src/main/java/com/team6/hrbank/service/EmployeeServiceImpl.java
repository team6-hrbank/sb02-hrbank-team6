package com.team6.hrbank.service;

import com.team6.hrbank.dto.employee.CursorPageResponseEmployeeDto;
import com.team6.hrbank.dto.employee.EmployeeCreateRequest;
import com.team6.hrbank.dto.employee.EmployeeDto;
import com.team6.hrbank.dto.employee.EmployeeSearchCondition;
import com.team6.hrbank.dto.employee.EmployeeUpdateRequest;
import com.team6.hrbank.entity.*;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.EmployeeMapper;
import com.team6.hrbank.repository.DepartmentRepository;
import com.team6.hrbank.repository.EmployeeQueryRepository;
import com.team6.hrbank.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final DepartmentRepository departmentRepository;
    private final FileMetadataService fileMetadataService;
    private final ChangeLogService changeLogService;
    private final EmployeeMapper employeeMapper;


    @Override
    public EmployeeDto create(EmployeeCreateRequest request, MultipartFile profileImage, String ipAddress) {
        String email = request.email();
        if (employeeRepository.existsByEmail(email)) {
            throw new RestException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        int year = request.hireDate().getYear();
        String employeeNumber = generateEmployeeNumber(year);

        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));

        EmployeePosition position = validateAndParsePosition(request.position());

        FileMetadata newProfileImage = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            newProfileImage = fileMetadataService.create(profileImage);
        }

        Employee newEmployee = employeeMapper.toEntity(request, employeeNumber, department, position, newProfileImage);
        Employee savedEmployee = employeeRepository.save(newEmployee);

        changeLogService.create(null, savedEmployee, request.memo(), ipAddress);

        return employeeMapper.toDto(savedEmployee);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return employeeMapper.toDto(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public CursorPageResponseEmployeeDto searchEmployees(EmployeeSearchCondition condition) {
        List<Employee> employees = employeeRepository.searchEmployeesWithCursor(condition);
        List<EmployeeDto> content = employees.stream()
                .map(employeeMapper::toDto)
                .toList();

        Long nextIdAfter = content.isEmpty() ? null : content.get(content.size() - 1).id();
        String nextCursor = null;
        if (!content.isEmpty()) {
            Employee lastEmployee = employees.get(employees.size() - 1);
            nextIdAfter = lastEmployee.getId();
            switch (condition.sortField()) {
                case "employeeNumber" -> nextCursor = lastEmployee.getEmployeeNumber();
                case "hireDate" -> nextCursor = lastEmployee.getHireDate().toString();
                default -> nextCursor = lastEmployee.getEmployeeName();
            }
        }
        long total = (isEmptyCondition(condition)) ? employeeRepository.count() : 0;

        int pageSize = condition.size() != null ? condition.size() : 10;

        return new CursorPageResponseEmployeeDto(
                content,
                nextCursor,
                nextIdAfter,
                pageSize,
                total,
                !content.isEmpty() && content.size() == pageSize
        );

    }

    @Override
    public EmployeeDto update(Long id, EmployeeUpdateRequest request, MultipartFile profileImage, String ipAddress) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RestException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Employee beforeUpdate = cloneEmployee(employee);

        String newEmail = request.email();
        if (!employee.getEmail().equals(newEmail) && employeeRepository.existsByEmail(newEmail)) {
            throw new RestException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        Department newDepartment = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));

        EmployeePosition position = validateAndParsePosition(request.position());

        FileMetadata newProfileImage = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            newProfileImage = fileMetadataService.create(profileImage);
        }
        employee.update(request, newDepartment, position, newProfileImage);

        changeLogService.create(beforeUpdate, employee, request.memo(), ipAddress);

        return employeeMapper.toDto(employee);
    }

    @Override
    public void deleteById(Long id, String ipAddress) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RestException(ErrorCode.EMPLOYEE_NOT_FOUND));


        if (employee.getProfileImage() != null) {
            fileMetadataService.deleteById(employee.getProfileImage().getId());
        }

        changeLogService.create(employee, null, null, ipAddress);

        employee.changeState(EmployeeState.RESIGNED);
    }

    @Override
    @Transactional(readOnly = true)
    public long count(EmployeeState status, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) {
            return employeeQueryRepository.countByEmployeeState(status);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        return employeeQueryRepository.countByEmployeeStateAndHireDateBetween(status, fromDate, toDate);
    }

    private String generateEmployeeNumber(int year) {
        while (true) {
            String random = String.format("%05d", (int) (Math.random() * 100000)); // 5자리
            String employeeNumber = String.format("EMP-%d-%s", year, random);
            if (!employeeRepository.existsByEmployeeNumber(employeeNumber)) {
                return employeeNumber;
            }
        }
    }

    private EmployeePosition validateAndParsePosition(String label) {
        for (EmployeePosition position : EmployeePosition.values()) {
            if (position.getLabel().equals(label)) {
                return position;
            }
        }
        try {
            return EmployeePosition.valueOf(label);
        } catch (IllegalArgumentException e) {
            throw new RestException(ErrorCode.INVALID_POSITION);
        }
    }

    private boolean isEmptyCondition(EmployeeSearchCondition condition) {
        return condition.nameOrEmail() == null &&
                condition.departmentName() == null &&
                condition.position() == null &&
                condition.employeeNumber() == null &&
                condition.hireDateFrom() == null &&
                condition.hireDateTo() == null &&
                condition.status() == null;
    }

    private Employee cloneEmployee(Employee original) {
        return Employee.builder()
                .id(original.getId())
                .email(original.getEmail())
                .employeeName(original.getEmployeeName())
                .employeeNumber(original.getEmployeeNumber())
                .employeePosition(original.getEmployeePosition())
                .employeeState(original.getEmployeeState())
                .department(original.getDepartment())
                .hireDate(original.getHireDate())
                .profileImage(original.getProfileImage())
                .build();
    }

}
