package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    // ADMIN DASHBOARD

    @GetMapping("/adminDashboard")
    public String adminDashboard(HttpSession session,
                                 Model model){

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null ||
                !user.getRole().equals("ADMIN")){

            return "redirect:/";
        }

        List<User> users =
                userRepository.findAll();

        Long totalUsers =
                userRepository.count();

        Long activeUsers =
                userRepository.countActiveUsers();

        Long inactiveUsers =
                userRepository.countInactiveUsers();

        Double systemExpense =
                expenseRepository.getSystemExpense();

        model.addAttribute("users", users);

        model.addAttribute("totalUsers",
                totalUsers);

        model.addAttribute("activeUsers",
                activeUsers);

        model.addAttribute("inactiveUsers",
                inactiveUsers);

        model.addAttribute("systemExpense",
                systemExpense);

        return "admin-dashboard";
    }

    // DEACTIVATE USER

    @GetMapping("/deactivateUser/{id}")
    public String deactivateUser(@PathVariable Long id){

        User user =
                userRepository.findById(id).orElse(null);

        if(user != null){

            user.setStatus("INACTIVE");

            userRepository.save(user);
        }

        return "redirect:/adminDashboard";
    }

    // ACTIVATE USER

    @GetMapping("/activateUser/{id}")
    public String activateUser(@PathVariable Long id){

        User user =
                userRepository.findById(id).orElse(null);

        if(user != null){

            user.setStatus("ACTIVE");

            userRepository.save(user);
        }

        return "redirect:/adminDashboard";
    }
}