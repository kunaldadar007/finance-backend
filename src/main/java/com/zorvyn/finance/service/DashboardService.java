package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.DashboardSummaryDTO;
import com.zorvyn.finance.entity.FinancialRecord;
import com.zorvyn.finance.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private FinancialRecordRepository recordRepository;

    public DashboardSummaryDTO getSummary(String userId) {
        List<FinancialRecord> records = recordRepository.findByUserId(userId);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (FinancialRecord record : records) {
            if ("INCOME".equals(record.getType())) {
                totalIncome = totalIncome.add(record.getAmount());
            } else if ("EXPENSE".equals(record.getType())) {
                totalExpense = totalExpense.add(record.getAmount());
            }
        }

        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        return new DashboardSummaryDTO(totalIncome, totalExpense, netBalance, (long) records.size());
    }

    public Map<String, BigDecimal> getCategoryBreakdown(String userId) {
        List<FinancialRecord> records = recordRepository.findByUserId(userId);
        Map<String, BigDecimal> categoryMap = new HashMap<>();

        for (FinancialRecord record : records) {
  

       String category = record.getCategory();
            BigDecimal amount = categoryMap.getOrDefault(category, BigDecimal.ZERO);
            categoryMap.put(category, amount.add(record.getAmount()));
        }

        return categoryMap;
    }

    public List<FinancialRecord> getRecentActivity(String userId) {
        List<FinancialRecord> records = recordRepository.findByUserId(userId);
        return records.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(10)
                .toList();
    }

    public List<FinancialRecord> getRecordsByDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        return recordRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public Map<String, BigDecimal> getIncomeVsExpenseBreakdown(String userId) {
        List<FinancialRecord> records = recordRepository.findByUserId(userId);
        
        BigDecimal totalIncome = records.stream()
            .filter(r -> "INCOME".equals(r.getType()))
            .map(FinancialRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalExpense = records.stream()
            .filter(r -> "EXPENSE".equals(r.getType()))
            .map(FinancialRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, BigDecimal> breakdown = new HashMap<>();
        breakdown.put("income", totalIncome);
        breakdown.put("expense", totalExpense);
        
        return breakdown;
    }
}