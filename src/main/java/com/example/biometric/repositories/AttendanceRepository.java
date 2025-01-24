package com.example.biometric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.biometric.entity.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Existing method
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    // Fetch all attendance records for a specific employee in a given month and year
    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId AND YEAR(a.date) = :year AND MONTH(a.date) = :month AND a.status = true")
    List<Attendance> findByEmployeeAndYearAndMonth(@Param("employeeId") Long employeeId,
                                                   @Param("year") int year,
                                                   @Param("month") int month);
}
