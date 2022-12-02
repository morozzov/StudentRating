package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(TeachersRestController.class);

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

    @PostMapping("/addNew")
    public String addNew(String surname, String name, String patronymic, String login, String group, @RequestParam(defaultValue = "false") boolean isStudentCouncil, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("ADMIN")) {
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
                    log.info("Student with id:{} was created", student.getId());
                    return "success";
                } else {
                    return "Введите другой логин";
                }
            } else {
                return "У вас нет прав на данное действие";
            }
//            return student;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/deleteById")
    public String deleteById(Long studentId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("ADMIN")) {
                ArrayList<PastYearPoint> pastYearPoints = pastYearPointRepository.findAllByStudent_Id(studentId);
                pastYearPointRepository.deleteAll(pastYearPoints);

                ArrayList<Activity> activities = activityRepository.findAllByStudent_Id(studentId);
                activityRepository.deleteAll(activities);

                ArrayList<Notification> notifications = notificationRepository.findAllByStudent_Id(studentId);
                notificationRepository.deleteAll(notifications);

                ArrayList<Respond> responds = respondRepository.findAllByExecutor_Id(studentId);
                respondRepository.deleteAll(responds);

                studentRepository.deleteById(studentId);
                log.info("Student with id:{} was deleted", studentId);
                return "success";
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (
                Exception e) {
            return e.getMessage();
        }
    }
}
