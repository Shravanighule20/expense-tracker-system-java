package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Budget;
import com.example.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository
        extends JpaRepository<Budget, Long> {

    Budget findByUserAndMonthAndYear(
            User user,
            Integer month,
            Integer year
    );
}