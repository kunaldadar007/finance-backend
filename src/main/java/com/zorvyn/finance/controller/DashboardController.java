package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.DashboardSummaryDTO;
import com.zorvyn.finance.dto.FinancialRecordDTO;
import com.zorvyn.finance.entity.FinancialRecord;
import com.zorvyn.finance.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@PreAuthorize("isAuthenticated()")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Get dashboard summary (total income, expenses, net balance, record count)
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        DashboardSummaryDTO summary = dashboardService.getSummary(userId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Get category-wise breakdown of spending/income
     */
    @GetMapping("/category-breakdown")
    public ResponseEntity<Map<String, BigDecimal>> getCategoryBreakdown(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        Map<String, BigDecimal> breakdown = dashboardService.getCategoryBreakdown(userId);
        return ResponseEntity.ok(breakdown);
    }

    /**
     * Get income vs expense breakdown
     */
    @GetMapping("/income-vs-expense")
    public ResponseEntity<Map<String, BigDecimal>> getIncomeVsExpense(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        Map<String, BigDecimal> breakdown = dashboardService.getIncomeVsExpenseBreakdown(userId);
        return ResponseEntity.ok(breakdown);
    }

    /**
     * Get 10 most recent transactions
     */
    @GetMapping("/recent-activity")
    public ResponseEntity<List<FinancialRecordDTO>> getRecentActivity(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        List<FinancialRecord> records = dashboardService.getRecentActivity(userId);
        List<FinancialRecordDTO> dtos = records.stream()
            .map(this::mapToDTO)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get records within a date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<FinancialRecordDTO>> getRecordsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        List<FinancialRecord> records = dashboardService.getRecordsByDateRange(userId, startDate, endDate);
        List<FinancialRecordDTO> dtos = records.stream()
            .map(this::mapToDTO)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get statistics by month (optional enhancement)
     */
    @GetMapping("/monthly-stats")
    public ResponseEntity<Map<String, Object>> getMonthlyStats(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        Map<String, Object> stats = new HashMap<>();
        
        DashboardSummaryDTO summary = dashboardService.getSummary(userId);
        Map<String, BigDecimal> categoryBreakdown = dashboardService.getCategoryBreakdown(userId);
        Map<String, BigDecimal> incomeVsExpense = dashboardService.getIncomeVsExpenseBreakdown(userId);
        
        stats.put("totalIncome", summary.getTotalIncome());
        stats.put("totalExpense", summary.getTotalExpense());
        stats.put("netBalance", summary.getNetBalance());
        stats.put("recordCount", summary.getRecordCount());
        stats.put("categoryBreakdown", categoryBreakdown);
        stats.put("incomeVsExpense", incomeVsExpense);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Helper method to convert FinancialRecord to DTO
     */
    private FinancialRecordDTO mapToDTO(FinancialRecord record) {
        FinancialRecordDTO dto = new FinancialRecordDTO();
        dto.setId(record.getId().toString());
        dto.setAmount(record.getAmount());
        dto.setType(record.getType());
        dto.setCategory(record.getCategory());
        dto.setDate(record.getDate());
        dto.setDescription(record.getDescription());
        return dto;
    }
}
