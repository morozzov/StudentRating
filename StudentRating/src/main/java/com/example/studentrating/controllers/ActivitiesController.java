package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Activity;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.ActivityRepository;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/activities")
public class ActivitiesController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/getByTeacher")
    public String getByTeacher(Model model, HttpSession session) {
        if (Session.isAuthorize(session).equals("TEACHER")) {
            ArrayList<Activity> activities = activityRepository.findAllByAuthor_Id(Session.getSessionId(session));
            ArrayList<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "surname"));

            model.addAttribute("activities", activities);
            model.addAttribute("students", students);
            model.addAttribute("title", "Мои активности");

            return "myTeacherActivities";
        } else return "redirect:/pages/signIn";
    }
}
