package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class MainController {

    @GetMapping("/")
    public String about(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN") || Session.isAuthorize(session).equals("TEACHER")) {
            return "redirect:/students/getAll";
        } else return "redirect:/pages/signIn";
    }
}
