package com.expensetracker;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseApiControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseService expenseService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        expenseService.clearAll();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testAddExpenseSuccess() throws Exception {
        Expense expense = new Expense(null, "Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 7, 10));
        
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Lunch"));
    }

    @Test
    void testAddExpenseValidationFailure() throws Exception {
        Expense expense = new Expense(null, "", new BigDecimal("-5.00"), "", null); // Invalid data
        
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetExpenses() throws Exception {
        expenseService.addExpense(new Expense(null, "Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 7, 10)));
        
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Lunch"));
    }

    @Test
    void testDeleteExpenseSuccess() throws Exception {
        Expense saved = expenseService.addExpense(new Expense(null, "Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 7, 10)));
        
        mockMvc.perform(delete("/api/expenses/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteExpenseNotFound() throws Exception {
        mockMvc.perform(delete("/api/expenses/999"))
                .andExpect(status().isNotFound());
    }
}
