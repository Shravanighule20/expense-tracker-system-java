package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    // PROFILE PAGE

    @GetMapping("/profile")
    public String profilePage(HttpSession session,
                              Model model){

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null){

            return "redirect:/";
        }

        model.addAttribute("user", user);

        return "profile";
    }

    // UPDATE PROFILE

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute User updatedUser,
                                @RequestParam("image") MultipartFile file,
                                HttpSession session)
            throws IOException {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null){

            return "redirect:/";
        }

        // UPDATE NAME

        user.setName(updatedUser.getName());

        // IMAGE UPLOAD

        if(!file.isEmpty()){

            // CREATE UPLOAD FOLDER

            String uploadDir =
                    System.getProperty("user.dir")
                            + "/src/main/resources/static/uploads/";

            File directory =
                    new File(uploadDir);

            if(!directory.exists()){

                directory.mkdirs();
            }

            // UNIQUE FILE NAME

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + file.getOriginalFilename();

            // SAVE FILE

            File saveFile =
                    new File(uploadDir + fileName);

            file.transferTo(saveFile);

            // SAVE DB PATH

            user.setProfilePic(fileName);
        }

        userRepository.save(user);

        // UPDATE SESSION

        session.setAttribute("loggedInUser", user);

        return "redirect:/profile";
    }
}