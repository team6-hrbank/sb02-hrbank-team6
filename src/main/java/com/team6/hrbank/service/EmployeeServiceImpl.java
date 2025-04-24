package com.team6.hrbank.service;

import com.team6.hrbank.dto.employee.EmployeeCreateRequest;
import com.team6.hrbank.dto.employee.EmployeeDto;

import com.team6.hrbank.dto.employee.EmployeeUpdateRequest;
import com.team6.hrbank.entity.Department;
import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.entity.FileMetadata;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.EmployeeMapper;
import com.team6.hrbank.repository.DepartmentRepository;
import com.team6.hrbank.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    //private final FileMetadataService fileMetadataService;
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

        FileMetadata newFile = null;
        //FileMetadata Service 구현체가 없으므로 주석 처리 하겠습니다.
        /*if (profileImage != null && !profileImage.isEmpty()) {
            newFile = fileMetadataService.create(profileImage);
        }*/

        Employee newEmployee = employeeMapper.toEntity(request, employeeNumber, department, newFile);
        Employee savedEmployee = employeeRepository.save(newEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return employeeMapper.toDto(employee);
    }

    // 아래 메서드부터는 다음 브랜치에서 구현 예정
    @Override
    public List<EmployeeDto> findAll() {
        return List.of();
    }

    @Override
    public EmployeeDto update(Long id, EmployeeUpdateRequest request, MultipartFile profileImage) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RestException(ErrorCode.EMPLOYEE_NOT_FOUND));
        employeeRepository.delete(employee);
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
}
