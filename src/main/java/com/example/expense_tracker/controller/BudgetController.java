package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Budget;
import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.BudgetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    // BUDGET PAGE

    @GetMapping("/setBudget")
    public String budgetPage(HttpSession session,
                             Model model){

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null){

            return "redirect:/";
        }

        model.addAttribute("budget",
                new Budget());

        return "set-budget";
    }

    // SAVE BUDGET

    @PostMapping("/saveBudget")
    public String saveBudget(@ModelAttribute Budget budget,
                             HttpSession session,
                             Model model){

        User user =
                (User) session.getAttribute("loggedInUser");

        int month = LocalDate.now().getMonthValue();

        int year = LocalDate.now().getYear();

        Budget existingBudget =
                budgetRepository.findByUserAndMonthAndYear(
                        user,
                        month,
                        year
                );

        // ALREADY EXISTS

        if(existingBudget != null){

            model.addAttribute("error",
                    "Budget already set for this month");

            return "set-budget";
        }

        budget.setUser(user);

        budget.setMonth(month);

        budget.setYear(year);

        budgetRepository.save(budget);

        return "redirect:/dashboard";
    }
}