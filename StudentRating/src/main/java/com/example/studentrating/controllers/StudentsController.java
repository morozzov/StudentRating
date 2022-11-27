package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
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
    private StudentRepository studentRepository;

    @Autowired
    private PastYearPointRepository pastYearPointRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/getById/{id}")
    public String getById(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            Student student = studentRepository.findById(id).get();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            ArrayList<Activity> activities = activityRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), student.getId());
            ArrayList<PastYearPoint> pastYearPoints = pastYearPointRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "year"), student.getId());
            ArrayList<Respond> responds = respondRepository.findByExecutor_IdAndStatusIsNot(Sort.by(Sort.Direction.DESC, "completedAt"), student.getId(), "BUSY");
            model.addAttribute("notifications", notifications);
            model.addAttribute("title", id.equals(Session.getSessionId(session)) ? "Мой профиль" : "Профиль");
            model.addAttribute("buttonsHidden", !id.equals(Session.getSessionId(session)));
            model.addAttribute("activities", activities);
            model.addAttribute("responds", responds);
            model.addAttribute("pastYearPoints", pastYearPoints);
            model.addAttribute("student", student);
            return "profile";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getAll")
    public String getAll(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            ArrayList<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("title", "Рейтинг");
            model.addAttribute("students", students);

            return "rating";
        } else return "redirect:/pages/signIn";
    }

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
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getSettingsById")
    public String getSettingsById(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
            Student student = studentRepository.findById(Session.getSessionId(session)).get();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), Session.getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("student", student);
            model.addAttribute("title", "Настройки");
            return "settings";
        } else return "redirect:/pages/signIn";
    }
}
