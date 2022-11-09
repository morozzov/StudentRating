package com.example.studentrating.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TasksController {

    @GetMapping("/getAll")
    public String getAll(Model model) {
        model.addAttribute("title", "Поручения");
        return "tasks";
    }

    @GetMapping("/getByUser")
    public String getByUser(Model model) {
        model.addAttribute("title", "Мои поручения");
        return "myTasks";
    }
}
