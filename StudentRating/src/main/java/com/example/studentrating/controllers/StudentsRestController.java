package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.StudentRepository;
import com.example.studentrating.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(path = "/studentsRest")
public class StudentsRestController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private NotificationRepository notificationRepository;

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
                student.setPassword("3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2");
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
            return "e.getMessage()";
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

    private static String encryptText(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);

            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
