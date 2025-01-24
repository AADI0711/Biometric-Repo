package com.example.biometric.controller;

import com.example.biometric.service.AttendanceService;
import com.example.biometric.service.MonthlyAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.biometric.dto.FingerprintRequestDto;
import com.example.biometric.dto.MonthlyAttendanceDto;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private MonthlyAttendanceService monthlyAttendanceService;
    
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/monthly-attendance")
    public ResponseEntity<MonthlyAttendanceDto> getMonthlyAttendanceDetails(
            @RequestParam Long employeeId,  
            @RequestParam int year,         
            @RequestParam int month) {      

        // Call the service to get attendance details as a DTO
        MonthlyAttendanceDto monthlyAttendanceDto = monthlyAttendanceService.getMonthlyAttendanceDetails(employeeId, year, month);

        return ResponseEntity.ok(monthlyAttendanceDto);
    }
    @PostMapping("/markAttendance")
    public ResponseEntity<String> markAttendance(@RequestBody FingerprintRequestDto fingerprintRequestDto) {
        try {
            System.out.println("Fingerprint Data Received: " + fingerprintRequestDto.getFingerprintData());
            String result = attendanceService.markAttendance(fingerprintRequestDto.getFingerprintData());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            return ResponseEntity.status(500).body("An error occurred while marking attendance.");
        }
    }


    @GetMapping("/generateMonthlyReport")
    public ResponseEntity<String> generateMonthlyAttendanceReport() {
        attendanceService.generateMonthlyAttendanceReport();
        return ResponseEntity.ok("Monthly attendance report generated successfully.");
    }    
    
}
