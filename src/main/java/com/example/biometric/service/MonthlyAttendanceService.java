package com.example.biometric.service;

import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.biometric.entity.AttendanceHistory;
import com.example.biometric.entity.Employee;
import com.example.biometric.entity.MonthlyAttendance;
import com.example.biometric.repositories.AttendanceHistoryRepository;
import com.example.biometric.repositories.AttendanceRepository;
import com.example.biometric.repositories.EmployeeRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import com.example.biometric.repositories.MonthlyAttendanceRepository;

@Service
public class MonthlyAttendanceService {
@Autowired
private EmployeeRepository employeeRepository;
@Autowired
private AttendanceRepository attendanceRepository;
@Autowired
private MonthlyAttendanceRepository monthlyAttendanceRepository;
@Autowired
private AttendanceHistoryRepository attendanceHistoryRepository;

private int calculateWeekends(int year, int month, int totalDaysInMonth) {
	try {
		int weekends = 0;
		for (int day = 1; day <= totalDaysInMonth; day++) {
		LocalDate date = LocalDate.of(year, month, day);
		if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
		weekends++;
		}
		}
		return weekends;
		} catch (Exception e) {
		throw new RuntimeException("Error while calculating weekends.", e);
		}
	}


public byte[] generateMonthlyReport() throws Exception {
   LocalDate now = LocalDate.now();
   int year = now.getYear();
   int month = now.getMonthValue();
   YearMonth yearMonth = YearMonth.of(year, month);
   int dayOfMonth = now.getDayOfMonth();

   try {
   // Retrieve all current MonthlyAttendance records
   List<MonthlyAttendance> existingData = monthlyAttendanceRepository.findAll();

   // Process each MonthlyAttendance record to update attendanceHistory
   for (MonthlyAttendance record : existingData) {
   // Check if a record for the same employee, month, and year exists in attendanceHistory
   Optional<AttendanceHistory> existingHistory = attendanceHistoryRepository.findByEmployeeIdAndMonthAndYear(record.getEmployee().getId(), record.getMonth(), record.getYear());

   if (existingHistory.isPresent()) {
   // Update the existing record
   AttendanceHistory history = existingHistory.get();
   history.setDaysPresent(dayOfMonth);
   history.setDaysAbsent(record.getDaysAbsent());
   history.setTotalWorkingDays(record.getTotalWorkingDays());
   attendanceHistoryRepository.save(history);
   } else {
   // Create a new record
   AttendanceHistory history = new AttendanceHistory();
   history.setEmployee(record.getEmployee());
   history.setMonth(record.getMonth());
   history.setYear(record.getYear());
   history.setDaysPresent(record.getDaysPresent());
   history.setDaysAbsent(record.getDaysAbsent());
   history.setTotalWorkingDays(record.getTotalWorkingDays());
   attendanceHistoryRepository.save(history);
   }
   }

// Delete all current MonthlyAttendance records to prepare for new data
   monthlyAttendanceRepository.deleteAll();

// Recalculate MonthlyAttendance for the current month up to today
   List<Employee> employees = employeeRepository.findAll();
   for (Employee employee : employees) {
   long presentDays = attendanceRepository.findAll()
   .stream()
   .filter(a -> a.getEmployee().getId().equals(employee.getId())
   && a.getDate().getYear() == year
   && a.getDate().getMonthValue() == month
   && a.getStatus())
   .count();

   int totalDaysTillToday = dayOfMonth;
   int weekendsTillToday = calculateWeekends(year, month, totalDaysTillToday);

   int totalWorkingDays = totalDaysTillToday - weekendsTillToday;
   int daysAbsent = totalWorkingDays - (int) presentDays;

   // Create new MonthlyAttendance records
   MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
   monthlyAttendance.setEmployee(employee);
   monthlyAttendance.setYear(year);
   monthlyAttendance.setMonth(month);
   monthlyAttendance.setDaysPresent((int) presentDays);
   monthlyAttendance.setDaysAbsent(daysAbsent);
   monthlyAttendance.setTotalWorkingDays(totalWorkingDays);
   monthlyAttendanceRepository.save(monthlyAttendance);
   }

   // Generate the Excel file using the updated MonthlyAttendance data
   List<MonthlyAttendance> attendanceData = monthlyAttendanceRepository.findAll();
   return generateExcelFile(attendanceData);
   } catch (Exception e) {
   e.printStackTrace();
   throw new Exception("Error occurred while generating the monthly attendance report: " + e.getMessage());
   }
   }
//Save to disk inside the method
private byte[] generateExcelFile(List<MonthlyAttendance> attendanceData) throws IOException {
 XSSFWorkbook workbook = null;
 ByteArrayOutputStream outputStream = null;
 try {
     workbook = new XSSFWorkbook();
     Sheet sheet = workbook.createSheet("Monthly Attendance");

     // Create header row
     Row headerRow = sheet.createRow(0);
     headerRow.createCell(0).setCellValue("Employee ID");
     headerRow.createCell(1).setCellValue("Days Present");
     headerRow.createCell(2).setCellValue("Days Absent");
     headerRow.createCell(3).setCellValue("Total Working Days");

     // Populate data rows
     int rowNum = 1;
     for (MonthlyAttendance attendance : attendanceData) {
         Row row = sheet.createRow(rowNum++);
         row.createCell(0).setCellValue(attendance.getEmployee().getId());
         row.createCell(1).setCellValue(attendance.getDaysPresent());
         row.createCell(2).setCellValue(attendance.getDaysAbsent());
         row.createCell(3).setCellValue(attendance.getTotalWorkingDays());
     }

     // Save to a file
     try (FileOutputStream fileOut = new FileOutputStream("MonthlyAttendance.xlsx")) {
         workbook.write(fileOut);
     }

     // Write to ByteArrayOutputStream for returning if needed
     outputStream = new ByteArrayOutputStream();
     workbook.write(outputStream);
     return outputStream.toByteArray();
 } finally {
     if (workbook != null) {
         workbook.close();
     }
     if (outputStream != null) {
         outputStream.close();
     }
 }
}



}




