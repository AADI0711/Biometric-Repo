package com.example.biometric.controller;

import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.biometric.dto.EmployeeRequestDto;
import com.example.biometric.dto.EmployeeResponseDto;
import com.example.biometric.service.EmployeeService;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/enroll")
	public ResponseEntity<EmployeeResponseDto> enrollEmployee(@RequestBody EmployeeRequestDto requestDto){
		try {
			System.out.println("Controller");
			EmployeeResponseDto responseDto = employeeService.addEmployee(requestDto);

			return ResponseEntity.ok(responseDto);
		} catch (NoSuchAlgorithmException e) {

			return ResponseEntity.status(500).body(null); // Consider adding a meaningful response body
		} catch (Exception e) {

			return ResponseEntity.status(500).body(null);
		}
	}
	
	@PostMapping("/validate")
	public ResponseEntity<String> validateFingerprint(@RequestParam String fingerprintData){
		try {

			boolean isValid = employeeService.validateFingerprint(fingerprintData);
			if (isValid) {
				return ResponseEntity.ok("Fingerprint validated successfully.");
			} else {
				return ResponseEntity.status(404).body("Error processing fingerprint.");
			}
		} catch (NoSuchAlgorithmException e) {

			return ResponseEntity.status(500).body("Error processing fingerprint.");
		} catch (Exception e) {

			return ResponseEntity.status(500).body("Error processing fingerprint.");
		}
	}
	
	
}
