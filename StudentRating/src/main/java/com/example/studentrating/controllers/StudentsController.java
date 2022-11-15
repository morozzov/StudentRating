package com.example.studentrating.controllers;

import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping("/getById/{id}")
    public String getById(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (isAuthorize(session)) {
            Student student = studentsRepository.findById(id).get();
            model.addAttribute("title", "Профиль");
            model.addAttribute("student", student);
            return "profile";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getAll")
    public String getAll(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            ArrayList<Student> students = studentsRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));
            model.addAttribute("title", "Рейтинг");
            model.addAttribute("students", students);
            return "rating";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getMyProfile")
    public String getById( Model model, HttpSession session) {
        if (isAuthorize(session)) {
            Student student = studentsRepository.findById(getSessionId(session)).get();
            model.addAttribute("title", "Мой профиль");
            model.addAttribute("student", student);
            return "profile";
        } else return "redirect:/pages/signIn";
    }
    @GetMapping("/getSettingsById")
    public String getSettingsById(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            model.addAttribute("title", "Настройки");
            return "settings";
        } else return "redirect:/pages/signIn";
    }

    public void setSession(HttpSession request, Long id) {
        request.setAttribute("id", id);
        request.setAttribute("type", "student");
    }

    public boolean isAuthorize(HttpSession request) {
        if (request.getAttribute("id") != null && request.getAttribute("type") != null) return true;
        else return false;
    }

    public void signOut(HttpSession request) {
        request.setAttribute("id", null);
        request.setAttribute("type", null);
    }

    public Long getSessionId(HttpSession request) {
        return (Long) request.getAttribute("id");
    }
}
