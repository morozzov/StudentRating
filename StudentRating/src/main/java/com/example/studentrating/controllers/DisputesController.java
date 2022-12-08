package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Dispute;
import com.example.studentrating.models.Notification;
import com.example.studentrating.repositories.DisputeRepository;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/disputes")
public class DisputesController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DisputeRepository disputeRepository;

    @GetMapping("/getByUserId")
    public String disputes(Model model, HttpSession session) {
        ArrayList<Dispute> disputes = null;

        if (Session.isAuthorize(session).equals("TEACHER")) {

            disputes = disputeRepository.findAllByTeacher_Id(Session.getSessionId(session));

            model.addAttribute("disputes", disputes);
            model.addAttribute("title", "Споры");

            return "disputes";
        } else if (studentRepository.findById(Session.getSessionId(session)).get().isStudentCouncil()) {
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));

            disputes = disputeRepository.findAllByStudent_Id(Session.getSessionId(session));

            model.addAttribute("notifications", notifications);
            model.addAttribute("disputes", disputes);
            model.addAttribute("title", "Споры");

            return "disputes";
        } else return "redirect:/pages/signIn";
    }
}
