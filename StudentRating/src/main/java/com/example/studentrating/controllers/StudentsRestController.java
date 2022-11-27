package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/updateProfile")
    public String updateProfile(String login, String password1, HttpSession session) {
        try {
            // image - null, password1 = ""
            if (login != "" || password1 != "") {
                Student student = studentRepository.findById(Session.getSessionId(session)).get();
                if (login != "") student.setLogin(login);
                if (password1 != "") student.setPassword(encryptText(password1));
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
