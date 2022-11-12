package com.example.studentrating.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UsersController {

    @GetMapping("/getById")
    public String getById(Model model) {
        model.addAttribute("title", "Профиль");
        return "profile";
    }

    @GetMapping("/getSettingsById")
    public String getSettingsById(Model model) {
        model.addAttribute("title", "Настройки");
        return "settings";
    }

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("title", "Авторизация");
        return "signIn";
    }
}
