package com.example.studentrating.controllers;

import com.example.studentrating.models.Activity;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.PastYearPoint;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.ActivityRepository;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.PastYearPointRepository;
import com.example.studentrating.repositories.StudentRepository;
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
    private NotificationRepository notificationRepository;

    @GetMapping("/getById/{id}")
    public String getById(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (isAuthorize(session)) {
            Student student = studentRepository.findById(id).get();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), getSessionId(session));
            ArrayList<Activity> activities = activityRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), student.getId());
            ArrayList<PastYearPoint> pastYearPoints = pastYearPointRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "year"), student.getId());
            model.addAttribute("notifications", notifications);
            model.addAttribute("title", id.equals(getSessionId(session)) ? "Мой профиль" : "Профиль");
            model.addAttribute("buttonsHidden", !id.equals(getSessionId(session)));
            model.addAttribute("activities", activities);
            model.addAttribute("pastYearPoints", pastYearPoints);
            model.addAttribute("student", student);
            return "profile";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getAll")
    public String getAll(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            ArrayList<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), getSessionId(session));
            model.addAttribute("notifications", notifications);
            model.addAttribute("title", "Рейтинг");
            model.addAttribute("students", students);
            return "rating";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getMyProfile")
    public String getById(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            Student student = studentRepository.findById(getSessionId(session)).get();
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), getSessionId(session));
            ArrayList<Activity> activities = activityRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), student.getId());
            ArrayList<PastYearPoint> pastYearPoints = pastYearPointRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "year"), student.getId());
            model.addAttribute("notifications", notifications);
            model.addAttribute("title", "Мой профиль");
            model.addAttribute("activities", activities);
            model.addAttribute("pastYearPoints", pastYearPoints);
            model.addAttribute("student", student);
            return "profile";
        } else return "redirect:/pages/signIn";
    }

    @GetMapping("/getSettingsById")
    public String getSettingsById(Model model, HttpSession session) {
        if (isAuthorize(session)) {
            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(Sort.by(Sort.Direction.DESC, "createdAt"), getSessionId(session));
            model.addAttribute("notifications", notifications);
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
