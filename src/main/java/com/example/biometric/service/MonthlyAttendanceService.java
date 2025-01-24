package com.example.biometric.service;

import com.example.biometric.dto.MonthlyAttendanceDto;
import com.example.biometric.entity.Employee;
import com.example.biometric.entity.MonthlyAttendance;
import com.example.biometric.repositories.EmployeeRepository;
import com.example.biometric.repositories.MonthlyAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MonthlyAttendanceService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MonthlyAttendanceRepository monthlyAttendanceRepository;

    // New method to fetch Monthly Attendance details and return MonthlyAttendanceDto
    public MonthlyAttendanceDto getMonthlyAttendanceDetails(Long employeeId, int year, int month) {
        
        // Fetch the Employee from the repository to get employee details
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));
        
        // Fetch the MonthlyAttendance record based on Employee, year, and month
        Optional<MonthlyAttendance> monthlyAttendanceOpt = monthlyAttendanceRepository
                .findByEmployeeAndMonthAndYear(employee, month, year);
        
        if (monthlyAttendanceOpt.isPresent()) {
            MonthlyAttendance monthlyAttendance = monthlyAttendanceOpt.get();

            // Calculate days absent
            int daysAbsent = monthlyAttendance.getTotalWorkingDays() - monthlyAttendance.getDaysPresent();

            // Return the DTO
            return new MonthlyAttendanceDto(
                    employee.getId(),
                    employee.getName(),
                    month,
                    year,
                    monthlyAttendance.getDaysPresent(),
                    daysAbsent,
                    monthlyAttendance.getTotalWorkingDays()
            );
        } else {
            throw new IllegalArgumentException("No monthly attendance data found for the given employee, month, and year.");
        }
    }
}
