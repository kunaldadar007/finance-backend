package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Find all records by userId
    List<FinancialRecord> findByUserId(String userId);

    // Find records by userId and type
    List<FinancialRecord> findByUserIdAndType(String userId, String type);

    // Find records by userId and category
    List<FinancialRecord> findByUserIdAndCategory(String userId, String category);

    // Find records by userId and date range
    List<FinancialRecord> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    // Find records by userId, type and category
    List<FinancialRecord> findByUserIdAndTypeAndCategory(String userId, String type, String category);

    // Find records by userId, type, category and date range
    List<FinancialRecord> findByUserIdAndTypeAndCategoryAndDateBetween(
        String userId, String type, String category, LocalDate startDate, LocalDate endDate);

}