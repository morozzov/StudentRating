package com.example.studentrating.controllers;

import com.example.studentrating.dto.StudentDTO;
import com.example.studentrating.models.Student;
import com.example.studentrating.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api")
public class ApiController {

    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping("/getById/{id}")
    public StudentDTO getById(@PathVariable("id") Long id) {
        Student student = studentsRepository.findById(id).get();

        StudentDTO studentDTO = new StudentDTO(student);

        return studentDTO;
    }

    @GetMapping("/getAll")
    public ArrayList<StudentDTO> getAll() {
        ArrayList<Student> students = studentsRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));

        ArrayList<StudentDTO> studentDtoList = new ArrayList<>();

        for (var entityItem : students) {
            studentDtoList.add(new StudentDTO(entityItem));
        }

        return studentDtoList;
    }

    @GetMapping("/getByLogin/{login}")
    public StudentDTO getByLogin(@PathVariable("login") String login) {
        Student student = studentsRepository.findByLogin(login);

        StudentDTO studentDTO = new StudentDTO(student);

        return studentDTO;
    }

    @PostMapping("/signIn")
    public String signIn(String login, String password, HttpSession session) {
//        model.addAttribute("title", "Авторизация");
        Student student = studentsRepository.findByLoginAndPassword(login, encryptText(password));
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

    private void setSession(HttpSession request, Long id, String type) {
        request.setAttribute("id", id);
        request.setAttribute("type", type);
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
