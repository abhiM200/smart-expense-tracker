package com.expensetracker.service;

import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public Expense addExpense(Expense expense) {
        return repository.save(expense);
    }

    public List<Expense> getExpenses(String category) {
        return repository.findAll().stream()
                .filter(e -> category == null || category.isBlank() || e.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalAmount(String category) {
        return getExpenses(category).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean deleteExpense(Long id) {
        return repository.deleteById(id);
    }

    public Map<String, Object> getMonthlySummary() {
        List<Expense> allExpenses = repository.findAll();
        Map<String, Object> summary = new TreeMap<>(Collections.reverseOrder());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, List<Expense>> expensesByMonth = allExpenses.stream()
                .collect(Collectors.groupingBy(e -> e.getDate().format(formatter)));

        for (Map.Entry<String, List<Expense>> entry : expensesByMonth.entrySet()) {
            String month = entry.getKey();
            List<Expense> monthExpenses = entry.getValue();

            BigDecimal total = monthExpenses.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, BigDecimal> byCategory = monthExpenses.stream()
                    .collect(Collectors.groupingBy(
                            Expense::getCategory,
                            Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                    ));

            Map<String, Object> monthData = new HashMap<>();
            monthData.put("total", total);
            monthData.put("byCategory", byCategory);

            summary.put(month, monthData);
        }

        return summary;
    }

    public void clearAll() {
        repository.deleteAll();
    }
}
