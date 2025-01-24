package com.example.biometric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.biometric.entity.HolidayList;

import java.util.List;

@Repository
public interface HolidayListRepository extends JpaRepository<HolidayList, Long> {
    @Query("SELECT h FROM HolidayList h WHERE MONTH(h.date) = :month AND YEAR(h.date) = :year")
    List<HolidayList> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}