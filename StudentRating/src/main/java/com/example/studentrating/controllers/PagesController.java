package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Student;
import com.example.studentrating.models.Teacher;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.StudentRepository;
import com.example.studentrating.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/pages")
public class PagesController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("title", "Справка");
            return "about";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("title", "Авторизация");
        return "signIn";
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("ADMIN")) {
            ArrayList<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));
            ArrayList<Teacher> teachers = teacherRepository.findAll();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("students", students);
            model.addAttribute("teachers", teachers);
            model.addAttribute("title", "Панель администратора");
            return "admin";
        } else return "redirect:/pages/signIn";
    }
}
