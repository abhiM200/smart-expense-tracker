package com.expensetracker.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseApiController {

    private final ExpenseService expenseService;

    public ExpenseApiController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<?> addExpense(@Valid @RequestBody Expense expense) {
        Expense created = expenseService.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(@RequestParam(required = false) String category) {
        return ResponseEntity.ok(expenseService.getExpenses(category));
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalAmount(@RequestParam(required = false) String category) {
        return ResponseEntity.ok(expenseService.getTotalAmount(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        boolean deleted = expenseService.deleteExpense(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Expense not found"));
        }
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlySummary() {
        return ResponseEntity.ok(expenseService.getMonthlySummary());
    }
}
