package com.example.biometric.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.biometric.entity.Employee;
import com.example.biometric.entity.MonthlyAttendance;

@Repository
public interface MonthlyAttendanceRepository extends JpaRepository<MonthlyAttendance, Long> {
    Optional<MonthlyAttendance> findByEmployeeAndMonthAndYear(Employee employee, int month, int year);
}
