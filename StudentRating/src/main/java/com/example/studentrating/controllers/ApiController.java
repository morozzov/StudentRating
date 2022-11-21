package com.example.studentrating.controllers;

import com.example.studentrating.dto.StudentDTO;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.NotificationRepository;
import com.example.studentrating.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api")
public class ApiController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/getById/{id}")
    public StudentDTO getById(@PathVariable("id") Long id) {
        Student student = studentRepository.findById(id).get();

        StudentDTO studentDTO = new StudentDTO(student);

        return studentDTO;
    }

    @GetMapping("/getAll")
    public ArrayList<StudentDTO> getAll() {
        ArrayList<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));

        ArrayList<StudentDTO> studentDtoList = new ArrayList<>();

        for (var entityItem : students) {
            studentDtoList.add(new StudentDTO(entityItem));
        }

        return studentDtoList;
    }

    @GetMapping("/getByLogin/{login}")
    public StudentDTO getByLogin(@PathVariable("login") String login) {
        Student student = studentRepository.findByLogin(login);

        StudentDTO studentDTO = new StudentDTO(student);

        return studentDTO;
    }

    @PostMapping("/signIn")
    public String signIn(String login, String password, HttpSession session) {
        Student student = studentRepository.findByLoginAndPassword(login, encryptText(password));
        if (student != null) {
            setSession(session, student.getId(), "STUDENT");
            return "success";
        } else {
            return "error";
        }
    }

    @PostMapping("/signOut")
    public String signOut(HttpSession request) {
        try {
            request.setAttribute("id", null);
            request.setAttribute("type", null);
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
                Student student = studentRepository.findById(getSessionId(session)).get();
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

    private void setSession(HttpSession request, Long id, String type) {
        request.setAttribute("id", id);
        request.setAttribute("type", type);
    }

    public Long getSessionId(HttpSession request) {
        return (Long) request.getAttribute("id");
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
