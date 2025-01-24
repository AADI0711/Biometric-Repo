package com.example.biometric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MonthlyAttendanceDto {

    private Long employeeId;
    private String employeeName;
    private int month;
    private int year;
    private int daysPresent;
    private int daysAbsent;
    private int totalWorkingDays;
}
