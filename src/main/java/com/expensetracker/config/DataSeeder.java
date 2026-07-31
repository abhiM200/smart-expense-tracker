package com.expensetracker.config;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final ExpenseService expenseService;

    public DataSeeder(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] categories = {"Food", "Travel", "Utilities", "Entertainment", "Shopping"};
        String[] titles = {
                "Lunch with team", "Uber ride", "Electric Bill", "Movie tickets", "Groceries",
                "Flight ticket", "Internet bill", "Coffee", "Dinner", "New shoes",
                "Train ticket", "Gas station", "Netflix subscription", "Gym membership", "Software license"
        };
        
        Random random = new Random();
        
        // Generate 50 fake expenses for the previous year (2025)
        for (int i = 0; i < 50; i++) {
            String category = categories[random.nextInt(categories.length)];
            String title = titles[random.nextInt(titles.length)];
            
            // Random amount between 10.00 and 200.00
            double amountValue = 10 + (200 - 10) * random.nextDouble();
            BigDecimal amount = new BigDecimal(amountValue).setScale(2, RoundingMode.HALF_UP);
            
            // Random date in 2025
            int month = random.nextInt(12) + 1; // 1 to 12
            int day = random.nextInt(28) + 1;   // 1 to 28 (safe for all months)
            LocalDate date = LocalDate.of(2025, month, day);
            
            Expense expense = new Expense(null, title, amount, category, date);
            expenseService.addExpense(expense);
        }
        
        System.out.println("DataSeeder: Injected 50 fake expenses.");
    }
}
