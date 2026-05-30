package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Budget;
import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.BudgetRepository;
import com.example.expense_tracker.repository.ExpenseRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session,
                            Model model){

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null){

            return "redirect:/";
        }

        // TOTAL EXPENSE

        Double totalExpense =
                expenseRepository.getTotalExpense(user);

        // TODAY EXPENSE

        Double todayExpense =
                expenseRepository.getTodayExpense(
                        user,
                        LocalDate.now()
                );

        // LAST 7 DAYS

        Double last7DaysExpense =
                expenseRepository.getLast7DaysExpense(
                        user,
                        LocalDate.now().minusDays(7)
                );

        // LAST 30 DAYS

        Double last30DaysExpense =
                expenseRepository.getLast30DaysExpense(
                        user,
                        LocalDate.now().minusDays(30)
                );

        // BUDGET

        int month = LocalDate.now().getMonthValue();

        int year = LocalDate.now().getYear();

        Budget budget =
                budgetRepository.findByUserAndMonthAndYear(
                        user,
                        month,
                        year
                );

        Double budgetAmount = 0.0;

        Double remainingBudget = 0.0;

        if(budget != null){

            budgetAmount =
                    budget.getBudgetAmount();

            remainingBudget =
                    budgetAmount - totalExpense;
        }

        // CATEGORY CHART

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        double food = 0;
        double travel = 0;
        double shopping = 0;
        double bills = 0;
        double entertainment = 0;

        for(Expense e : expenses){

            switch (e.getCategory()){

                case "Food" ->
                        food += e.getAmount();

                case "Travel" ->
                        travel += e.getAmount();

                case "Shopping" ->
                        shopping += e.getAmount();

                case "Bills" ->
                        bills += e.getAmount();

                case "Entertainment" ->
                        entertainment += e.getAmount();
            }
        }

        // SEND DATA

        model.addAttribute("todayExpense",
                todayExpense);

        model.addAttribute("last7DaysExpense",
                last7DaysExpense);

        model.addAttribute("last30DaysExpense",
                last30DaysExpense);

        model.addAttribute("totalExpense",
                totalExpense);

        model.addAttribute("budgetAmount",
                budgetAmount);

        model.addAttribute("remainingBudget",
                remainingBudget);

        model.addAttribute("food",
                food);

        model.addAttribute("travel",
                travel);

        model.addAttribute("shopping",
                shopping);

        model.addAttribute("bills",
                bills);

        model.addAttribute("entertainment",
                entertainment);

        return "dashboard";
    }
}