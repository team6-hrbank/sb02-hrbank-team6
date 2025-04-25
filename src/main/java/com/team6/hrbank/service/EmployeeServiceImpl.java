package com.team6.hrbank.service;

import com.team6.hrbank.dto.employee.*;

import com.team6.hrbank.entity.Department;
import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.entity.EmployeeState;
import com.team6.hrbank.entity.FileMetadata;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.EmployeeMapper;
import com.team6.hrbank.repository.DepartmentRepository;
import com.team6.hrbank.repository.EmployeeQueryRepository;
import com.team6.hrbank.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final DepartmentRepository departmentRepository;
    private final FileMetadataService fileMetadataService;
    private final EmployeeMapper employeeMapper;


    @Override
    public EmployeeDto create(EmployeeCreateRequest request, MultipartFile profileImage) {
        String email = request.email();
        if (employeeRepository.existsByEmail(email)) {
            throw new RestException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        int year = LocalDate.now().getYear();
        String employeeNumber = generateEmployeeNumber(year);

        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));

        FileMetadata newProfileImage = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            newProfileImage = fileMetadataService.create(profileImage);
        }

        Employee newEmployee = employeeMapper.toEntity(request, employeeNumber, department, newProfileImage);
        Employee savedEmployee = employeeRepository.save(newEmployee);
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

        return new CursorPageResponseEmployeeDto(
                content,
                nextCursor,
                nextIdAfter,
                condition.size() != null ? condition.size() : 10,
                total,
                !content.isEmpty() && content.size() >= (condition.size() != null ? condition.size() : 10)
        );

    }

    @Override
    public EmployeeDto update(Long id, EmployeeUpdateRequest request, MultipartFile profileImage) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RestException(ErrorCode.EMPLOYEE_NOT_FOUND));

        String newEmail = request.email();
        if (!employee.getEmail().equals(newEmail) && employeeRepository.existsByEmail(newEmail)) {
            throw new RestException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        Department newDepartment = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));

        FileMetadata newProfileImage = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            newProfileImage = fileMetadataService.create(profileImage);
        }
        employee.update(request, newDepartment, newProfileImage);
        return employeeMapper.toDto(employee);
    }

    @Override
    public void deleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RestException(ErrorCode.EMPLOYEE_NOT_FOUND));
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
    private boolean isEmptyCondition(EmployeeSearchCondition condition) {
        return condition.nameOrEmail() == null &&
                condition.departmentName() == null &&
                condition.position() == null &&
                condition.employeeNumber() == null &&
                condition.hireDateFrom() == null &&
                condition.hireDateTo() == null &&
                condition.status() == null;
    }
}
