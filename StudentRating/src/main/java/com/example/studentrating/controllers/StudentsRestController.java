package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.example.studentrating.lib.Encrypt.encryptText;

@RestController
@RequestMapping(path = "/studentsRest")
public class StudentsRestController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private PastYearPointRepository pastYearPointRepository;

    @PostMapping("/signIn")
    public String signIn(String login, String password, HttpSession session) {
        Student student = studentRepository.findByLoginAndPassword(login, encryptText(password));
        if (student != null) {
            Session.setSession(session, student.getId(), student.getRole());
            return "success";
        } else {
            return "error";
        }
    }

    @PostMapping("/signOut")
    public String signOut(HttpSession request) {
        try {
            Session.signOut(request);
            return "success";
        } catch (Exception e) {
            return "e.getMessage()";
        }
    }

    @PostMapping("/addNew")
    public String addNew(String surname, String name, String patronymic, String login, String group, @RequestParam(defaultValue = "false") boolean isStudentCouncil, HttpSession session) {
        try {
            if (studentRepository.findByLogin(login) == null && teacherRepository.findByLogin(login) == null) {
                Student student = new Student();
                student.setSurname(surname);
                student.setName(name);
                student.setPatronymic(patronymic);
                student.setLogin(login);
                student.setGroupName(group);
                student.setStudentCouncil(isStudentCouncil);
                student.setPassword(encryptText("123"));
                student.setImageUrl("/img/avatars/personal.svg");
                student.setRole("STUDENT");
                student.setPoints(0);
                studentRepository.save(student);
                return "success";
            } else {
                return "Введите другой логин";
            }

//            return student;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/updateProfile")
    public String updateProfile(String login, String password1, HttpSession session) {
        try {
            // image - null, password1 = ""
            if (!login.equals("") || password1.equals("")) {
                Student student = studentRepository.findById(Session.getSessionId(session)).get();
                if (!login.equals(studentRepository.findById(Session.getSessionId(session)).get().getLogin())) {
                    if (login != "" && studentRepository.findByLogin(login) == null && teacherRepository.findByLogin(login) == null)
                        student.setLogin(login);
                    else return "Введите другой логин";
                }
                if (!password1.equals("")) student.setPassword(encryptText(password1));
                studentRepository.save(student);
            }
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/deleteById")
    public String deleteById(Long studentId, HttpSession session) {
        try {
            ArrayList<PastYearPoint> pastYearPoints = pastYearPointRepository.findAllByStudent_Id(studentId);
            pastYearPointRepository.deleteAll(pastYearPoints);

            ArrayList<Activity> activities = activityRepository.findAllByStudent_Id(studentId);
            activityRepository.deleteAll(activities);

            ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(studentId);
            notificationRepository.deleteAll(notifications);

            ArrayList<Respond> responds = respondRepository.findAllByExecutor_Id(studentId);
            respondRepository.deleteAll(responds);

            studentRepository.deleteById(studentId);
            return "success";

        } catch (
                Exception e) {
            return e.getMessage();
        }

    }

    @DeleteMapping("/deleteNotification")
    public String deleteNotification(Long notificationId) {
        try {
            notificationRepository.deleteById(notificationId);
            return "success";
        } catch (Exception e) {
            return "e.getMessage()";
        }
    }
}
