package com.example.studentrating.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class PagesController {

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Справка");
        return "about";
    }
}
