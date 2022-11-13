package com.example.studentrating.controllers;

import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class StudentsController {

    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping("/getById/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        Student student = studentsRepository.findById(id).get();
        model.addAttribute("title", "Профиль");
        model.addAttribute("student", student);
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
