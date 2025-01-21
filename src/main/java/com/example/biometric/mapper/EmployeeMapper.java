package com.example.biometric.mapper;

import org.springframework.stereotype.Component;

import com.example.biometric.dto.EmployeeRequestDto;
import com.example.biometric.dto.EmployeeResponseDto;
import com.example.biometric.entity.Employee;

@Component
public class EmployeeMapper {
	
	// Convert RequestDto to Entity
	public Employee toEntity(EmployeeRequestDto dto) {
		Employee employee = new Employee();
		employee.setName(dto.getName());
		employee.setFingerprintHash(dto.getFingerprintData());
		return employee;
	}
	
	// Convert Entity to ResponseDto
	public EmployeeResponseDto toRepsonseDto(Employee employee) {
		EmployeeResponseDto dto = new EmployeeResponseDto();
		dto.setId(employee.getId());
		dto.setName(employee.getName());
		return dto;
	}
}
