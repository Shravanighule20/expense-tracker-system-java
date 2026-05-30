package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);
    // SEARCH BY CATEGORY

    List<Expense> findByUserAndCategoryContainingIgnoreCase(
            User user,
            String category
    );


// FILTER BY DATE

    List<Expense> findByUserAndExpenseDate(
            User user,
            LocalDate expenseDate
    );


// FILTER BY MONTH

    @Query("""
        SELECT e
        FROM Expense e
        WHERE e.user = :user
        AND MONTH(e.expenseDate) = :month
        """)
    List<Expense> filterByMonth(
            User user,
            int month
    );

    // TOTAL EXPENSE
    @Query("""
            SELECT COALESCE(SUM(e.amount),0)
            FROM Expense e
            WHERE e.user = :user
            """)
    Double getTotalExpense(User user);

    // TODAY EXPENSE
    @Query("""
            SELECT COALESCE(SUM(e.amount),0)
            FROM Expense e
            WHERE e.user = :user
            AND e.expenseDate = :date
            """)
    Double getTodayExpense(User user,
                           LocalDate date);

    // LAST 7 DAYS
    @Query("""
            SELECT COALESCE(SUM(e.amount),0)
            FROM Expense e
            WHERE e.user = :user
            AND e.expenseDate >= :date
            """)
    Double getLast7DaysExpense(User user,
                               LocalDate date);

    // LAST 30 DAYS
    @Query("""
            SELECT COALESCE(SUM(e.amount),0)
            FROM Expense e
            WHERE e.user = :user
            AND e.expenseDate >= :date
            """)
    Double getLast30DaysExpense(User user,
                                LocalDate date);
    // TOTAL SYSTEM EXPENSE

    @Query("""
        SELECT COALESCE(SUM(e.amount),0)
        FROM Expense e
        """)
    Double getSystemExpense();
}