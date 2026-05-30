package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.ExpenseRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // ADD EXPENSE PAGE
    @GetMapping("/addExpense")
    public String addExpensePage(HttpSession session,
                                 Model model){

        if(session.getAttribute("loggedInUser") == null){

            return "redirect:/";
        }

        model.addAttribute("expense",
                new Expense());

        return "add-expense";
    }

    // SAVE EXPENSE
    @PostMapping("/saveExpense")
    public String saveExpense(@ModelAttribute Expense expense,
                              HttpSession session){

        User user =
                (User) session.getAttribute("loggedInUser");

        expense.setUser(user);

        if(expense.getExpenseDate() == null){

            expense.setExpenseDate(LocalDate.now());
        }

        expenseRepository.save(expense);

        return "redirect:/manageExpense";
    }

    @GetMapping("/manageExpense")
    public String manageExpense(HttpSession session,
                                Model model,

                                @RequestParam(required = false)
                                String category,

                                @RequestParam(required = false)
                                String date,

                                @RequestParam(required = false)
                                Integer month){

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null){

            return "redirect:/";
        }

        List<Expense> expenses =
                new ArrayList<>();

        // CATEGORY SEARCH

        if(category != null &&
                !category.isEmpty()){

            expenses =
                    expenseRepository
                            .findByUserAndCategoryContainingIgnoreCase(
                                    user,
                                    category
                            );
        }

        // DATE FILTER

        else if(date != null &&
                !date.isEmpty()){

            expenses =
                    expenseRepository
                            .findByUserAndExpenseDate(
                                    user,
                                    LocalDate.parse(date)
                            );
        }

        // MONTH FILTER

        else if(month != null){

            expenses =
                    expenseRepository
                            .filterByMonth(
                                    user,
                                    month
                            );
        }

        // ALL EXPENSES

        else{

            expenses =
                    expenseRepository.findByUser(user);
        }

        model.addAttribute("expenses", expenses);

        return "manage-expense";
    }
    // EDIT PAGE

    @GetMapping("/editExpense/{id}")
    public String editExpensePage(@PathVariable Long id,
                                  Model model,
                                  HttpSession session){

        if(session.getAttribute("loggedInUser") == null){

            return "redirect:/";
        }

        Expense expense =
                expenseRepository.findById(id).orElse(null);

        model.addAttribute("expense", expense);

        return "edit-expense";
    }


// UPDATE EXPENSE

    @PostMapping("/updateExpense")
    public String updateExpense(@ModelAttribute Expense expense,
                                HttpSession session){

        User user =
                (User) session.getAttribute("loggedInUser");

        expense.setUser(user);

        expenseRepository.save(expense);

        return "redirect:/manageExpense";
    }


// DELETE EXPENSE

    @GetMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable Long id){

        expenseRepository.deleteById(id);

        return "redirect:/manageExpense";
    }
}