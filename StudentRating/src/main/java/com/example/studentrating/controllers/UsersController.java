package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private PastYearPointRepository pastYearPointRepository;

    @GetMapping("/getMyProfile")
    public String getById(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            Student student = studentRepository.findById(Session.getSessionId(session)).get();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            ArrayList<Activity> activities = activityRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), student.getId());
            ArrayList<PastYearPoint> pastYearPoints = pastYearPointRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "year"), student.getId());
            ArrayList<Respond> responds = respondRepository.findByExecutor_IdAndStatusIsNot(Sort.by(Sort.Direction.DESC, "completedAt"), student.getId(), "BUSY");
            model.addAttribute("notifications", notifications);
            model.addAttribute("title", "Мой профиль");
            model.addAttribute("activities", activities);
            model.addAttribute("responds", responds);
            model.addAttribute("pastYearPoints", pastYearPoints);
            model.addAttribute("student", student);
            return "profile";
        } else if (Session.isAuthorize(session).equals("TEACHER")) {
            Teacher teacher = teacherRepository.findById(Session.getSessionId(session)).get();
            model.addAttribute("title", "Мой профиль");
            model.addAttribute("teacher", teacher);
            return "teacherProfile";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getSettingsById")
    public String getSettingsById(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            Student student = studentRepository.findById(Session.getSessionId(session)).get();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("user", student);
            model.addAttribute("title", "Настройки");
            return "settings";
        } else if (Session.isAuthorize(session).equals("TEACHER")) {
            Teacher teacher = teacherRepository.findById(Session.getSessionId(session)).get();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("user", teacher);
            model.addAttribute("title", "Настройки");
            return "settings";
        } else return "redirect:/pages/signIn";
    }
}
