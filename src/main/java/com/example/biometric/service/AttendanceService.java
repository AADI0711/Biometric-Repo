package com.example.biometric.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.biometric.entity.Attendance;
import com.example.biometric.entity.MonthlyAttendance;
import com.example.biometric.entity.Employee;
import com.example.biometric.entity.HolidayList;
import com.example.biometric.repositories.AttendanceRepository;
import com.example.biometric.repositories.MonthlyAttendanceRepository;
import com.example.biometric.repositories.EmployeeRepository;
import com.example.biometric.repositories.HolidayListRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private HolidayListRepository holidayListRepository;

    @Autowired
    private MonthlyAttendanceRepository monthlyAttendanceRepository;

    public String markAttendance(String fingerprintData) throws Exception {
        // Generate fingerprint hash
        String fingerprintHash = hashFingerprint(fingerprintData);
        System.out.println("Generated Fingerprint Hash: " + fingerprintHash);

        // Validate fingerprint with database
        Optional<Employee> optionalEmployee = employeeRepository.findByFingerprintHash(fingerprintHash);
        if (optionalEmployee.isEmpty()) {
            throw new IllegalArgumentException("Invalid fingerprint data. No matching employee found.");
        }

        Employee employee = optionalEmployee.get();
        LocalDate today = LocalDate.now();

        // Check if attendance is already marked for today
        Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeIdAndDate(employee.getId(), today);
        if (existingAttendance.isPresent()) {
            return "Attendance already marked for today.";
        }

        // Mark attendance
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(today);
        attendance.setStatus(true);

        attendanceRepository.save(attendance);
        System.out.println("Attendance marked successfully for employee: " + employee.getName());

        return "Attendance marked successfully.";
    }

    public void generateMonthlyAttendanceReport() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        YearMonth yearMonth = YearMonth.of(year, month);
        int totalDays = yearMonth.lengthOfMonth();
        List<HolidayList> holidays = holidayListRepository.findByMonthAndYear(month, year);

        // Calculate weekends (Saturdays and Sundays)
        int weekends = calculateWeekends(year, month, totalDays);

        // Calculate total working days by excluding weekends and holidays
        int totalWorkingDays = totalDays - weekends - holidays.size();

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            long presentDays = attendanceRepository
                    .findAll()
                    .stream()
                    .filter(a -> a.getEmployee().getId().equals(employee.getId())
                            && a.getDate().getYear() == year
                            && a.getDate().getMonthValue() == month
                            && a.getStatus())
                    .count();

            // Updated logic for retrieving or creating a new MonthlyAttendance record
            MonthlyAttendance monthlyAttendance = monthlyAttendanceRepository
                    .findByEmployeeAndMonthAndYear(employee, year, month)
                    .orElseGet(() -> {
                        MonthlyAttendance newRecord = new MonthlyAttendance();
                        newRecord.setEmployee(employee);
                        newRecord.setYear(year);
                        newRecord.setMonth(month);
                        newRecord.setTotalWorkingDays(0); // Default value
                        newRecord.setDaysPresent(0);     // Default value
                        return newRecord;
                    });

            // Set values for the current month
            monthlyAttendance.setTotalWorkingDays(totalWorkingDays); // Ensure it's properly set
            monthlyAttendance.setDaysPresent((int) presentDays);     // Ensure it's properly set

            // Save the updated or new MonthlyAttendance record
            monthlyAttendanceRepository.save(monthlyAttendance);
        }
    }

    private int calculateWeekends(int year, int month, int totalDaysInMonth) {
        int weekends = 0;
        for (int day = 1; day <= totalDaysInMonth; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
                weekends++;
            }
        }
        return weekends;
    }

    public String hashFingerprint(String fingerprintData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fingerprintData.getBytes());
        StringBuilder hexHash = new StringBuilder();
        for (byte b : hashBytes) {
            hexHash.append(String.format("%02x", b));
        }
        return hexHash.toString();
    }
}
