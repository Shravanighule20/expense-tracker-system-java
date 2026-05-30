package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // LOGIN PAGE
    @GetMapping("/")
    public String loginPage() {

        return "login";
    }

    // REGISTER PAGE
    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    // REGISTER USER
    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute User user,
                               Model model) {

        User existingUser =
                userRepository.findByEmail(user.getEmail());

        if(existingUser != null){

            model.addAttribute("error",
                    "Email already exists");

            return "register";
        }

        userRepository.save(user);

        model.addAttribute("success",
                "Registration Successful");

        return "login";
    }

    // LOGIN USER
    @PostMapping("/loginUser")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        User user =
                userRepository.findByEmail(email);

        if(user != null &&
                user.getPassword().equals(password) &&
                user.getStatus().equals("ACTIVE")) {

            // SESSION

            session.setAttribute("loggedInUser", user);

            // ADMIN LOGIN

            if(user.getRole().equals("ADMIN")){

                return "redirect:/adminDashboard";
            }

            // USER LOGIN

            return "redirect:/dashboard";
        }

        // INVALID LOGIN

        model.addAttribute("error",
                "Invalid Email or Password");

        return "login";
    }
    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "redirect:/";
    }
}