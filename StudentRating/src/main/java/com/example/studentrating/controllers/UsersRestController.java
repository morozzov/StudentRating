package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static com.example.studentrating.lib.Encrypt.encryptText;

@RestController
@RequestMapping(path = "/usersRest")
public class UsersRestController {

    private static final Logger log = LoggerFactory.getLogger(UsersRestController.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/updateProfile")
    public String updateProfile(String login, String password1, HttpSession session) {
        try {
            // image - null, password1 = ""
            if (Session.getSessionType(session).equals("STUDENT") || Session.getSessionType(session).equals("ADMIN")) {
                if (!login.equals("") || password1.equals("")) {
                    Student student = studentRepository.findById(Session.getSessionId(session)).get();

                    if (!login.equals(studentRepository.findById(Session.getSessionId(session)).get().getLogin())) {
                        if (login != "" && studentRepository.findByLogin(login) == null && teacherRepository.findByLogin(login) == null)
                            student.setLogin(login);
                        else return "Введите другой логин";
                    }
                    if (!password1.equals("")) student.setPassword(encryptText(password1));

                    studentRepository.save(student);
                    log.info("Student with id:{} was updated", student.getId());
                }
            } else if (Session.getSessionType(session).equals("TEACHER")) {
                if (!login.equals("") || password1.equals("")) {
                    Teacher teacher = teacherRepository.findById(Session.getSessionId(session)).get();

                    if (!login.equals(teacherRepository.findById(Session.getSessionId(session)).get().getLogin())) {
                        if (login != "" && teacherRepository.findByLogin(login) == null && teacherRepository.findByLogin(login) == null)
                            teacher.setLogin(login);
                        else return "Введите другой логин";
                    }
                    if (!password1.equals("")) teacher.setPassword(encryptText(password1));

                    teacherRepository.save(teacher);
                    log.info("Teacher with id:{} was updated", teacher.getId());
                }
            } else {
                return "У вас нет прав на данное действие";
            }

            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @PostMapping("/signIn")
    public String signIn(String login, String password, HttpSession session) {
        Student student = studentRepository.findByLoginAndPassword(login, encryptText(password));
        Teacher teacher = teacherRepository.findByLoginAndPassword(login, encryptText(password));

        if (student != null) {
            Session.setSession(session, student.getId(), student.getRole(), student.isStudentCouncil());

            return "success";
        } else if (teacher != null) {
            Session.setSession(session, teacher.getId(), teacher.getRole(), false);

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

    @DeleteMapping("/deleteNotification")
    public String deleteNotification(Long notificationId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("STUDENT") || Session.isAuthorize(session).equals("ADMIN")) {
                notificationRepository.deleteById(notificationId);
                log.info("Notifications with id:{} was deleted", notificationId);

                return "success";
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (Exception e) {
            return "e.getMessage()";
        }
    }
}
