package com.expensetracker;

import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTests {

    private ExpenseRepository repository;
    private ExpenseService service;

    @BeforeEach
    void setUp() {
        repository = new ExpenseRepository();
        service = new ExpenseService(repository);
        repository.deleteAll();
    }

    @Test
    void testAddExpense() {
        Expense expense = new Expense(null, "Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 7, 10));
        Expense saved = service.addExpense(expense);
        
        assertNotNull(saved.getId());
        assertEquals("Lunch", saved.getTitle());
    }

    @Test
    void testGetExpensesFilter() {
        service.addExpense(new Expense(null, "Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 7, 10)));
        service.addExpense(new Expense(null, "Taxi", new BigDecimal("25.00"), "Travel", LocalDate.of(2026, 7, 11)));
        
        List<Expense> all = service.getExpenses(null);
        assertEquals(2, all.size());
        
        List<Expense> food = service.getExpenses("Food");
        assertEquals(1, food.size());
        assertEquals("Lunch", food.get(0).getTitle());
    }

    @Test
    void testGetTotalAmount() {
        service.addExpense(new Expense(null, "Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 7, 10)));
        service.addExpense(new Expense(null, "Dinner", new BigDecimal("30.00"), "Food", LocalDate.of(2026, 7, 11)));
        service.addExpense(new Expense(null, "Taxi", new BigDecimal("25.00"), "Travel", LocalDate.of(2026, 7, 12)));
        
        assertEquals(new BigDecimal("70.50"), service.getTotalAmount(null));
        assertEquals(new BigDecimal("45.50"), service.getTotalAmount("Food"));
    }

    @Test
    void testDeleteExpense() {
        Expense expense = service.addExpense(new Expense(null, "Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 7, 10)));
        assertTrue(service.deleteExpense(expense.getId()));
        assertFalse(service.deleteExpense(expense.getId()));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetMonthlySummary() {
        service.addExpense(new Expense(null, "Lunch", new BigDecimal("15.00"), "Food", LocalDate.of(2026, 7, 10)));
        service.addExpense(new Expense(null, "Taxi", new BigDecimal("25.00"), "Travel", LocalDate.of(2026, 7, 11)));
        service.addExpense(new Expense(null, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 6, 15)));
        
        Map<String, Object> summary = service.getMonthlySummary();
        
        assertTrue(summary.containsKey("2026-07"));
        assertTrue(summary.containsKey("2026-06"));
        
        Map<String, Object> julyData = (Map<String, Object>) summary.get("2026-07");
        assertEquals(new BigDecimal("40.00"), julyData.get("total"));
        
        Map<String, BigDecimal> julyCategory = (Map<String, BigDecimal>) julyData.get("byCategory");
        assertEquals(new BigDecimal("15.00"), julyCategory.get("Food"));
        assertEquals(new BigDecimal("25.00"), julyCategory.get("Travel"));
    }
}
