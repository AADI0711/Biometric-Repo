package com.example.biometric.controller;

import com.example.biometric.service.AttendanceService;
import com.example.biometric.service.MonthlyAttendanceService;

import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.biometric.dto.FingerprintRequestDto;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

@Autowired
private MonthlyAttendanceService monthlyAttendanceService;

@Autowired
private AttendanceService attendanceService;

@PostMapping("/markAttendance")
public ResponseEntity<?> markAttendance(@RequestBody FingerprintRequestDto fingerprintRequestDto) {
try {
System.out.println("Marking attendance with fingerprint data: " + fingerprintRequestDto.getFingerprintData());

String result = attendanceService.markAttendance(fingerprintRequestDto.getFingerprintData());
return ResponseEntity.ok(result);
} catch (IllegalArgumentException e) {
System.err.println("Error: " + e.getMessage());
return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
} catch (Exception e) {
System.err.println("Unexpected Error: " + e.getMessage());
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
.body("An error occurred while marking attendance.");
}
}

@GetMapping("/generateMonthlyReport")
public ResponseEntity<byte[]> generateMonthlyReport() {
try {

byte[] report = monthlyAttendanceService.generateMonthlyReport();
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
headers.add("Content-Disposition", "attachment; filename=monthly_attendance_report.xlsx");
return new ResponseEntity<>(report, headers, HttpStatus.OK);

} catch (Exception e) {

e.printStackTrace();
return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
}
}
}
