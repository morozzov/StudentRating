package com.example.studentrating.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rating")
public class RatingController {

    @GetMapping("/getAll")
    public String getAll(Model model) {
        model.addAttribute("title", "Рейтинг");
        return "rating";
    }

}
