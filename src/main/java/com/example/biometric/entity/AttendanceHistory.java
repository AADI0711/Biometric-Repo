package com.example.biometric.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendance_history", uniqueConstraints = {
@UniqueConstraint(columnNames = {"employee_id", "month", "year"})
})
public class AttendanceHistory {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "employee_id", nullable = false)
private Employee employee;

@Column(nullable = false)
private int month;

@Column(nullable = false)
private int year;

@Column(nullable = false)
private int daysPresent;

@Column(nullable = false)
private int daysAbsent;

@Column(nullable = false)
private int totalWorkingDays;
}