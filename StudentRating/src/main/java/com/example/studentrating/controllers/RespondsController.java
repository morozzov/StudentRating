package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Respond;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.RespondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/responds")
public class RespondsController {

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/getByUser")
    public String getByUser(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            ArrayList<Respond> responds = respondRepository.findAllByExecutor_IdAndStatus(Session.getSessionId(session), "BUSY");
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("responds", responds);
            model.addAttribute("title", "Мои поручения");
            return "myTasks";
        } else return "redirect:/pages/signIn";
    }
}
