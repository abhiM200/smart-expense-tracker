package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ExpenseRepository {

    private final Map<Long, Expense> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Expense save(Expense expense) {
        if (expense.getId() == null) {
            expense.setId(idGenerator.getAndIncrement());
        }
        storage.put(expense.getId(), expense);
        return expense;
    }

    public List<Expense> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Expense> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public void deleteAll() {
        storage.clear();
        idGenerator.set(1);
    }
}
