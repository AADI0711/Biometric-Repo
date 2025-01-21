package com.example.biometric.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.biometric.dto.EmployeeRequestDto;
import com.example.biometric.dto.EmployeeResponseDto;
import com.example.biometric.entity.Employee;
import com.example.biometric.mapper.EmployeeMapper;
import com.example.biometric.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	
	public String hashFingerprint(String fingerprintData) throws NoSuchAlgorithmException{
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		byte[] hashBytes = digest.digest(fingerprintData.getBytes());
		StringBuilder hexHash = new StringBuilder();
		for(byte b: hashBytes) {
			hexHash.append(String.format("%02x", b));
		}
		
		return hexHash.toString();	
	}
	

	public EmployeeResponseDto addEmployee(EmployeeRequestDto requestDto) throws NoSuchAlgorithmException{
		if (requestDto.getFingerprintData() == null || requestDto.getFingerprintData().isEmpty()) {
			
            throw new IllegalArgumentException("Fingerprint data in request cannot be null or empty.");
        }

		
		String fingerprintHash = hashFingerprint(requestDto.getFingerprintData());
		
		Employee employee = employeeMapper.toEntity(requestDto);
		employee.setFingerprintHash(fingerprintHash);
		
		Employee savedEmployee= repository.save(employee);
		
		return employeeMapper.toRepsonseDto(savedEmployee);
		
	}

	public boolean validateFingerprint(String fingerprintData) throws NoSuchAlgorithmException{
		if (fingerprintData == null || fingerprintData.isEmpty()) {
            throw new IllegalArgumentException("Fingerprint data cannot be null or empty.");
        }
		String fingerprintHash= hashFingerprint(fingerprintData);
		return repository.findByFingerprintHash(fingerprintHash).isPresent();
		
	}


	public List<Employee> getEmployee() {
		List<Employee> employee = repository.findAll();
		return employee;
	}
	
	
}
