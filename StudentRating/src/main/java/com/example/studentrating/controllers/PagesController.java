package com.example.studentrating.controllers;

import com.example.studentrating.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/pages")
public class PagesController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            model.addAttribute("title", "Справка");
            return "about";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("title", "Авторизация");
        return "signIn";
    }


    public void setSession(HttpSession request, Long id, String type) {
        request.setAttribute("id", id);
        request.setAttribute("type", type);
    }

    public boolean isAuthorize(HttpSession request) {
        if (request.getAttribute("id") != null && request.getAttribute("type") != null) return true;
        else return false;
    }
}
