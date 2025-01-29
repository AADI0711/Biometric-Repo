package com.example.biometric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.biometric.entity.AttendanceHistory;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceHistoryRepository extends JpaRepository<AttendanceHistory, Long> {
List<AttendanceHistory> findByMonthAndYear(int month, int year);
Optional<AttendanceHistory> findByEmployeeIdAndMonthAndYear(Long employeeId, int month, int year);

List<AttendanceHistory> findByEmployeeId(Long employeeId);
}
