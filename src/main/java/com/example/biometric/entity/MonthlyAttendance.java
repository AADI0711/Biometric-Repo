package com.example.biometric.entity;

import jakarta.persistence.*;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "monthly_attendance")
public class MonthlyAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "total_working_days", nullable = false)
    private int totalWorkingDays;

    @Column(name = "days_present", nullable = false)
    private int daysPresent;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyAttendance that = (MonthlyAttendance) o;
        return month == that.month && year == that.year && totalWorkingDays == that.totalWorkingDays
                && daysPresent == that.daysPresent && Objects.equals(id, that.id) && Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employee, month, year, totalWorkingDays, daysPresent);
    }

    @Override
    public String toString() {
        return "MonthlyAttendance{" +
                "id=" + id +
                ", employee=" + (employee != null ? employee.getId() : null) +
                ", month=" + month +
                ", year=" + year +
                ", totalWorkingDays=" + totalWorkingDays +
                ", daysPresent=" + daysPresent +
                '}';
    }
}
